package rpgNecroPaladin;

import rpgInventory.mod_RpgInventory;
import rpgNecroPaladin.minions.EntityMinionS;
import rpgNecroPaladin.minions.EntityMinionZ;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "RPGPlusPlus", name = "Subarakis RPG++ mod", version = "RpgInv8.4", dependencies="required-after:rpginventorymod")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
clientPacketHandlerSpec =
@SidedPacketHandler(channels = {"RpgPlusPlus"}, packetHandler = RpgPlusPacketHandler.class),
serverPacketHandlerSpec =
@SidedPacketHandler(channels = {"RpgPlusPlus"}, packetHandler = RpgPlusPacketHandler.class))
public class mod_RpgPlus {



	@SidedProxy(serverSide = "rpgNecroPaladin.CommonProxyRpgplus", clientSide = "rpgNecroPaladin.ClientProxyRpgPlus")
	public static CommonProxyRpgplus proxy;

	@Init
	public void load(FMLInitializationEvent event) {


	}
	@PostInit
	public void post(FMLPostInitializationEvent evt){

		proxy.registerRenderInformation();
		
		EntityRegistry.registerGlobalEntityID(EntityMinionS.class, "skeletonMinion", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityMinionZ.class, "zombieMinion", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMinionS.class, "skeletonMinion", mod_RpgInventory.instance.getUniqueID(), this, 100, 2, true);
		EntityRegistry.registerModEntity(EntityMinionZ.class, "zombieMinion", mod_RpgInventory.instance.getUniqueID(), this, 100, 2, true);
		LanguageRegistry.instance().addStringLocalization("entity.EntityMinionS.name", "Skeleton Minion");
		LanguageRegistry.instance().addStringLocalization("entity.EntityMinionZ.name", "Zombie Minion");

		
		TickRegistry.registerTickHandler(new CommonTickHandlerRpgPlus(), Side.SERVER);
	}
}