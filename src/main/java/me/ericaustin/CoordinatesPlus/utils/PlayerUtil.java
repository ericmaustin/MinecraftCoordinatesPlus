package me.ericaustin.CoordinatesPlus.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static String getPrettyName(Player p) {
        return ChatColor.GOLD + p.getDisplayName() + ChatColor.RESET;
    }
}
