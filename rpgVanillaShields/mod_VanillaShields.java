package rpgVanillaShields;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import rpgInventory.config.RpgConfig;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;



@Mod(modid = "VanillaShields", name = "Vanilla Shields Mod", version = "RpgInv8.4", dependencies="required-after:rpginventorymod")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class mod_VanillaShields {
    
	@SidedProxy(serverSide = "rpgVanillaShields.CommonProxy", clientSide = "rpgVanillaShields.ClientProxy")
	public static CommonProxy proxy;
	
	public static CreativeTabs tab;
	
	
	public static String WOODENSHIELD = "vanillaWood";
	public static String IRONSHIELD = "vanillaIron";
	public static String GOLDENSHIELD = "vanillaGolden";
	public static String DIAMONDSHIELD = "vanillaDiamond";

	public static Item
	shieldWood, shieldIron, shieldGold, shieldDiamond;

	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		FMLLog.info("Rpg++ Vanilla Shields is installed. Renderers can be Used");

		tab = new ShieldTab(CreativeTabs.getNextID(), "ShieldTab");
		
		shieldWood = new ItemRpgInvShields(RpgConfig.instance.shieldWoodID, 1, 50, "wood","subaraki:jewels/ShieldWood.png").setUnlocalizedName("shieldWood");
		shieldIron = new ItemRpgInvShields(RpgConfig.instance.shieldIronID, 1, 125, "iron","subaraki:jewels/ShieldIron.png").setUnlocalizedName("shieldIron");
		shieldGold = new ItemRpgInvShields(RpgConfig.instance.shieldGoldID, 1, 250, "gold","subaraki:jewels/ShieldGold.png").setUnlocalizedName("shieldGold");
		shieldDiamond = new ItemRpgInvShields(RpgConfig.instance.shieldDiamondID, 1, 500, "diamond","subaraki:jewels/ShieldDiamond.png").setUnlocalizedName("shieldDiamond");

		LanguageRegistry.addName(shieldWood, "Wooden Shield");
		LanguageRegistry.addName(shieldIron, "Iron Shield");
		LanguageRegistry.addName(shieldGold, "Golden Shield");
		LanguageRegistry.addName(shieldDiamond, "Diamond Shield");
		shieldWood.setCreativeTab(tab);
		shieldIron.setCreativeTab(tab);
		shieldGold.setCreativeTab(tab);
		shieldDiamond.setCreativeTab(tab);
		
		GameRegistry.addRecipe(new ItemStack(shieldWood, 1), new Object[]{"WWW", "WBW", " W ", 'W', Block.planks, 'B', Block.wood});
		GameRegistry.addRecipe(new ItemStack(shieldIron, 1), new Object[]{"WWW", "WWW", " W ", 'W', Item.ingotIron, });
		GameRegistry.addRecipe(new ItemStack(shieldGold, 1), new Object[]{"WWW", "WWW", " W ", 'W', Item.ingotGold});
		GameRegistry.addRecipe(new ItemStack(shieldDiamond, 1), new Object[]{"WWW", "WBW", " W ", 'W', Item.diamond, 'B', Block.blockDiamond});
		
		proxy.registerRenderers();
		
		MinecraftForge.EVENT_BUS.register(new VanillaEvents());

    }
}