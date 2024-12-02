package io.github.mikip98.boesearth.config;

public class DefaultConfig {
    public static final boolean defaultSnowOnLeavesBlockstate = true;
    public static final boolean defaultVinesOnLeavesBlockstate = true;
    public static final boolean defaultCarpetOnLeavesBlockstate = true;
    public static final boolean defaultLeavesWithSnowOnTopBlockstate = true;

    public static final boolean defaultDoRandomTickSnowUpdates = true;
    public static final boolean defaultDoRandomTickVineUpdates = true;
    public static final boolean defaultDoRandomTickLeavesUpdates = true;

    public static boolean defaultUpdateSnowOnNeighborChange = true;
    public static boolean defaultUpdateVineOnNeighborChange = true;
    public static boolean defaultUpdateLeavesOnNeighborChange = true;
    public static boolean defaultUpdateLeavesOnNeighborChangeOnlyAbove = false;
    public static int defaultMaxSnowUpdateChain = 24;
    public static int defaultMaxVineUpdateChain = 32;
    public static int defaultMaxLeavesUpdateChain = 16;

    public static final VinePriority defaultVinePriority = VinePriority.BLOCKS;
}
