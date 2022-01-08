package me.secretagent.lobbyparkour.storage;

import me.secretagent.lobbyparkour.course.attempt.CourseAttempt;
import me.secretagent.lobbyparkour.course.attempt.record.AttemptRecord;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Storage {

    void saveCourseAttempt(CourseAttempt courseAttempt);

    AttemptRecord getCourseAttempt(UUID uuid);

    default AttemptRecord getCourseAttempt(Player player) {
        return getCourseAttempt(player.getUniqueId());
    }

    List<AttemptRecord> getCourseAttempts();

}
