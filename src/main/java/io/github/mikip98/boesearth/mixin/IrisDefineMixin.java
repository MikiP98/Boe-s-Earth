package io.github.mikip98.boesearth.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import net.irisshaders.iris.gl.shader.StandardMacros;
import net.irisshaders.iris.helpers.StringPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = StandardMacros.class, remap = false)
public abstract class IrisDefineMixin {
//    @Unique
//    private static void define(List<StringPair> defines, String key) {
//        defines.add(new StringPair(key, ""));
//    }

    @Unique
    private static void define(List<StringPair> defines, String key, String value) {
        defines.add(new StringPair(key, value));
    }


    @Inject(at = @At("RETURN"), method = "createStandardEnvironmentDefines", cancellable = true)
    private static void createStandardEnvironmentDefines(
            CallbackInfoReturnable<ImmutableList<StringPair>> cir,
            @Local() ArrayList<StringPair> standardDefines
    ) {
        define(standardDefines, "BOES_EARTH_BLOCKSTATES", "1");
        cir.setReturnValue(ImmutableList.copyOf(standardDefines));
        cir.cancel();
    }
}
