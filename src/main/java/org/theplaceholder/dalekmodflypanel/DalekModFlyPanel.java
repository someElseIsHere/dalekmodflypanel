package org.theplaceholder.dalekmodflypanel;

import com.swdteam.common.init.DMTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.theplaceholder.dalekmodflypanel.block.FlightPanelBlock;
import org.theplaceholder.dalekmodflypanel.capability.ITardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisHandler;
import org.theplaceholder.dalekmodflypanel.capability.TardisStorage;
import org.theplaceholder.dalekmodflypanel.client.RenderPlayerTardis;
import org.theplaceholder.dalekmodflypanel.packet.SyncFlightPacket;

import java.util.Optional;

@Mod(DalekModFlyPanel.MODID)
public class DalekModFlyPanel {
    public static final String MODID = "dalekmodflypanel";
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "network"), () -> PROTOCOL_VERSION, s -> s.equals(PROTOCOL_VERSION), s -> s.equals(PROTOCOL_VERSION));
    public static final ResourceLocation TARDIS_CAP = new ResourceLocation(MODID, "tardis_cap");
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public DalekModFlyPanel() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        RegistryObject<Block> BLOCK = BLOCKS.register("tardis_flight_panel", () -> new FlightPanelBlock(Block.Properties.of(Material.STONE)));
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register("tardis_flight_panel", () -> new BlockItem(BLOCK.get(), new Item.Properties().tab(DMTabs.DM_TARDIS)));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(new TardisHandler());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new RenderPlayerTardis()));
    }

    public void setup(FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(ITardisCapability.class, new TardisStorage(), TardisCapability::new);

        int index = 0;
        NETWORK.registerMessage(index++, SyncFlightPacket.class, SyncFlightPacket::encode, SyncFlightPacket::decode, SyncFlightPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
