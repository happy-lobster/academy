package com.ctw.workstation.utils;

import java.time.LocalDateTime;

public class TimeInterval {
    private LocalDateTime from;
    private LocalDateTime to;

    public TimeInterval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public Boolean contains(LocalDateTime time) {
        return from.isEqual(time) || to.isEqual(time) || from.isBefore(time) && to.isAfter(time);
    }

    public Boolean overlaps(TimeInterval time) {
        return contains(time.getFrom()) || contains(time.getTo()) || from.isAfter(time.getFrom()) && to.isBefore(time.getTo());
    }
}
