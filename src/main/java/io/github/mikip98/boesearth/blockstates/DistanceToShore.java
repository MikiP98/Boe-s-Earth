package io.github.mikip98.boesearth.blockstates;

import net.minecraft.state.property.IntProperty;

public class DistanceToShore {
    public static final String name = "distance_to_shore";
    public static final IntProperty DISTANCE_TO_SHORE = IntProperty.of(name, 0, 15);
}
