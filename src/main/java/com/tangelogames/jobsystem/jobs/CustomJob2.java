package com.tangelogames.jobsystem.jobs;

import com.tangelogames.jobsystem.base.AbstractScheduledJob;
import com.tangelogames.jobsystem.base.SchedulePeriod;

import javax.enterprise.context.Dependent;

@Dependent
public class CustomJob2 extends AbstractScheduledJob {

    public CustomJob2() {
        super(SchedulePeriod.TWO_HOURS);
    }

    @Override
    public void execute() {
        System.out.println("this is message from " + this.getClass().getSimpleName());
    }
}
