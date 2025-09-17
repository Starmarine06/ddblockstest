package net.starmarine.ddblockstest.block.entity;

import net.minecraft.core.HolderLookup;
import net.starmarine.ddblockstest.core.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DyeableBlockEntity extends BlockEntity {
    private int color = 0xFFFFFF; // Default to white

    public DyeableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModRegistry.DYEABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void setColor(int color) {
        this.color = color;
        setChanged(); // Mark the block entity as dirty to save it
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getColor() {
        return this.color;
    }

    // Save the color to NBT with the new signature
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("color", this.color);
        super.saveAdditional(pTag, pRegistries);
    }

    // Load the color from NBT with the new signature
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        // It's good practice to check if the tag exists before trying to read it
        if (pTag.contains("color")) {
            this.color = pTag.getInt("color");
        }
    }

    // Create a packet to sync data to the client (this method is unchanged)
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Get the NBT data for the update packet with the new signature
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}