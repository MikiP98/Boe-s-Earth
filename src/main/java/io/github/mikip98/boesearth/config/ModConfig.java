package io.github.mikip98.boesearth.config;

public class ModConfig extends DefaultConfig {
    public static boolean snowOnLeavesBlockstate = defaultSnowOnLeavesBlockstate;
    public static boolean vinesOnLeavesBlockstate = defaultVinesOnLeavesBlockstate;
    public static boolean carpetOnLeavesBlockstate = defaultCarpetOnLeavesBlockstate;
    public static boolean leavesWithSnowOnTopBlockstate = defaultLeavesWithSnowOnTopBlockstate;
    public static boolean isOnLeavesBlockstate = snowOnLeavesBlockstate && carpetOnLeavesBlockstate;

    public static boolean doRandomTickSnowUpdates = defaultDoRandomTickSnowUpdates;
    public static boolean doRandomTickVineUpdates = defaultDoRandomTickVineUpdates;
    public static boolean doRandomTickLeavesUpdates = defaultDoRandomTickLeavesUpdates;

    public static VinePriority vinePriority = defaultVinePriority;
    public static boolean VinePrioritiseLeaves = vinePriority == VinePriority.LEAVES;
}
