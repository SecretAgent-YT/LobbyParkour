package me.secretagent.lobbyparkour.course.attempt.record;

import org.bson.Document;

import java.text.DecimalFormat;
import java.util.UUID;

public class AttemptRecord implements Comparable<AttemptRecord> {

    private final UUID playerUUID;
    private final long startTime;
    private final long endTime;

    public AttemptRecord(UUID playerUUID, long startTime, long endTime) {
        this.playerUUID = playerUUID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public long getStartTime() {
        return startTime;
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

    public static AttemptRecord valueOf(Document document) {
        return new AttemptRecord(
                UUID.fromString(document.getString("_id")),
                document.getLong("startTime"),
                document.getLong("endTime")
        );
    }

    @Override
    public int compareTo(AttemptRecord record) {
        if (record.getLength() == getLength()) return 0;
        if (record.getLength() < getLength()) return 1;
        if (record.getLength() > getLength()) return -1;
        return 0;
    }

}
