package org.theplaceholder.dalekmodflypanel;

import com.swdteam.common.init.DMTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
import org.theplaceholder.dalekmodflypanel.capability.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapabilityManager;
import org.theplaceholder.dalekmodflypanel.client.RenderPlayerTardis;
import org.theplaceholder.dalekmodflypanel.event.TardisHandler;

import java.util.Optional;

import static org.theplaceholder.dalekmodflypanel.DalekModFlyPanel.MODID;

@Mod(MODID)
public class DalekModFlyPanel {
    public static final String MODID = "dalekmodflypanel";
    //NETWORKING
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "network"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    //REGISTRIES
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public DalekModFlyPanel() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        //BLOCK/ITEM REGISTRATION
        RegistryObject<Block> BLOCK = BLOCKS.register("tardis_flight_panel", () -> new FlightPanelBlock(Block.Properties.of(Material.STONE)));
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register("tardis_flight_panel", () -> new BlockItem(BLOCK.get(), new Item.Properties().tab(DMTabs.DM_TARDIS)));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        //REGISTER CAPABILITY EVENT
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TardisCapabilityManager::attach);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TardisCapabilityManager::cloned);

        //REGISTER EVENT HANDLERS
        MinecraftForge.EVENT_BUS.register(new TardisHandler());
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> RenderPlayerTardis::new);
    }

    public void setup(FMLCommonSetupEvent event){
        //CAPABILITY REGISTRATION
        TardisCapabilityManager.register();

        //NETWORKING REGISTRATION
        int index = 0;
        CHANNEL.registerMessage(index++, SyncTardisPacket.class, SyncTardisPacket::encode, SyncTardisPacket::decode, SyncTardisPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
