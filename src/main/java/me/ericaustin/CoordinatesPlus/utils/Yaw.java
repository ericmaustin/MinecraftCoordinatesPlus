package me.ericaustin.CoordinatesPlus.utils;

import org.bukkit.Location;

public enum Yaw {
    NORTH, SOUTH, EAST, WEST;

    @Override
    public String toString() {
        switch(this) {
            case NORTH: return "N";
            case SOUTH: return "S";
            case EAST: return "E";
            case WEST: return "W";
            default: throw new IllegalArgumentException();
        }
    }

    public static Yaw getFacingDirection(Location location) {
        float yaw = location.getYaw();
        if (yaw >= -45 && yaw < 45) {
            return Yaw.SOUTH;
        }

        if (yaw >= 45 && yaw < 135) {
            return Yaw.WEST;
        }

        if (yaw >= -135 && yaw < 45) {
            return Yaw.EAST;
        }

        return Yaw.NORTH;
    }
}
