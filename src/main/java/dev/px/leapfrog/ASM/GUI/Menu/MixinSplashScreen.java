package dev.px.leapfrog.ASM.GUI.Menu;

import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(SplashProgress.class)
public class MixinSplashScreen {

    @Inject(method = "start", at = @At("HEAD"), cancellable = true, remap = false) // Fuck forge
    private static void start(CallbackInfo ci){
        try {
            Method method = SplashProgress.class.getDeclaredMethod("disableSplash");
            method.setAccessible(true);
            method.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {

        }

        ci.cancel();
    }

}
