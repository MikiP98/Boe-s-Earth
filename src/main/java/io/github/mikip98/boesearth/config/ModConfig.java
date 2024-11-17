package io.github.mikip98.boesearth.config;

public class ModConfig extends DefaultConfig {
    public static boolean snowOnLeavesBlockstate = defaultSnowOnLeavesBlockstate;
    public static boolean vinesOnLeavesBlockstate = defaultVinesOnLeavesBlockstate;
    public static boolean carpetOnLeavesBlockstate = defaultCarpetOnLeavesBlockstate;

    public static boolean correctSnowWithTime = defaultCorrectSnowWithTime;
    public static boolean correctVinesWithTime = defaultCorrectVinesWithTime;

    public static VinePriority vinePriority = defaultVinePriority;
    public static boolean VinePrioritiseLeaves = defaultVinePriority == VinePriority.LEAVES;
}
