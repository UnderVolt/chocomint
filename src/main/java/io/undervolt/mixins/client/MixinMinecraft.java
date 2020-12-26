package io.undervolt.mixins.client;

import io.undervolt.api.event.events.GameShutdownEvent;
import io.undervolt.api.event.events.TickEvent;
import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import io.undervolt.instance.LaunchType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public GameSettings gameSettings;

    @Inject(method = "startGame", at = @At("HEAD"), remap = false)
    private void preinit(CallbackInfo info){
        GameBridge.chocomint = new Chocomint(GameBridge.getMinecraft());
    }

    @Inject(method = "startGame", at = @At("RETURN"), remap = false)
    private void init(CallbackInfo info){
        GameBridge.chocomint.init(LaunchType.PREINIT);
        GameBridge.chocomint.init(LaunchType.INIT);
        GameBridge.chocomint.init(LaunchType.POSTINIT);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void exit(CallbackInfo info){
        GameBridge.getChocomint().getEventManager().callEvent(new GameShutdownEvent());
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void update(CallbackInfo info){
        GameBridge.getChocomint().getEventManager().callEvent(new TickEvent.ClientTickEvent());
    }

    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;skipRenderWorld:Z", shift = At.Shift.AFTER))
    private void render(CallbackInfo info){
        GameBridge.getChocomint().getEventManager().callEvent(new TickEvent.RenderTickEvent());
    }

    public int getLimitFramerate() {
        return 9999;
    }
    
}
