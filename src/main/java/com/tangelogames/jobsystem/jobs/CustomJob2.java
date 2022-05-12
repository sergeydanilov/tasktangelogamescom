package com.tangelogames.jobsystem.jobs;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.base.SchedulePeriod;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Log4j2
@Dependent
public class CustomJob2 extends AbstractScheduledJob {

    public CustomJob2() {
        super(SchedulePeriod.TWO_HOURS, CustomJob2.class.getCanonicalName());
    }

    @Inject
    private Vertx vertx;

    @Override
    protected void doJob() {
        log.info("this is execution from " + this.getClass().getSimpleName());
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
