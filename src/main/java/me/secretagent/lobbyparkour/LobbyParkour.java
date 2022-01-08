package me.secretagent.lobbyparkour;

import me.secretagent.lobbyparkour.course.Course;
import me.secretagent.lobbyparkour.course.manager.CourseManager;
import me.secretagent.lobbyparkour.listener.RegionChangeListener;
import me.secretagent.lobbyparkour.storage.Storage;
import me.secretagent.lobbyparkour.storage.mongo.MongoStorage;
import me.secretagent.lobbyparkour.vector.Vector;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class LobbyParkour extends JavaPlugin {

    public final File CHECKPOINTS = new File(getDataFolder(), "checkpoints.json");

    private Storage storage;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (!CHECKPOINTS.exists()) {
            try {
                CHECKPOINTS.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getServer().getPluginManager().registerEvents(new RegionChangeListener(this), this);
        CourseManager manager = new CourseManager(this);
        Course course = new Course(getCheckPoints(), "haha");
        manager.registerCourse(course);
        storage = new MongoStorage();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public List<Vector> getCheckPoints() {
        List<Vector> checkpoints = new ArrayList<>();
        String inline = "";
        try {
            Scanner scanner = new Scanner(CHECKPOINTS);
            while (scanner.hasNextLine()) {
                inline += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            JSONObject object = (JSONObject) new JSONParser().parse(inline);
            JSONArray array = (JSONArray) object.get("checkpointsData");
            for (Object obj : array) {
                if (obj instanceof JSONObject) {
                    Vector vector = Vector.valueOf((JSONObject) obj);
                    checkpoints.add(vector);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return checkpoints;
    }

    public MongoStorage getStorage() {
        return (MongoStorage) storage;
    }

}
