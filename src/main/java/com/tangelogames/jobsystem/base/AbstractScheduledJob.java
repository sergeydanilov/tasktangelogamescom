package com.tangelogames.jobsystem.base;

public abstract class AbstractScheduledJob {
    private SchedulePeriod period;

    public AbstractScheduledJob(SchedulePeriod period) {
        this.period = period;
    }

    public long getDelayInMiliSec() {
        return period.getDelayInMiliSec();
    }

    public abstract void execute();
}
