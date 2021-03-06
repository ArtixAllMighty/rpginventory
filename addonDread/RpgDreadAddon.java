package addonDread;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import rpgInventory.RpgInventoryMod;
import rpgInventory.handlers.RPGEventHooks;
import rpgInventory.utils.RpgUtility;
import addonDread.items.ItemNecroArmor;
import addonDread.items.ItemNecroPaladinMats;
import addonDread.items.ItemNecroSkull;
import addonDread.items.ItemPaladinArmor;
import addonDread.items.ItemPaladinSword;
import addonDread.items.ItemRpgInvArmorPlus;
import addonDread.minions.EntityMinionS;
import addonDread.minions.EntityMinionZ;
import addonDread.packets.PacketNecroSpecial;
import addonDread.packets.PacketNecroSpecial.HandlerNecroSpecial;
import addonDread.packets.PacketPalaSpecial;
import addonDread.packets.PacketPalaSpecial.HandlerPacketPalaSpecial;
import addonDread.packets.PacketSpawnMinion;
import addonDread.packets.PacketSpawnMinion.HandlerPacketSpawnMinion;
import addonDread.richutils.potions.DecomposePotion;
import addonDread.richutils.potions.MasochismPotion;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "RPGPlusPlus", name = "Necro Paladin Addon", version = "RpgInv8.4", dependencies = "required-after:rpginventorymod")
public class RpgDreadAddon {

	@SidedProxy(serverSide = "addonDread.ServerProxy", clientSide = "addonDread.ClientProxy")
	public static ServerProxy proxy;

	public static String CLASSNECRO = "necro";
	public static String CLASSNECROSHIELD = "shieldedNecro";
	public static String CLASSPALADIN = "paladin";
	public static String CLASSPALADINSHIELD = "shieldedPaladin";

	public static Potion decomposePotion;
	public static Potion masochismPotion;

	private String[][] recipePatterns;
	private Object[][] recipeItems;

	public static final ArmorMaterial NECROARMOR = EnumHelper.addArmorMaterial("necromancer", 20, new int[] { 2, 5, 1, 1 }, 5);

	public static final ArmorMaterial PALADINARMOR = EnumHelper.addArmorMaterial("paladin", 20, new int[] { 4, 7, 2, 1 }, 5);

	public static final ToolMaterial NECRO = EnumHelper.addToolMaterial("souls",0, 1024, 5F, 1, 0);
	public static final ToolMaterial PALADIN = EnumHelper.addToolMaterial("steel",0, 1024, 5F, 0, 0);

	public static final SimpleNetworkWrapper SNW = NetworkRegistry.INSTANCE.newSimpleChannel("DreadPacket");

	public static PlusTab tab;

	public static Item allItems[];
	public static Item
	/* ====shields==== */
	paladinShield, necroShield,
	/* ====weapons==== */
	paladinSword, necroSkull,
	/* ====armor==== */
	necroHood, necroChestplate, necroLeggings, necroBoots, palaHelm, palaChest,
	palaLeggings, palaBoots,
	/* ====leathers/skins==== */
	necroleather, paladinSteel;

