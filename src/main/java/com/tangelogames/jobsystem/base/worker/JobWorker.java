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

        startTimer(getExecutionDelayInMiliSec());

        final String topicName = job.getClass().getCanonicalName();

//        var bus = vertx.eventBus().consumer(topicName, msg->{executeCommand(msg);});
    }

    private void startTimer(long delay) {
        log.info("Delay {} {}", job.getClass().getSimpleName(), delay);
        if (delay <= 0) {
            startTimeout();
            return;
        }
        timerID = vertx.setTimer(delay, id -> {
            vertx.executeBlocking(handler(), false,
                    res -> {
                        if (res.failed()) log.error(res.cause().getMessage(), res.cause());
                    }
            );
        });
    }

    private void startTimeout() {
        timerID = vertx.setTimer(60_000, id -> {
            startTimer(getExecutionDelayInMiliSec());
        });
    }

    private long getExecutionDelayInMiliSec() {
        return job.getDelayInMiliSec();
    }

    private Handler<Promise<Void>> handler() {
        return p -> {
            try {
                job.execute();
                startTimer(getExecutionDelayInMiliSec());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                startTimer(60_000 * 5);
            }
        };
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (timerID > 0) vertx.cancelTimer(timerID);
    }
}
