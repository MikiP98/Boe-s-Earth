package io.github.mikip98.boesearth.config;

public class ModConfig extends DefaultConfig {
    // ------ BLOCKSTATE TOGGLES ------
    public static boolean snowOnLeavesBlockstate = defaultSnowOnLeavesBlockstate;
    public static boolean vinesOnLeavesBlockstate = defaultVinesOnLeavesBlockstate;
    public static boolean carpetOnLeavesBlockstate = defaultCarpetOnLeavesBlockstate;
    public static boolean leavesWithSnowOnTopBlockstate = defaultLeavesWithSnowOnTopBlockstate;
    // This is a cache variable
    public static boolean isOnLeavesBlockstate = snowOnLeavesBlockstate && carpetOnLeavesBlockstate;


    // ------ BLOCKSTATE UPDATES ------
    // Random tick updates
    public static boolean doRandomTickSnowUpdates = defaultDoRandomTickSnowUpdates;
    public static boolean doRandomTickVineUpdates = defaultDoRandomTickVineUpdates;
    public static boolean doRandomTickLeavesUpdates = defaultDoRandomTickLeavesUpdates;

    // Neighbor change updates
    public static boolean updateSnowOnNeighborChange = defaultUpdateSnowOnNeighborChange;
    public static boolean updateVineOnNeighborChange = defaultUpdateVineOnNeighborChange;
    public static boolean updateLeavesOnNeighborChange = defaultUpdateLeavesOnNeighborChange;
    public static boolean updateLeavesOnNeighborChangeOnlyAbove = defaultUpdateLeavesOnNeighborChangeOnlyAbove;
    public static int maxSnowUpdateChain = defaultMaxSnowUpdateChain;
    public static int maxVineUpdateChain = defaultMaxVineUpdateChain;
    public static int maxLeavesUpdateChain = defaultMaxLeavesUpdateChain;


    // ------ OTHER ------
    public static VinePriority vinePriority = defaultVinePriority;
    // This is a cache variable
    public static boolean VinePrioritiseLeaves = vinePriority == VinePriority.LEAVES;
}


// --------- DEFAULT CONFIG ---------
class DefaultConfig {
    // ------ BLOCKSTATE TOGGLES ------
    public static final boolean defaultSnowOnLeavesBlockstate = true;
    public static final boolean defaultVinesOnLeavesBlockstate = true;
    public static final boolean defaultCarpetOnLeavesBlockstate = true;
    public static final boolean defaultLeavesWithSnowOnTopBlockstate = true;


    // ------ BLOCKSTATE UPDATES ------
    // Random tick updates
    public static final boolean defaultDoRandomTickSnowUpdates = true;
    public static final boolean defaultDoRandomTickVineUpdates = true;
    public static final boolean defaultDoRandomTickLeavesUpdates = true;

    // Neighbor change updates
    public static boolean defaultUpdateSnowOnNeighborChange = true;
    public static boolean defaultUpdateVineOnNeighborChange = true;
    public static boolean defaultUpdateLeavesOnNeighborChange = true;
    public static boolean defaultUpdateLeavesOnNeighborChangeOnlyAbove = false;
    public static int defaultMaxSnowUpdateChain = 24;
    public static int defaultMaxVineUpdateChain = 32;
    public static int defaultMaxLeavesUpdateChain = 16;


    // ------ OTHER ------
    public static final VinePriority defaultVinePriority = VinePriority.BLOCKS;
}
