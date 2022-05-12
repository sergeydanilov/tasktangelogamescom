package com.tangelogames.jobsystem.jobs;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.base.SchedulePeriod;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Log4j2
@Dependent
public class CustomJob1 extends AbstractScheduledJob {

    @Inject
    private Vertx vertx;

    public CustomJob1() {
        super(SchedulePeriod.ONE_SECOND, "custom.job.1");
    }

    @Override
    protected void doJob() {
        log.info("this is execution from " + this.getClass().getSimpleName());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
