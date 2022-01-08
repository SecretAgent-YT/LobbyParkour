package me.secretagent.lobbyparkour.course.manager;

import me.secretagent.lobbyparkour.LobbyParkour;
import me.secretagent.lobbyparkour.course.Course;
import me.secretagent.lobbyparkour.course.attempt.CourseAttempt;
import me.secretagent.lobbyparkour.util.ChatUtil;
import me.secretagent.lobbyparkour.vector.Vector;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseManager implements Listener {

    private final List<Course> courses = new ArrayList<>();
    private final List<CourseAttempt> courseAttempts = new ArrayList<>();
    private final LobbyParkour plugin;

    public CourseManager(LobbyParkour plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerCourse(Course course) {
        courses.add(course);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Vector vector = Vector.valueOf(player.getLocation());
        if (courseAttempts.stream().noneMatch(courseAttempt -> courseAttempt.getPlayerUUID().equals(uuid))) {
            for (Course course : courses) {
                if (vector.equals(course.getCheckPoints().get(0))) {
                    player.sendMessage(ChatUtil.format(plugin.getConfig().getString("start-message"))
                            .replace("%coursename%", course.getName()));
                    courseAttempts.add(new CourseAttempt(uuid, course, System.currentTimeMillis()));
                }
            }
        } else if (courseAttempts.stream().anyMatch(courseAttempt -> courseAttempt.getPlayerUUID().equals(uuid))) {
            CourseAttempt attempt = courseAttempts
                    .stream()
                    .filter( courseAttempt -> courseAttempt.getPlayerUUID().equals(uuid))
                    .findFirst()
                    .get();
            Course course = attempt.getCourse();
            if (vector.equals(course.getCheckPoints().get(course.getCheckPoints().size() - 1)) && attempt.getCheckpoint() == course.getCheckPoints().size() - 1) {
                attempt.setEndTime(System.currentTimeMillis());
                player.sendMessage(ChatUtil.format(plugin.getConfig().getString("end-message"))
                        .replace("%time%", String.valueOf(attempt.getLengthString()))
                        .replace("%coursename%", course.getName()));
                plugin.getStorage().saveCourseAttempt(attempt);
                courseAttempts.remove(attempt);
            } else if (vector.equals(course.getCheckPoints().get(attempt.getCheckpoint()))) {
                player.sendMessage(ChatUtil.format(plugin.getConfig().getString("checkpoint-message"))
                        .replace("%checkpoint%", String.valueOf(attempt.getCheckpoint()))
                        .replace("%coursename%", course.getName()));
                getCourseAttempt(uuid).addCheckpoint();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (courseAttempts.stream().anyMatch(courseAttempt -> courseAttempt.getPlayerUUID().equals(event.getPlayer().getUniqueId()))) {
            courseAttempts.remove(courseAttempts.stream().filter(courseAttempt -> courseAttempt.getPlayerUUID().equals(event.getPlayer().getUniqueId())).findFirst().get());
        }
    }

    public CourseAttempt getCourseAttempt(UUID uuid) {
        return courseAttempts.stream().filter(courseAttempt -> courseAttempt.getPlayerUUID().equals(uuid)).findFirst().orElse(null);
    }

}
