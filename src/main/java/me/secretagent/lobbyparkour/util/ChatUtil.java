package me.secretagent.lobbyparkour.util;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
