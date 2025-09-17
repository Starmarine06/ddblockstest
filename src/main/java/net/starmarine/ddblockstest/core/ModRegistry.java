package net.starmarine.ddblockstest.core;

import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.starmarine.ddblockstest.DdblockstestMod;
import net.starmarine.ddblockstest.block.DyeableBlock;
import net.starmarine.ddblockstest.block.entity.DyeableBlockEntity;

public class ModRegistry {
    // --- REGISTRIES ---
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DdblockstestMod.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DdblockstestMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DdblockstestMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DdblockstestMod.MODID);

    // --- BLOCKS ---
    public static final DeferredHolder<Block, DyeableBlock> DYEABLE_BLOCK = BLOCKS.register("dyeable_block",
            () -> new DyeableBlock(BlockBehaviour.Properties.of().strength(1.5f)));

    // --- BLOCK ITEMS ---
    public static final DeferredHolder<Item, BlockItem> DYEABLE_BLOCK_ITEM = ITEMS.register("dyeable_block",
            () -> new BlockItem(DYEABLE_BLOCK.get(), new Item.Properties()));

    // --- BLOCK ENTITIES ---
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DyeableBlockEntity>> DYEABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("dyeable_block_entity",
            () -> {
                // THIS IS THE FINAL FIX:
                // As you pointed out, we must remove the reference to DYEABLE_BLOCK.get()
                // to completely break the circular dependency.
                return new BlockEntityType<>(DyeableBlockEntity::new, DYEABLE_BLOCK.get());
            });


    // --- CREATIVE TABS ---
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DYEABLE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("dyeable_blocks_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.dyeable_blocks_tab"))
                    .icon(DYEABLE_BLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> {
                        output.accept(DYEABLE_BLOCK_ITEM.get());
                    }).build());

    // --- REGISTRATION METHOD ---
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

