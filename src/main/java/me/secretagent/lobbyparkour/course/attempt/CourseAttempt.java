package me.secretagent.lobbyparkour.course.attempt;

import me.secretagent.lobbyparkour.course.Course;
import org.bson.Document;

import java.text.DecimalFormat;
import java.util.UUID;

public class CourseAttempt {

    private final UUID playerUUID;

    private final Course course;

    private final long startTime;
    private long endTime;

    private int checkpoint;

    public CourseAttempt(UUID playerUUID, Course course, long startTime) {
        this.playerUUID = playerUUID;
        this.course = course;
        this.startTime = startTime;
        this.checkpoint = 1;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Course getCourse() {
        return course;
    }

    public void addCheckpoint() {
        checkpoint++;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getLength() {
        return getEndTime() - getStartTime();
    }

    public String getLengthString() {
        double time = getLength() / 1000.0;
        return new DecimalFormat("0.000").format(time);
    }

}
