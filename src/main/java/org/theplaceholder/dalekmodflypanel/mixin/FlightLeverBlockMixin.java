package org.theplaceholder.dalekmodflypanel.mixin;

import com.swdteam.common.block.tardis.FlightLeverBlock;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

@Mixin(value = FlightLeverBlock.class, remap = false)
public abstract class FlightLeverBlockMixin {
    @Shadow public abstract void switchLever(BlockState state, World worldIn, BlockPos pos);

    @Inject(method = "func_225533_a_", at = @At("HEAD"))
    private void use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult blockRayTraceResult, CallbackInfoReturnable<ActionResultType> cir) {
        if (handIn == Hand.MAIN_HAND && !worldIn.isClientSide) {
            if (worldIn.dimension().equals(DMDimensions.TARDIS)) {
                ITardisData data = (ITardisData) DMTardis.getTardisFromInteriorPos(pos);
                this.switchLever(state, worldIn, pos);
                if (data.dalekmodflypanel$isInFlightMode())
                    cir.setReturnValue(ActionResultType.SUCCESS);
            }
        }
    }
}
