package org.theplaceholder.dalekmodflypanel.block;

import com.swdteam.common.block.IBlockTooltip;
import com.swdteam.common.init.DMDimensions;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.theplaceholder.dalekmodflypanel.utils.TardisFlightUtils;

public class FlightPanelBlock extends Block implements IWaterLoggable, IBlockTooltip {

    public FlightPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getName(BlockState blockState, BlockPos blockPos, Vector3d vector3d, PlayerEntity playerEntity) {
        return new TranslationTextComponent("block.dalekmodflypanel.tardis_flight_panel");
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult){
        if (!world.isClientSide) {
            if (world.dimension() == DMDimensions.TARDIS) {
                TardisFlightUtils.startPlayerFlight(playerEntity);
            }else{
                playerEntity.displayClientMessage(new TranslationTextComponent("message.dalekmodflypanel.tardis_flight_panel.not_in_tardis").withStyle(TextFormatting.RED), true);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}