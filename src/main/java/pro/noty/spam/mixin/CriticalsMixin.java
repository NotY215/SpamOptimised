package pro.noty.spam.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pro.noty.spam.SpamOptimised;

@Mixin(ClientPlayerEntity.class)
public class CriticalsMixin {
    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        // If mod is enabled and we are attacking, spoof a small fall
        if (SpamOptimised.enabled && player.isAttacking()) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            // Send tiny movement packets to trigger crit logic on server
            player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false, player.horizontalCollision));
            player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, player.horizontalCollision));
        }
    }
}