package com.tangelogames.jobsystem.base.worker;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JobWorker extends AbstractVerticle {

    private final AbstractScheduledJob job;

    private long timerID;

    public JobWorker(AbstractScheduledJob job) {
        this.job = job;
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        super.start(promise);
        startTimer(job.getDelayInMiliSec());
    }

    void startTimer(long delay) {
//        log.info("Delay {} {}", job.getClass().getSimpleName(), delay);
        if (delay <= 0) {
            startTimeout();
            return;
        }
        timerID = vertx.setTimer(delay, this::handleTimer);
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
}
