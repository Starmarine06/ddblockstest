package net.starmarine.ddblockstest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.starmarine.ddblockstest.block.entity.DyeableBlockEntity;

import javax.annotation.Nullable;

public class DyeableBlock extends BaseEntityBlock {

    public DyeableBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DyeableBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        // THIS IS THE FIX:
        // Instead of getting the BlockEntityType from the registry, we create
        // the BlockEntity directly. This breaks the dependency loop.
        return new DyeableBlockEntity(pPos, pState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide && pStack.getItem() instanceof DyeItem dye) {
            if (pLevel.getBlockEntity(pPos) instanceof DyeableBlockEntity blockEntity) {
                int color = dye.getDyeColor().getTextureDiffuseColor();
                blockEntity.setColor(color);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}