package me.ericaustin.CoordinatesPlus.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Coordinates {

    private final char upArrow = '\u2191';
    private final char downArrow = '\u2193';

    private final Location location;

    public Coordinates(Location location) {
        this.location = location;
    }

    public Yaw getYaw() {
        return Yaw.getFacingDirection(this.location);
    }

    private String xString(Yaw yaw) {
        String prefix = "";

        if (yaw == Yaw.WEST) {
            prefix = String.valueOf(this.downArrow);
        } else if (yaw == Yaw.EAST) {
            prefix = String.valueOf(this.upArrow);
        }

        return String.format(ChatColor.RED + "%s%.0f" + ChatColor.RESET, prefix, this.location.getX());
    }

    private String yString() {
        return String.format(ChatColor.GREEN + "%.0f" + ChatColor.RESET, this.location.getY());
    }

    private String zString(Yaw yaw) {
        String prefix = "";

        if (yaw == Yaw.NORTH) {
            prefix = String.valueOf(this.downArrow);
        } else if (yaw == Yaw.SOUTH) {
            prefix = String.valueOf(this.upArrow);
        }

        return String.format(ChatColor.BLUE + "%s%.0f" + ChatColor.RESET, prefix, this.location.getZ());
    }

    public String getPrettyString(boolean withDirection) {
        Yaw yaw = null;
        String prefix = "";

        if (withDirection) {
            yaw = this.getYaw();
            prefix = ChatColor.GRAY + yaw.toString() + ChatColor.RESET + " ";
        }

        return String.format("%s%s" + ChatColor.WHITE + "/%s" + ChatColor.WHITE + "/%s",
                prefix,
                this.xString(yaw),
                this.yString(),
                this.zString(yaw));
    }
}
