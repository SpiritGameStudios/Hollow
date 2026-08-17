package dev.spiritstudios.hollow.world.level.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EchoingPotBlockEntity extends BlockEntity {
    public int activeTime = 0;
    public long wobbleStartedAtTick;
    public DecoratedPotBlockEntity.WobbleStyle lastWobbleStyle;

    public EchoingPotBlockEntity(BlockPos pos, BlockState state) {
        super(HollowBlockEntityTypes.ECHOING_POT, pos, state);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, EchoingPotBlockEntity blockEntity) {

    }

    public void use(Player player, InteractionHand hand) {
        wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
        player.level().playSound(null, worldPosition, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
        player.level().gameEvent(player, GameEvent.BLOCK_CHANGE, worldPosition);
    }

    public void wobble(DecoratedPotBlockEntity.WobbleStyle wobbleType) {
        if (this.level != null && !this.level.isClientSide())
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, wobbleType.ordinal());
    }

    @Override
    public boolean triggerEvent(int event, int data) {
        if (this.level == null || event != 1 || data < 0 || data >= DecoratedPotBlockEntity.WobbleStyle.values().length)
            return super.triggerEvent(event, data);

        this.wobbleStartedAtTick = this.level.getGameTime();
        this.lastWobbleStyle = DecoratedPotBlockEntity.WobbleStyle.values()[data];
        return true;
    }

    // region NBT
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    protected void saveAdditional(ValueOutput output){
        output.putInt("ActiveTime", activeTime);
        super.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        activeTime = input.getIntOr("ActiveTime", 0);
    }
    // endregion
}
