package me.ericaustin.CoordinatesPlus.deathnote;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ericaustin.CoordinatesPlus.CoordinatesPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.*;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


class DeathBook {

    public Stack<String> pages;

    public DeathBook() {
        this.pages = new Stack<>();
    }

    public void addPage(String msg, Location location, int level) {

        // limit to max size
        while (this.pages.size() >= 50) {
            this.pages.remove(0);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("LLL d, yyyy K:mm:ssa");

        String page = ZonedDateTime.now().format(fmt) + "\n\n" +
                msg + "\n\n" +
                "Location of death:\n" +
                String.format("%sX: %s%.0f\n", ChatColor.RESET, ChatColor.DARK_RED, location.getX()) +
                String.format("%sY: %s%.0f\n", ChatColor.RESET, ChatColor.DARK_GREEN, location.getY()) +
                String.format("%sZ: %s%.0f\n\n", ChatColor.RESET, ChatColor.DARK_BLUE, location.getZ()) +
                String.format("%sYou were Level: %s%d", ChatColor.RESET, ChatColor.DARK_PURPLE, level);

        this.pages.push(page);
    }

    public ItemStack book() {
        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
        bookMeta.setTitle("Book Of Dying");
        bookMeta.setAuthor("DEATH");

        ListIterator<String> pagesIterator = this.pages.listIterator(this.pages.size());
        while (pagesIterator.hasPrevious()) {
            bookMeta.addPage(pagesIterator.previous());
        }

        writtenBook.setItemMeta(bookMeta);
        return writtenBook;
    }
}


public class DeathNote {
    static String fileName = "DeathBook.json";

    // todo: persist storage for this map
    private HashMap<UUID, DeathBook> deathMap;

    public DeathNote() {
        this.deathMap = new HashMap<>();
    }

    public void registerDeath(Player p, String deathMsg) {
        Location location = p.getLocation();

        String msg = CoordinatesPlus.prettyPlayerName(p.getDisplayName()) +
                        " DIED HERE: " + CoordinatesPlus.prettyCoordinatesString(location);

        Bukkit.broadcastMessage(msg);

        UUID uuid = p.getUniqueId();

        if (!this.deathMap.containsKey(uuid)) {
            this.deathMap.put(uuid, new DeathBook());
        }

        this.deathMap.get(uuid).addPage(deathMsg, p.getLocation(), p.getLevel());

        try {
            this.storeDeaths();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeDeaths() throws IOException {
        // gson the object
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(DeathNote.fileName);
        gson.toJson(this.deathMap, writer);
        writer.flush();
        writer.close();
    }

    public void loadDeaths() {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(DeathNote.fileName);
            this.deathMap = gson.fromJson(reader, new TypeToken<HashMap<UUID, DeathBook>>() {}.getType());
            if (this.deathMap == null) {
                // prevent deathMap from being set as null
                this.deathMap = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println(DeathNote.fileName + " not found... skipping load");
        }
    }

    public void giveDeathBook(Player p) {
        UUID uuid = p.getUniqueId();

        if (!this.deathMap.containsKey(uuid)) {
            // no death book needed
            return;
        }

        p.getInventory().setItemInMainHand(this.deathMap.get(uuid).book());
    }
}
