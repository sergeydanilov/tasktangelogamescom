package com.tangelogames.jobsystem.base.worker;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JobWorker extends AbstractVerticle {

    private final AbstractScheduledJob job;

    private long timerID;

    public JobWorker(AbstractScheduledJob job) {
        this.job = job;
    }

    void initEventBusConsumer() {
        MessageConsumer<String> consumer = vertx.eventBus().consumer(job.getName());
        consumer.handler(message -> {
            final String cmd = message.body();
            log.info("I have received a message: " + cmd);
            if ("run".equalsIgnoreCase(cmd)) {
                log.info("do run cmd");
                if (job.getStatus().equals(AbstractScheduledJob.Status.RUNNING)) {
                    message.reply(job.getName() + " already running : " + this.job.getStatus());
                } else {
                    this.startImmediately();
                    message.reply(job.getName() + " stated");
                }
            } else {
                message.reply(job.getName() + " " + this.job.getStatus());
            }
        });
    }


    @Override
    public void start(Promise<Void> promise) throws Exception {
        super.start(promise);
        startTimer(job.getDelayInMiliSec());
        initEventBusConsumer();
    }

    void startTimer(long delay) {
        log.info("Delay {} {}", job.getClass().getSimpleName(), delay);
        if (delay <= 0) {
            startTimeout();
            return;
        }
        timerID = vertx.setTimer(delay, this::handleTimer);
    }

    void startImmediately() {
        log.info("startImmediately {}", job.getClass().getSimpleName());
        if (timerID > 0) vertx.cancelTimer(timerID);
        timerID = vertx.setTimer(10, this::handleTimer);
    }

    void startTimeout() {
        timerID = vertx.setTimer(60_000, id -> {
            startTimer(job.getDelayInMiliSec());
        });
    }

    Handler<Promise<Void>> handler() {
        return this::handleJobExecute;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (timerID > 0) vertx.cancelTimer(timerID);
    }

    void handleTimer(Long id) {
        vertx.executeBlocking(handler(), false,
                res -> {
                    if (res.failed()) log.error(res.cause().getMessage(), res.cause());
                }
        );
    }

    void handleJobExecute(Promise<Void> p) {
        try {
            job.execute();
            startTimer(job.getDelayInMiliSec());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            startTimer(60_000 * 5);
        }
    }

    public AbstractScheduledJob getJob() {
        return job;
    }
}
