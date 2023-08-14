package org.theplaceholder.dalekmodflypanel.block;

import com.swdteam.common.block.IBlockTooltip;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;
import org.theplaceholder.dalekmodflypanel.util.TardisFlightUtils;

public class FlightPanelBlock extends Block implements IWaterLoggable, IBlockTooltip {
    public FlightPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getName(BlockState blockState, BlockPos blockPos, Vector3d vector3d, PlayerEntity playerEntity) {
        return new TranslationTextComponent("block.dalekmodflypanel.panel");
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult){
        if (!world.isClientSide) {
            if (world.dimension() == DMDimensions.TARDIS) {
                TardisData data = DMTardis.getTardisFromInteriorPos(blockPos);
                if (data != null){
                    if (!((ITardisData) data).dalekmodflypanel$isInFlightMode())
                        TardisFlightUtils.startPlayerFlight((ServerPlayerEntity) playerEntity);
                }

            }else{
                playerEntity.displayClientMessage(new TranslationTextComponent("message.dalekmodflypanel.panel.not_in_tardis").withStyle(TextFormatting.RED), true);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return Block.box(0, 0, 0, 16, 1, 16);
    }
}
