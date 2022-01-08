package me.secretagent.lobbyparkour.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboard;
import me.secretagent.lobbyparkour.LobbyParkour;
import me.secretagent.lobbyparkour.course.attempt.record.AttemptRecord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class RegionChangeListener implements Listener {

    private final LobbyParkour plugin;
    private final JPerPlayerScoreboard scoreboard;
    private final Map<UUID, Boolean> inScoreboard = new HashMap<>();

    public RegionChangeListener(LobbyParkour plugin) {
        this.plugin = plugin;
        this.scoreboard = new JPerPlayerScoreboard(
                (player) -> "&e&lPARKOUR",
                (player) -> Arrays.asList(
                        ChatColor.GREEN + "",
                        "&aBest Attempt: &e" + plugin.getStorage().getBestTime(player),
                        ChatColor.GREEN + "",
                        "&aLeaderboard:",
                        getLeaderboardString(0),
                        getLeaderboardString(1),
                        getLeaderboardString(2),
                        getLeaderboardString(3),
                        getLeaderboardString(4)
                )
        );
        Bukkit.getScheduler().runTaskTimer(plugin, this::onTick, 0, 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        checkPlayerInRegion(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkPlayerInRegion(event.getPlayer());
    }

    private void onTick() {
        scoreboard.updateScoreboard();
    }

    private void checkPlayerInRegion(Player player) {
        RegionManager manager = WorldGuard
                .getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(BukkitAdapter.adapt(player.getWorld()));
        UUID uuid = player.getUniqueId();
        if (!inScoreboard.containsKey(uuid) || !inScoreboard.get(uuid)) {
            if (manager.getRegion("parkour_region") != null && manager.getRegion("parkour_region")
                    .contains(BlockVector3.at(
                            player.getLocation().getX(),
                            player.getLocation().getY(),
                            player.getLocation().getZ()))) {
                scoreboard.addPlayer(player);
                inScoreboard.put(uuid, true);
            }
        } else if (inScoreboard.get(uuid)) {
            if (manager.getRegion("parkour_region") != null && !manager.getRegion("parkour_region")
                    .contains(BlockVector3.at(
                            player.getLocation().getX(),
                            player.getLocation().getY(),
                            player.getLocation().getZ()))) {
                scoreboard.removePlayer(player);
                inScoreboard.put(uuid, false);
            }
        }
    }

    private String getLeaderboardString(int i) {
        List<AttemptRecord> list = plugin.getStorage().getCourseAttempts();
        Collections.sort(list);
        if (i > list.size() - 1) {
            return "  &a#" + (i + 1) + " - None";
        } else {
            AttemptRecord record = list.get(i);
            return "  &a#" + (i + 1) + " - " + getName(record.getPlayerUUID()) + " - &e" + record.getLengthString();
        }
    }

    private String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

}
