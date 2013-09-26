package rpgInventory.item.armor;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import rpgInventory.mod_RpgInventory;
import rpgInventory.gui.rpginv.PlayerRpgInventory;
import rpgInventory.models.shields.IronThorn;
import rpgInventory.models.shields.MainShield;
import rpgInventory.models.shields.ModelShield;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRpgInvArmor extends Item {

	/**
	 * Holds the 'base' maxDamage that each armorType have.
	 */
	private final int[] maxDamageArray = new int[]{30, 20, 50, 20, 30, 30};
	/**
	 * Stores the armor type: 0 is necklace, 2 is cloak, 1 is shield, 3 is
	 * gloves, 4 are rings
	 */
	public int armorType;

	@Override
	public void registerIcons(IconRegister par1IconRegister ) {
		String itemName = getUnlocalizedName().substring(getUnlocalizedName().lastIndexOf(".") + 1);
		this.itemIcon = par1IconRegister.registerIcon("rpginventorymod:" + itemName);
	}

	/**second string : name, isn't used. 
	 * @params
	 * ItemID
	 * ArmorType (aka shield,gloves, cloak, ... )
	 * the maximum damage this item can take. only used for shields
	 * name is unused
	 * location for texture*/
	public ItemRpgInvArmor(int par1, int par4, int maxDamage, String name, String resourcelocation) {
		super(par1);
		this.armorType = par4;
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxDamage(maxDamage);
		TEXTURE = new ResourceLocation(resourcelocation);
	}

	private IronThorn it = new IronThorn();
	private ModelShield arch = new ModelShield();

	public MainShield getShieldModel(){
		if(this.equals(mod_RpgInventory.berserkerShield))
			return it;
		if(this.equals(mod_RpgInventory.archerShield))
			return arch;
		return new MainShield();
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		if (par1ItemStack.itemID == mod_RpgInventory.cloakI.itemID) {
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if (par1ItemStack.itemID == mod_RpgInventory.cloakRed.itemID) {
			return 0xd2120e;
		}
		if (par1ItemStack.itemID == mod_RpgInventory.cloakGreen.itemID) {
			return 0x0fb15d;
		}
		if (par1ItemStack.itemID == mod_RpgInventory.cloakYellow.itemID) {
			return 0xf7cd09;
		}
		if (par1ItemStack.itemID == mod_RpgInventory.cloakSub.itemID) {
			return 0x440001;
		}
		if (par1ItemStack.itemID == mod_RpgInventory.cloakBlue.itemID) {
			return 0x291ef6;
		}

		return 0xffffff;
	}

	/**
	 * Returns the 'max damage' factor array for the armor, each piece of armor
	 * have a durability factor (that gets multiplied by armor material factor)
	 */
	int[] getMaxDamageArray() {
		return maxDamageArray;
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 */
	public void addInformation(ItemStack stack, EntityPlayer p1, List list, boolean yesno) {
		if (stack.itemID == mod_RpgInventory.ringem.itemID) {
			list.add(StatCollector.translateToLocal("Left: Dispell Negative Effects"));
			list.add(StatCollector.translateToLocal("Right: Increased Mining Speed x4"));
		}

		if (stack.itemID == mod_RpgInventory.neckem.itemID) {
			list.add(StatCollector.translateToLocal("Water Breathing"));
			list.add(StatCollector.translateToLocal("Holding breath under water lasts twice as long"));
		}

		if (stack.itemID == mod_RpgInventory.glovesem.itemID) {
			list.add(StatCollector.translateToLocal("Resistance"));
			list.add(StatCollector.translateToLocal("Reduces damage taken by 20%"));
		}

		if (stack.itemID == mod_RpgInventory.ringdia.itemID || stack.itemID == mod_RpgInventory.glovesdia.itemID
				|| stack.itemID == mod_RpgInventory.neckdia.itemID) {
			list.add(StatCollector.translateToLocal("Healing"));
			list.add(StatCollector.translateToLocal("+15% Heal Speed"));
		}

		if (stack.itemID == mod_RpgInventory.ringgold.itemID || stack.itemID == mod_RpgInventory.glovesbutter.itemID
				|| stack.itemID == mod_RpgInventory.neckgold.itemID) {
			list.add(StatCollector.translateToLocal("Speed + 12.5%"));
			if (stack.itemID == mod_RpgInventory.ringgold.itemID){
				list.add(StatCollector.translateToLocal("Jump +0.5 Block height"));
			}else{
				list.add(StatCollector.translateToLocal("Jump +0.25 Block height"));
			}
		}

		if (stack.itemID == mod_RpgInventory.ringlap.itemID || stack.itemID == mod_RpgInventory.gloveslap.itemID
				|| stack.itemID == mod_RpgInventory.necklap.itemID) {
			list.add(StatCollector.translateToLocal("Strength"));
			if (stack.itemID == mod_RpgInventory.ringlap.itemID){
				list.add(StatCollector.translateToLocal("+0.5"));
			}if (stack.itemID == mod_RpgInventory.gloveslap.itemID){
				list.add(StatCollector.translateToLocal("+1"));
			}if (stack.itemID == mod_RpgInventory.necklap.itemID){
				list.add(StatCollector.translateToLocal("+1.5"));
			}
			list.add(StatCollector.translateToLocal("-1 Damage on Held Weapon Every 20 Seconds"));
		}
	}

	private ResourceLocation TEXTURE;

	/**Gets the String initially set in the item constructor and turns it into a use-able resource-location*/
	public ResourceLocation getTexture(){
		return TEXTURE;
	}

	/**Used to upgrade the player's class to 'playerClass' + 'shieldedClass'
	 * Only works if the shield finds the matching class with boundArmorClass()*/
	public String shieldClass(){
		if(this.equals(mod_RpgInventory.berserkerShield))
			return mod_RpgInventory.CLASSBERSERKERSHIELD;
		if(this.equals(mod_RpgInventory.archerShield))
			return mod_RpgInventory.CLASSARCHERSHIELD;
		if(this.equals(mod_RpgInventory.talisman))
			return mod_RpgInventory.CLASSMAGESHIELD;	
		return "none";
	}


	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {

		PlayerRpgInventory inv = PlayerRpgInventory.get(par3EntityPlayer);

		for(int i = 0; i < inv.getSizeInventory(); i++){	
			if(inv.getStackInSlot(i) == null){
				if(inv.isItemValidForSlot(i, par1ItemStack)){
					inv.setInventorySlotContents(i, par1ItemStack);
					par3EntityPlayer.destroyCurrentEquippedItem();
				}
			}
		}

		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	/**'Bounds' the shield to an armor class. this should be overridden in child mods !
	 * If the string is left/set to "none", it will not check for class armor and can be used by anyone
	 * (like Vanilla Shields)*/
	public String boundArmorClass(){
		if(this.equals(mod_RpgInventory.berserkerShield))
			return mod_RpgInventory.CLASSBERSERKER;
		if(this.equals(mod_RpgInventory.archerShield))
			return mod_RpgInventory.CLASSARCHER;
		if(this.equals(mod_RpgInventory.talisman))
			return mod_RpgInventory.CLASSMAGE;	
		return "none";
	}
}