	@EventHandler
	public void load(FMLInitializationEvent event) {

		FMLLog.info("Rpg++ Necromancer and Paladin is installed. Renderers can be Used",1);

		GameRegistry.addRecipe(new ItemStack(necroleather, 1), new Object[] {
			"BWB", "WLW", "BWB", 'W', Items.spider_eye, 'B', Items.bone,
			'L', Items.leather });
		GameRegistry.addRecipe(new ItemStack(paladinSteel, 1),
				new Object[] { "GGG", "BIB", "GGG", 'I', Items.gold_ingot, 'B',
			(new ItemStack(Items.potionitem, 1, 0)), 'G',
			Items.iron_ingot });
		GameRegistry.addRecipe(new ItemStack(necroShield, 1), new Object[] {
			"WWW", "WBW", " W ", 'W', necroleather, 'B',
			new ItemStack(Items.skull, 1, 1) });
		GameRegistry.addRecipe(new ItemStack(paladinShield, 1), new Object[] {
			"WWW", "WBW", " W ", 'W', paladinSteel, 'B', Blocks.iron_block });
		GameRegistry.addRecipe(new ItemStack(necroSkull, 1), new Object[] {
			"WWW", "WBW", "WWW", 'W', Items.bone, 'B',
			new ItemStack(Items.skull, 1, 1) });
		GameRegistry.addRecipe(new ItemStack(paladinSword, 1), new Object[] {
			"S", "S", "G", 'S', Items.iron_ingot, 'G', paladinSteel });

		recipePatterns = new String[][] { { "XXX", "X X" },
				{ "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" },
				{ "X X", "X X" } };
		recipeItems = new Object[][] { { paladinSteel, necroleather },
				{ palaHelm, necroHood }, { palaChest, necroChestplate },
				{ palaLeggings, necroLeggings }, { palaBoots, necroBoots } };

		for (int var2 = 0; var2 < recipeItems[0].length; ++var2) {
			Object var3 = recipeItems[0][var2];

			for (int var4 = 0; var4 < (this.recipeItems.length - 1); ++var4) {
				Item var5 = (Item) this.recipeItems[var4 + 1][var2];
				GameRegistry.addRecipe(new ItemStack(var5), new Object[] {
					this.recipePatterns[var4], 'X', var3 });
			}
		}

		necroHood.setCreativeTab(tab);
		necroChestplate.setCreativeTab(tab);
		necroLeggings.setCreativeTab(tab);
		necroBoots.setCreativeTab(tab);

		palaHelm.setCreativeTab(tab);
		palaChest.setCreativeTab(tab);
		palaLeggings.setCreativeTab(tab);
		palaBoots.setCreativeTab(tab);

		necroShield.setCreativeTab(tab);
		necroSkull.setCreativeTab(tab);
		paladinShield.setCreativeTab(tab);
		paladinSword.setCreativeTab(tab);
		necroleather.setCreativeTab(tab);
		paladinSteel.setCreativeTab(tab);

		FMLCommonHandler.instance().bus().register(new CommonTickHandlerRpgPlus());
		MinecraftForge.EVENT_BUS.register(new DreadEventHooks());
		// hack to increase the number of potion types allowed

		if (Potion.potionTypes.length < 256) {
			boolean found = false;
			Field fallbackfield = null;
			Potion[] potionTypes = null;
			for (Field f : Potion.class.getDeclaredFields()) {
				try {
					if ((fallbackfield != null)
							&& (f.getType() == Potion[].class)) {
						fallbackfield = f;
					}
					if (f.getName().equals("potionTypes")
							|| f.getName().equals("a")
							|| f.getName().equals("field_76425_a")) {
						found = true;
						Field modfield = Field.class
								.getDeclaredField("modifiers");
						modfield.setAccessible(true);
						modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

						potionTypes = (Potion[]) f.get(null);
						final Potion[] newPotionTypes = new Potion[256];
						System.arraycopy(potionTypes, 0, newPotionTypes, 0,
								potionTypes.length);
						f.set(null, newPotionTypes);
						break;
					}
				} catch (Exception e) {
					System.err.println("Severe error, please report this to the mod author:");
					System.err.println(e);
				}
			}
			try {
				if ((fallbackfield != null) && !found) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(fallbackfield, fallbackfield.getModifiers()
							& ~Modifier.FINAL);

					potionTypes = (Potion[]) fallbackfield.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0,
							potionTypes.length);
					fallbackfield.set(null, newPotionTypes);
				}
			} catch (Exception ex) {
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(ex);
			}
		}

		for (int pos = 32; pos < Potion.potionTypes.length; pos++) {
			if (Potion.potionTypes[pos] == null) {
				if (decomposePotion == null) {
					decomposePotion = new DecomposePotion(pos);
					Potion.potionTypes[pos] = decomposePotion;
				} else if (masochismPotion == null) {
					masochismPotion = new MasochismPotion(pos);
					Potion.potionTypes[pos] = masochismPotion;
				} else {
					break;
				}
			}

			RPGEventHooks.negativeEffects.add(2);
			RPGEventHooks.negativeEffects.add(4);
			RPGEventHooks.negativeEffects.add(9);
			RPGEventHooks.negativeEffects.add(15);
			RPGEventHooks.negativeEffects.add(17);
			RPGEventHooks.negativeEffects.add(18);
			RPGEventHooks.negativeEffects.add(19);
			RPGEventHooks.negativeEffects.add(20);
			RPGEventHooks.negativeEffects.add(decomposePotion.id);
		}

		RpgUtility.registerAbilityWeapon(necroSkull);
		RpgUtility.registerAbilityWeapon(paladinSword);


	}

	@EventHandler
	public void post(FMLPostInitializationEvent evt) {

		proxy.registerRenderInformation();

		EntityRegistry.registerGlobalEntityID(EntityMinionS.class,
				"skeletonMinion", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityMinionZ.class,
				"zombieMinion", EntityRegistry.findGlobalUniqueEntityId());
		LanguageRegistry.instance().addStringLocalization(
				"entity.EntityMinionS.name", "Skeleton Minion");
		LanguageRegistry.instance().addStringLocalization(
				"entity.EntityMinionZ.name", "Zombie Minion");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {

		SNW.registerMessage(HandlerNecroSpecial.class, PacketNecroSpecial.class, 0, Side.SERVER);
		SNW.registerMessage(HandlerPacketPalaSpecial.class, PacketPalaSpecial.class, 1, Side.SERVER);
		SNW.registerMessage(HandlerPacketSpawnMinion.class, PacketSpawnMinion.class, 2, Side.SERVER);

		tab = new PlusTab(CreativeTabs.getNextID(), "Necromancer Paladin Addon");

		necroHood = new ItemNecroArmor(NECROARMOR, 4, 0).setUnlocalizedName("necro1");
		necroChestplate = new ItemNecroArmor(NECROARMOR, 4, 1).setUnlocalizedName("necro2");
		necroLeggings = new ItemNecroArmor(NECROARMOR, 4, 2).setUnlocalizedName("necro3");
		necroBoots = new ItemNecroArmor(NECROARMOR, 4, 3).setUnlocalizedName("necro4");

		palaHelm = new ItemPaladinArmor(PALADINARMOR, 4, 0).setUnlocalizedName("paladin1");
		palaChest = new ItemPaladinArmor(PALADINARMOR, 4, 1).setUnlocalizedName("paladin2");
		palaLeggings = new ItemPaladinArmor(PALADINARMOR, 4, 2).setUnlocalizedName("paladin3");
		palaBoots = new ItemPaladinArmor(PALADINARMOR, 4, 3).setUnlocalizedName("paladin4");

		necroShield = new ItemRpgInvArmorPlus(1, 250, "necro","subaraki:jewels/NecroShield.png").setUnlocalizedName("shieldNecro");
		necroSkull = new ItemNecroSkull(NECRO).setFull3D().setUnlocalizedName("Skull");
		paladinShield = new ItemRpgInvArmorPlus(1, 450, "pala","subaraki:jewels/PaladinShield.png").setUnlocalizedName("shieldPaladin");
		paladinSword = new ItemPaladinSword(0,PALADIN).setFull3D().setUnlocalizedName("paladinPride");

		necroleather = new ItemNecroPaladinMats(0).setUnlocalizedName("n.leather");
		paladinSteel = new ItemNecroPaladinMats(0).setUnlocalizedName("p.iron_ingot");

		allItems = new Item[] { paladinShield, necroShield, paladinSword,
				necroSkull, necroHood, necroChestplate, necroLeggings,
				necroBoots, palaHelm, palaChest, palaLeggings, palaBoots,
				necroleather, paladinSteel };

		for (int i = 0; i < allItems.length; i++) {
			if (allItems[i] != null) {

				String itemName = allItems[i].getUnlocalizedName().substring(
						allItems[i].getUnlocalizedName().indexOf(".") + 1);

				String itemNameCropped = itemName.substring(itemName
						.indexOf(".") + 1);

				if ((allItems[i] == necroleather) || (allItems[i] == paladinSteel)) {
					allItems[i].setTextureName("minecraft:" + itemNameCropped);
				} else {
					allItems[i].setTextureName(RpgInventoryMod.name + ":"
							+ itemNameCropped);
				}

				GameRegistry.registerItem(allItems[i],allItems[i].getUnlocalizedName(),RpgInventoryMod.name);
			} else {
				System.out.println("Item is null !" + i);
			}
		}
	}

	public class PlusTab extends CreativeTabs {

		public PlusTab(int par1, String label) {
			super(par1, label);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return RpgDreadAddon.necroSkull;
		}

		@Override
		public String getTranslatedTabLabel() {
			return this.getTabLabel();
		}
	}
}
