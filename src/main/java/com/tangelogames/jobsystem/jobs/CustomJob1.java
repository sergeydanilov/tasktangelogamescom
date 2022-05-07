package com.tangelogames.jobsystem.jobs;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.base.SchedulePeriod;

import javax.enterprise.context.Dependent;

@Dependent
public class CustomJob1 extends AbstractScheduledJob {

    public CustomJob1() {
        super(SchedulePeriod.ONE_HOUR);
    }

    @Override
    public void execute() {
        System.out.println("this is message from " + this.getClass().getSimpleName());
    }
}
