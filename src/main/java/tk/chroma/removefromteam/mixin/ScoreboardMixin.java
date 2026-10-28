package tk.chroma.removefromteam.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
    @Final
    @Shadow
    private static Logger LOGGER;

    @Unique private static boolean warned = false;

    @Inject(method = "removePlayerFromTeam(Ljava/lang/String;Lnet/minecraft/world/scores/PlayerTeam;)V", at = @At(
            value = "INVOKE",
            target = "java/lang/IllegalStateException.<init> (Ljava/lang/String;)V"
    ), cancellable = true)
    private void removeScoreHolderFromTeam(String string, PlayerTeam playerTeam, CallbackInfo ci) {
        if (!warned && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("[Removefromteam] \"Cannot remove from team\" warning thrown in logs (will not send this chat message again this session)"));
            warned = true;
        }
        LOGGER.warn("\"Cannot remove from team\" warning for player {} on team {}", string, playerTeam.getName());
        ci.cancel();
    }
}
