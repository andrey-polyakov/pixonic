package com.company.core;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;


class PrioritizedTask implements Comparable<PrioritizedTask> {
    private Callable callable;
    private LocalDateTime dateTime;

    public PrioritizedTask(Callable callable, LocalDateTime dateTime) {
        this.callable = callable;
        this.dateTime = dateTime;
    }

    public Callable getCallable() {
        return callable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return dateTime.compareTo(o.dateTime);
    }
}