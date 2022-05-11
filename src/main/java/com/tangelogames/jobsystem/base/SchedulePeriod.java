package com.tangelogames.jobsystem.base;

public enum SchedulePeriod {

    ONE_SECOND(1, 1000),
    ONE_HOUR(1, (60 * 60 * 1000)),
    TWO_HOURS(2, 2 * (60 * 60 * 1000)),
    SIX_HOURS(6, 6 * (60 * 60 * 1000)),
    TWELVE_HOURS(12, 12 * (60 * 60 * 1000));

    private int periodValue;
    private long delayInMiliSec;

    SchedulePeriod(int periodValue, long delayInMiliSec) {
        this.periodValue = periodValue;
        this.delayInMiliSec = delayInMiliSec;
    }

    public int getPeriodValue() {
        return periodValue;
    }

    public long getDelayInMiliSec() {
        return delayInMiliSec;
    }
}
