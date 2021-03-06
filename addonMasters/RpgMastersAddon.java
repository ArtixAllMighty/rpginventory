package addonMasters;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import rpgInventory.RpgInventoryMod;
import rpgInventory.RpgInventoryMod.ITEMTYPE;
import rpgInventory.gui.rpginv.PacketNotify;
import rpgInventory.gui.rpginv.PacketNotify.HandlerPacketNotify;
import rpgInventory.utils.RpgUtility;
import addonMasters.entity.EntityPetXP;
import addonMasters.entity.EntityTeleportStone;
import addonMasters.entity.IPet;
import addonMasters.entity.IPet.PetID;
import addonMasters.entity.pet.BoarPet;
import addonMasters.entity.pet.BullPet;
import addonMasters.entity.pet.ChickenPet;
import addonMasters.entity.pet.SpiderPet;
import addonMasters.items.ItemBeastAxe;
import addonMasters.items.ItemBeastMasterArmor;
import addonMasters.items.ItemCandy;
import addonMasters.items.ItemCrystal;
import addonMasters.items.ItemPetWhistle;
import addonMasters.items.ItemRBMats;
import addonMasters.items.ItemRogueArmor;
import addonMasters.items.ItemRpgInvArmorRB;
import addonMasters.items.PetExpPotion;
import addonMasters.packets.PacketCrystal;
import addonMasters.packets.PacketCrystal.HandlerPacketCrystal;
import addonMasters.packets.PacketName;
import addonMasters.packets.PacketName.HandlerPacketName;
import addonMasters.packets.PacketPetGui;
import addonMasters.packets.PacketPetGui.HandlerPacketPetGui;
import addonMasters.packets.PacketStorePet;
import addonMasters.packets.PacketStorePet.HandlerPacketStorePet;
import addonMasters.packets.PacketTeleport;
import addonMasters.packets.PacketTeleport.HandlerPacketTeleport;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "RPGRB", name = "Rogue Beastmaster Addon", version = "RpgInv8.4", dependencies = "required-after:rpginventorymod")
public class RpgMastersAddon {

	@SidedProxy(serverSide = "addonMasters.RBCommonProxy", clientSide = "addonMasters.RBClientProxy")
	public static RBCommonProxy proxy;
	public static CreativeTabs tab;

	public static String CLASSBEASTMASTER = "beastMaster";
	public static String CLASSBEASTMASTERSHIELDED = "shieldedBeastMaster";

	public static String CLASSROGUE = "rogue";
	public static String CLASSROGUESHIELDED = "Ninja";

	private String[][] recipePatterns;
	private Object[][] recipeItems;

	public static Item allItems[];
	public static Item beastShield, daggers, beastAxe, beastHood, beastChest,
	beastLegs, beastBoots, rogueHood, rogueChest, rogueLegs,
	rogueBoots, rogueLeather, beastLeather, crystal, whistle, petCandy,
	tangledBrench, PetXPBottle;

	public final static ArmorMaterial rogueArmor = EnumHelper.addArmorMaterial("rogue", 40, new int[] { 3, 5, 4, 3 }, 5);
	public final static ArmorMaterial beastMaster = EnumHelper.addArmorMaterial("beastmaster", 40, new int[] { 4, 5, 4, 3 }, 5);

	ToolMaterial BeastAxeMaterial = EnumHelper.addToolMaterial("BeastAxe", 4,1280, 6.0F, 3, 22);

	public static final SimpleNetworkWrapper SNW = new SimpleNetworkWrapper("R_BChannel");
	
