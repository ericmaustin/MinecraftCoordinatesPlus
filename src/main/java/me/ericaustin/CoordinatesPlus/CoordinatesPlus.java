package me.ericaustin.CoordinatesPlus;

import me.ericaustin.CoordinatesPlus.deathnote.DeathNote;
import me.ericaustin.CoordinatesPlus.events.PlayerEvents;
import me.ericaustin.CoordinatesPlus.scoreboard.CoordinatesScoreBoard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoordinatesPlus extends JavaPlugin {

    public static CoordinatesScoreBoard scoreboard;
    public static DeathNote deathNote;

    @Override
    public void onEnable() {
        CoordinatesPlus.scoreboard = new CoordinatesScoreBoard();
        CoordinatesPlus.deathNote = new DeathNote();
        CoordinatesPlus.deathNote.loadDeaths();

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CoordinatesPlus.scoreboard = null;
        CoordinatesPlus.deathNote = null;
    }

    public static String prettyCoordinatesString(Location location) {
        return String.format("%s%.0f" + ChatColor.WHITE + "/%s%.0f" + ChatColor.WHITE + "/%s%.0f",
                ChatColor.RED, location.getX(),
                ChatColor.GREEN, location.getY(),
                ChatColor.BLUE, location.getZ());
    }

    public static String prettyPlayerName(String name) {
        return ChatColor.GOLD + name + ChatColor.RESET;
    }
}
