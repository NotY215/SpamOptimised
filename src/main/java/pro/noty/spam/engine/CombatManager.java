package pro.noty.spam.engine;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pro.noty.spam.SpamOptimised;
import pro.noty.spam.util.InventoryUtils;

public class CombatManager {
    private static int tickCounter = 0;

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!SpamOptimised.enabled || client.player == null || client.world == null) return;

            tickCounter++;
            // Calculate delay based on CPS
            if (tickCounter >= (20 / Math.max(1, SpamOptimised.cps))) {
                tickCounter = 0;
                executeCombatLogic(client);
            }
        });
    }

    private static void executeCombatLogic(MinecraftClient client) {
        // Mode: Bow Logic (doesn't require crosshair on entity)
        if (SpamOptimised.currentMode.equals("Bow")) {
            handleBow(client);
            return;
        }

        if (client.crosshairTarget instanceof EntityHitResult hit) {
            var target = hit.getEntity();

            switch (SpamOptimised.currentMode) {
                case "Sword", "Axe" -> performStandardAttack(client, target);
                case "Mace" -> handleMace(client, target);
                case "Crystal" -> handleCrystalPVP(client, hit);
            }
        }

        // Block-based PVP (Anchor/Crystal placement)
        if (client.crosshairTarget instanceof BlockHitResult blockHit) {
            if (SpamOptimised.currentMode.equals("Anchor")) {
                handleAnchorPVP(client, blockHit);
            }
        }
    }

    private static void performStandardAttack(MinecraftClient client, net.minecraft.entity.Entity target) {
        // The CriticalsMixin handles the "Power" - we just trigger the hit
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    private static void handleMace(MinecraftClient client, net.minecraft.entity.Entity target) {
        // Mace only "spams" when player is high enough for a smash
        if (client.player.fallDistance > 1.5f) {
            performStandardAttack(client, target);
        }
    }

    private static void handleCrystalPVP(MinecraftClient client, EntityHitResult hit) {
        // 1. If looking at Crystal, explode it
        if (hit.getEntity() instanceof EndCrystalEntity crystal) {
            client.interactionManager.attackEntity(client.player, crystal);
            client.player.swingHand(Hand.MAIN_HAND);
        } else {
            // 2. Place Obsidian then Crystal (Requires items in hotbar)
            int obsSlot = InventoryUtils.findItemInHotbar(Items.OBSIDIAN);
            int crySlot = InventoryUtils.findItemInHotbar(Items.END_CRYSTAL);

            if (obsSlot != -1 && crySlot != -1) {
                InventoryUtils.selectSlot(obsSlot);
                // Placement logic would go here (InteractBlock)
            }
        }
    }

    private static void handleAnchorPVP(MinecraftClient client, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        // 1. Place side block for resistance (Protection)
        int blockSlot = InventoryUtils.findItemInHotbar(Items.COBBLESTONE);
        if (blockSlot != -1) {
            InventoryUtils.selectSlot(blockSlot);
            // Logic to place block between player and Anchor
        }

        // 2. Place and Charge Anchor
        int anchorSlot = InventoryUtils.findItemInHotbar(Items.RESPAWN_ANCHOR);
        int glowSlot = InventoryUtils.findItemInHotbar(Items.GLOWSTONE);

        if (anchorSlot != -1 && glowSlot != -1) {
            InventoryUtils.selectSlot(anchorSlot);
            // Place -> Switch to Glowstone -> Interact -> Explode
        }
    }

    private static void handleBow(MinecraftClient client) {
        if (client.player.isUsingItem() && client.player.getActiveItem().isOf(Items.BOW)) {
            if (client.player.getItemUseTime() >= 20) { // Fully charged
                client.interactionManager.stopUsingItem(client.player);
            }
        }
    }
}