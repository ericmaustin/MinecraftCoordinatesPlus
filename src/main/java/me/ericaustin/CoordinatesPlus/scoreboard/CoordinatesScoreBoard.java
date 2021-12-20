package me.ericaustin.CoordinatesPlus.scoreboard;

import me.ericaustin.CoordinatesPlus.CoordinatesPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

class PlayerMap {
    public Player player;
    public Score  score;
    public String entity;

    public PlayerMap(Player p, Objective objective, Scoreboard scoreboard) {
        this.setPlayer(p, objective, scoreboard);
    }

    public void setPlayer(Player p, Objective objective, Scoreboard scoreboard) {

        if (this.player != null) {
            // remove the previous entity from the scoreboard
            scoreboard.resetScores(this.entity);
        } else {
            this.player = p;
        }

        // set the entity for this player
        this.entity = PlayerMap.entityString(p);
        // add objective entity
        this.score = objective.getScore(this.entity);

        int percent = Math.round(((float) p.getHealth() / (float) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) * 100.0F);

        // set the score using the players current level
        this.score.setScore(percent);
    }

    public static String entityString(Player p) {
        // create a string from the location
        return CoordinatesPlus.prettyPlayerName(p.getDisplayName()) + " " +
                CoordinatesPlus.prettyCoordinatesString(p.getLocation());
    }
}

public class CoordinatesScoreBoard {
    private HashMap<UUID, PlayerMap> playerMap;

    final private Scoreboard scoreBoard;
    private Objective objective;

    public CoordinatesScoreBoard() {
        // Plugin startup logic
        ScoreboardManager scoreBoardManager = Bukkit.getScoreboardManager();
        this.scoreBoard = scoreBoardManager.getNewScoreboard();
        this.objective = scoreBoard.registerNewObjective("coordinates", "dummy",ChatColor.DARK_AQUA + "coordinates");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.playerMap = new HashMap<>();
    }

    public void updatePlayer(Player p) {
        UUID uuid = p.getUniqueId();
        PlayerMap pm = null;

        if (!this.playerMap.containsKey(uuid)) {
            // player does not exist
            // create a new PlayerMap

            System.out.println("Adding player to coordinates scoreboard: " + p.getUniqueId() + " " + p.getDisplayName());

            pm = new PlayerMap(p, this.objective, this.scoreBoard);
            // put the player in the playerMap
            playerMap.put(p.getUniqueId(), pm);

            System.out.println("Added player to coordinates scoreboard: " + p.getUniqueId() + " " + p.getDisplayName());

            p.setScoreboard(this.scoreBoard);
        } else {
            // player exists, so update the PlayerMap with the player's unique id
            pm = this.playerMap.get(p.getUniqueId());
            pm.setPlayer(p, this.objective, this.scoreBoard);
        }
    }

    public void delPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        // player exists, remove the player's scoreboard entity and remove from the map
        if (this.playerMap.containsKey(uuid)) {
            this.scoreBoard.resetScores(this.playerMap.get(uuid).entity);
            this.playerMap.remove(uuid);

            System.out.println("Removed player from scoreboard: " + p.getUniqueId() + " " + p.getDisplayName());
        }
    }
}
