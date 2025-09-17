package net.starmarine.ddblockstest.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.starmarine.ddblockstest.core.ModRegistry;
import net.starmarine.ddblockstest.block.entity.DyeableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.item.component.BlockEntityData;

public class ClientSetup {

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof DyeableBlockEntity dyeableBlockEntity) {
                    return dyeableBlockEntity.getColor();
                }
            }
            return 0xFFFFFF; // Default white
        }, ModRegistry.DYEABLE_BLOCK.get());
    }
    public static void registerColorHandlers() {
        // --- BLOCK COLORS ---
        // This registers the color handler for the block when it's placed in the world.
        Minecraft.getInstance().getBlockColors().register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                // Get the color from our Block Entity
                if (level.getBlockEntity(pos) instanceof DyeableBlockEntity blockEntity) {
                    return blockEntity.getColor();
                }
            }
            return 0xFFFFFF; // Default to white if something goes wrong
        }, ModRegistry.DYEABLE_BLOCK.get());

        // --- ITEM COLORS ---
        // This registers the color handler for the block when it's an item in an inventory.
//        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
//            // Get the BlockEntityData component from the item stack
//            Optional<BlockEntityData> blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
//            if (blockEntityData.isPresent()) {
//                // Get the NBT tag from the component and read the "color" value
//                return blockEntityData.get().getTag().getInt("color");
//            }
//            return 0xFFFFFF; // Default to white
//        }, ModRegistry.DYEABLE_BLOCK_ITEM.get());
    }
}


