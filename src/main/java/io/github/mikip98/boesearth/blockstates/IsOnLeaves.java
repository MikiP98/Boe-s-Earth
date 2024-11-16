package io.github.mikip98.boesearth.blockstates;

import io.github.mikip98.boesearth.blockstates.abstacts.CustomBooleanProperty;
import net.minecraft.state.property.BooleanProperty;

public class IsOnLeaves extends CustomBooleanProperty {
    public static final String name = "is_on_leaves";
    public static final BooleanProperty IS_ON_LEAVES = BooleanProperty.of(name);
}
