package com.tangelogames.jobsystem.base;

import java.util.UUID;

public abstract class AbstractScheduledJob {
    public static enum Status {
        RUNNING, WAITING
    }

    private SchedulePeriod period;
    private Status status;

    private UUID id;

    private String name;

    public AbstractScheduledJob(SchedulePeriod period, String name) {
        this.period = period;
        this.status = Status.WAITING;
        this.name = name;
        this.id = UUID.randomUUID();
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

    public String getName() {
        return name;
    }
}
