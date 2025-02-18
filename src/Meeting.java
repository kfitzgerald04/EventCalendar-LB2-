// Meeting.java
// Kendra Fitzgerald

import java.time.Duration;
import java.time.LocalDateTime;

public class Meeting extends Event implements Completable {
    private LocalDateTime endDateTime;
    private String location;
    private boolean complete;

    public Meeting(String name, LocalDateTime start, LocalDateTime end, String location) {
        super(name, start);
        this.endDateTime = end;
        this.location = location;
        this.complete = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void complete() {
        this.complete = true;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Duration getDuration() {
        return Duration.between(getDateTime(), endDateTime);
    }

    public String getLocation() {
        return location;
    }

    public void setEndDateTime(LocalDateTime end) {
        this.endDateTime = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}