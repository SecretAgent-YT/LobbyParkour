package me.secretagent.lobbyparkour.vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;

public class Vector {

    private final int x;
    private final int y;
    private final int z;
    private final World world;

    public Vector(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public boolean equals(Vector vector) {
        if (vector.getX() != getX()) return false;
        if (vector.getY() != getY()) return false;
        if (vector.getZ() != getZ()) return false;
        if (!vector.getWorld().getName().equals(getWorld().getName())) return false;
        return true;
    }

    public static Vector valueOf(Location location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        World world = location.getWorld();
        return new Vector(x, y, z, world);
    }

    public static Vector valueOf(JSONObject jsonObject) {
        return new Vector(
                Integer.parseInt(jsonObject.get("x").toString()),
                Integer.parseInt(jsonObject.get("y").toString()),
                Integer.parseInt(jsonObject.get("z").toString()),
                Bukkit.getWorld(jsonObject.get("worldName").toString())
                );
    }

}
