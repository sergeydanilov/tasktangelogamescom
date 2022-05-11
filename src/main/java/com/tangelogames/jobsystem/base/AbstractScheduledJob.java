package com.tangelogames.jobsystem.base;

public abstract class AbstractScheduledJob {
    public static enum Status {
        RUNNING, WAITING
    }

    private SchedulePeriod period;
    private Status status;

    public AbstractScheduledJob(SchedulePeriod period) {
        this.period = period;
        this.status = Status.WAITING;
    }

    public long getDelayInMiliSec() {
        return period.getDelayInMiliSec();
    }

    public Status getStatus() {
        return status;
    }

    public void execute() {
        status = Status.RUNNING;
        doJob();
        status = Status.WAITING;
    }

    protected abstract void doJob();
}
