package me.ericaustin.CoordinatesPlus;

import me.ericaustin.CoordinatesPlus.deathnote.DeathNote;
import me.ericaustin.CoordinatesPlus.events.PlayerEvents;
import me.ericaustin.CoordinatesPlus.scoreboard.CoordinatesScoreBoard;
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
}
