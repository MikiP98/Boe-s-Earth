package io.github.mikip98.boesearth.blockstates;

import io.github.mikip98.boesearth.blockstates.abstacts.CustomBooleanProperty;
import net.minecraft.state.property.BooleanProperty;

public class SnowOnTop extends CustomBooleanProperty {
    public static final String name = "snow_on_top";
    public static final BooleanProperty SNOW_ON_TOP = BooleanProperty.of(name);
}
