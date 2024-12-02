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

    public static boolean updateSnowOnNeighborChange = defaultUpdateSnowOnNeighborChange;
    public static boolean updateVineOnNeighborChange = defaultUpdateVineOnNeighborChange;
    public static boolean updateLeavesOnNeighborChange = defaultUpdateLeavesOnNeighborChange;
    public static boolean updateLeavesOnNeighborChangeOnlyAbove = defaultUpdateLeavesOnNeighborChangeOnlyAbove;
    public static int maxSnowUpdateChain = defaultMaxSnowUpdateChain;
    public static int maxVineUpdateChain = defaultMaxVineUpdateChain;
    public static int maxLeavesUpdateChain = defaultMaxLeavesUpdateChain;

    public static VinePriority vinePriority = defaultVinePriority;
    public static boolean VinePrioritiseLeaves = vinePriority == VinePriority.LEAVES;
}