	@EventHandler
	public void load(FMLInitializationEvent event) {


		RpgUtility.registerAbilityWeapon(daggers);

		FMLLog.info("Rpg++ Rogue and BeastMaster Installed. Renderers can be Used");

		GameRegistry.addRecipe(new ItemStack(daggers, 1), new Object[] { " ei",
			"eie", "se ", 'i', Items.iron_ingot, 'e', Items.spider_eye,
			's', Items.stick });
		GameRegistry.addShapelessRecipe(new ItemStack(whistle), new Object[] {
			Items.stick, Items.reeds, Items.reeds });
		GameRegistry.addRecipe(new ItemStack(beastLeather), new Object[] {
			"LLL", "LVL", "LLL", 'L', Blocks.leaves, 'V', Items.leather });
		GameRegistry.addRecipe(new ItemStack(rogueLeather), new Object[] {
			"DSD", "SLS", "DSD", 'S', Items.string, 'L', Items.leather,
			'D', new ItemStack(Items.dye, 1, 5) });
		GameRegistry.addRecipe(new ItemStack(beastShield), new Object[] {
			"III", "IDI", " I ", 'I', beastLeather, 'D', Blocks.log });
		GameRegistry.addRecipe(new ItemStack(beastAxe), new Object[] { " IW",
			" SI", "S  ", 'S', tangledBrench, 'I', Blocks.iron_block, 'W',
			Blocks.log });
		GameRegistry.addShapelessRecipe(new ItemStack(tangledBrench),
				new Object[] { Items.stick, Items.stick, Items.string,
			Items.string, Items.string, Items.string });

		recipePatterns = new String[][] { { "XXX", "X X" },
				{ "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" },
				{ "X X", "X X" } };
		recipeItems = new Object[][] { { rogueLeather, beastLeather },
				{ rogueHood, beastHood }, { rogueChest, beastChest },
				{ rogueLegs, beastLegs }, { rogueBoots, beastBoots } };

		for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
			Object var3 = this.recipeItems[0][var2];

			for (int var4 = 0; var4 < (this.recipeItems.length - 1); ++var4) {
				Item var5 = (Item) this.recipeItems[var4 + 1][var2];
				GameRegistry.addRecipe(new ItemStack(var5), new Object[] {
					this.recipePatterns[var4], 'X', var3 });
			}
		}
		RpgInventoryMod.instance.addChestLoot(new ItemStack(PetXPBottle), 1,
				1, 40, "Pet Drinks");
		RpgInventoryMod.instance.addCandyChestLoot(new ItemStack(petCandy), 1,
				6, 20, "Easter Egg");

		daggers.setCreativeTab(tab);
		beastAxe.setCreativeTab(tab);

		rogueLeather.setCreativeTab(tab);
		beastShield.setCreativeTab(tab);
		beastLeather.setCreativeTab(tab);

		rogueHood.setCreativeTab(tab);
		rogueChest.setCreativeTab(tab);
		rogueLegs.setCreativeTab(tab);
		rogueBoots.setCreativeTab(tab);

		beastHood.setCreativeTab(tab);
		beastChest.setCreativeTab(tab);
		beastLegs.setCreativeTab(tab);
		beastBoots.setCreativeTab(tab);

		petCandy.setCreativeTab(tab);
		tangledBrench.setCreativeTab(tab);
		PetXPBottle.setCreativeTab(tab);

		crystal.setCreativeTab(tab);

		whistle.setCreativeTab(tab);

		proxy.registerRendering();

