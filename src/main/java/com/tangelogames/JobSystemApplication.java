package com.tangelogames;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.base.worker.JobWorker;
import io.quarkus.runtime.Startup;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Startup
@ApplicationScoped
public class JobSystemApplication {

    @Inject
    Instance<AbstractScheduledJob> jobs;

    @Inject
    Vertx vertx;

    @PostConstruct
    void init() {
        AtomicBoolean failed = new AtomicBoolean(false);
        CountDownLatch deployLatch = new CountDownLatch((int) jobs.stream().count());

        Handler<AsyncResult<String>> completionHandler = ar -> {
            if (ar.failed()) {
                log.error("Failed to deploy verticle", ar.cause());
                failed.compareAndSet(false, true);
            }
            deployLatch.countDown();
        };

        jobs.forEach(job -> {
            JobWorker worker = new JobWorker(job);
            vertx.deployVerticle(worker, completionHandler);
        });

        try {
            if (!deployLatch.await(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout waiting for verticle deployments");
            } else if (failed.get()) {
                throw new RuntimeException("Failure while deploying verticles");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // start listener here

    }
}
