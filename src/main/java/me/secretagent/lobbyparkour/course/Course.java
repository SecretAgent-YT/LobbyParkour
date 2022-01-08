package me.secretagent.lobbyparkour.course;

import me.secretagent.lobbyparkour.LobbyParkour;
import me.secretagent.lobbyparkour.vector.Vector;
import org.bukkit.event.Listener;

import java.util.List;

public class Course implements Listener {

    private final List<Vector> checkPoints;
    private final String name;


    public Course(List<Vector> checkPoints, String name) {
        this.checkPoints = checkPoints;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Vector> getCheckPoints() {
        return checkPoints;
    }

}