		EntityRegistry.registerGlobalEntityID(BullPet.class, "BullPet",EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(SpiderPet.class, "SpiderPet",EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(BoarPet.class, "BoarPet",EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(ChickenPet.class, "RoosterPet",EntityRegistry.findGlobalUniqueEntityId());

		EntityRegistry.registerModEntity(EntityPetXP.class, "PetXP",RpgInventoryMod.instance.getUniqueID(), this, 80, 1, true);
		EntityRegistry.registerModEntity(EntityTeleportStone.class,"TelePortStone", RpgInventoryMod.instance.getUniqueID(), this,80, 1, true);

		MinecraftForge.EVENT_BUS.register(new BeastMasterEvent());

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		SNW.registerMessage(HandlerPacketCrystal.class, PacketCrystal.class, 1, Side.SERVER);
		SNW.registerMessage(HandlerPacketName.class, PacketName.class, 2, Side.SERVER);
		SNW.registerMessage(HandlerPacketPetGui.class, PacketPetGui.class, 3, Side.SERVER);
		SNW.registerMessage(HandlerPacketTeleport.class, PacketTeleport.class, 4, Side.SERVER);
		SNW.registerMessage(HandlerPacketStorePet.class, PacketStorePet.class, 5, Side.SERVER);
		SNW.registerMessage(HandlerPacketNotify.class, PacketNotify.class, 6, Side.SERVER);

		tab = new RBTab(CreativeTabs.getNextID(), "Rogue Beastmaster Addon");

		daggers = new ItemRpgInvArmorRB(1, 800, "","subaraki:jewels/DaggerShield.png").setUnlocalizedName("dagger");
		beastAxe = new ItemBeastAxe(BeastAxeMaterial).setFull3D().setUnlocalizedName("forestAxe");

		rogueLeather = new ItemRBMats().setUnlocalizedName("r.leather");
		beastLeather = new ItemRBMats().setUnlocalizedName("b.leather");

		beastShield = new ItemRpgInvArmorRB(1, 250, "","subaraki:jewels/lion.png").setUnlocalizedName("shieldBeastMaster");

		rogueHood = new ItemRogueArmor(rogueArmor, 4, 0).setUnlocalizedName("rogue1");
		rogueChest = new ItemRogueArmor(rogueArmor, 4, 1).setUnlocalizedName("rogue2");
		rogueLegs = new ItemRogueArmor(rogueArmor, 4, 2).setUnlocalizedName("rogue3");
		rogueBoots = new ItemRogueArmor(rogueArmor, 4, 3).setUnlocalizedName("rogue4");

		beastHood = new ItemBeastMasterArmor(beastMaster, 4, 0).setUnlocalizedName("beast1");
		beastChest = new ItemBeastMasterArmor(beastMaster, 4, 1).setUnlocalizedName("beast2");
		beastLegs = new ItemBeastMasterArmor(beastMaster, 4, 2).setUnlocalizedName("beast3");
		beastBoots = new ItemBeastMasterArmor(beastMaster, 4, 3).setUnlocalizedName("beast4");

		whistle = new ItemPetWhistle().setUnlocalizedName("whistle");

		petCandy = new ItemCandy(0).setUnlocalizedName("petCandy");tangledBrench = new ItemCandy(0)
		.setUnlocalizedName("tangledBrench");
		PetXPBottle = new PetExpPotion().setUnlocalizedName("PetXPBottle");

		crystal = new ItemCrystal(ITEMTYPE.CRYSTAL, -1, "").setUnlocalizedName("petCrystal");

		LanguageRegistry.addName(daggers, "Rogue Daggers");
		LanguageRegistry.addName(rogueLeather, "Rogue Leather");
		LanguageRegistry.addName(beastLeather, "BeastMaster Leather");
		LanguageRegistry.addName(rogueHood, "Rogue Hood");
		LanguageRegistry.addName(rogueChest, "Rogue Breast Plate");
		LanguageRegistry.addName(rogueLegs, "Rogue Chaps");
		LanguageRegistry.addName(rogueBoots, "Rogue Boots");
		LanguageRegistry.addName(beastHood, "BeastMaster Hood");
		LanguageRegistry.addName(beastChest, "BeastMaster Body Protection");
		LanguageRegistry.addName(beastLegs, "BeastMaster Leg Protection");
		LanguageRegistry.addName(beastBoots, "BeastMaster Shoes");
		LanguageRegistry.addName(whistle, "Pet Whistle");
		LanguageRegistry.addName(beastShield, "BeastMaster Shield");
		LanguageRegistry.addName(beastAxe, "BeastMaster Forest Axe");
		LanguageRegistry.addName(petCandy, "Rare Pet Candy");
		LanguageRegistry.addName(tangledBrench, "Tangled Brench");
		LanguageRegistry.addName(PetXPBottle, "Bottle 'O Pet");

		LanguageRegistry.addName(new ItemStack(crystal, 1, 0), "Pet Crystal");
		LanguageRegistry.addName(new ItemStack(crystal, 1, 1), "Boar");
		LanguageRegistry.addName(new ItemStack(crystal, 1, 2), "Spider");
		LanguageRegistry.addName(new ItemStack(crystal, 1, 3), "Bull");

		allItems = new Item[] { beastShield, daggers, beastAxe, beastHood,
				beastChest, beastLegs, beastBoots, rogueHood, rogueChest,
				rogueLegs, rogueBoots, rogueLeather, beastLeather, crystal,
				whistle, petCandy, tangledBrench, PetXPBottle };

		for (int i = 0; i < allItems.length; i++) {
			if (allItems[i] != null) {

				String itemName = allItems[i].getUnlocalizedName().substring(allItems[i].getUnlocalizedName().indexOf(".") + 1);

				String itemNameCropped = itemName.substring(itemName.indexOf(".") + 1);

				if ((allItems[i] == rogueLeather)
						|| (allItems[i] == beastLeather)){
					allItems[i].setTextureName("minecraft:" + itemNameCropped);
				} else {
					allItems[i].setTextureName(RpgInventoryMod.name + ":"+ itemNameCropped);
				}

				GameRegistry.registerItem(allItems[i],allItems[i].getUnlocalizedName(),RpgInventoryMod.name);
			} else {
				System.out.println("Item is null !" + i);
			}
		}
	}


	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent evt){
		if(IPet.playersWithActivePets.size() > 0)
			for(PetID pet : IPet.playersWithActivePets.values()){
				pet.retrievePet();
			}
	}
}
