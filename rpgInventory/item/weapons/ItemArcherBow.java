package rpgInventory.item.weapons;

import javax.swing.Icon;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import rpgInventory.mod_RpgInventory;
import rpgInventory.gui.rpginv.PlayerRpgInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemArcherBow extends Item {

	public static final String[] ItemNameArray = new String[] { "elmBow",
			"elmBow2", "elmBow3", "elmBow4" };

	public int usingItem = 0;
	@SideOnly(Side.CLIENT)
	private Icon[] IconArray;

	public ItemArcherBow(int par1) {
		super(par1);
		this.maxStackSize = 1;
		this.setMaxDamage(1000);
		this.setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player,
			ItemStack usingItem, int useRemaining) {
		// This never get called.
		// Now it gets called, added a hook in our custom renderer.
		if (stack == usingItem) {
			if ((usingItem != null)
					&& (usingItem.getItem().itemID == mod_RpgInventory.elfbow.itemID)) {
				if (useRemaining > 21) {
					return IconArray[3];
				} else if (useRemaining > 14) {
					return IconArray[2];
				} else if (useRemaining > 7) {
					return IconArray[1];
				}
			}
		}
		return IconArray[0];
	}

	@Override
	public Icon getIconFromDamage(int par1) {
		return this.IconArray[0];
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return -1;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World,
			EntityPlayer player) {
		return par1ItemStack;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer player) {
		ArrowNockEvent event = new ArrowNockEvent(player, par1ItemStack);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}
		player.setItemInUse(par1ItemStack,
				this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	/**
	 * called when the player releases the use item button. Args: itemstack,
	 * world, entityplayer, itemInUseCount
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World par2World,
			EntityPlayer player, int par4) {
		super.onPlayerStoppedUsing(stack, par2World, player, par4);
		usingItem = 0;
		PlayerRpgInventory inv = PlayerRpgInventory.get(player);
		ItemStack shield = inv.getJewelInSlot(1);

		int j = this.getMaxItemUseDuration(stack) - par4;
		ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		j = event.charge;

		boolean var5 = player.capabilities.isCreativeMode;

		ItemStack var3 = player.inventory.armorItemInSlot(3);
		ItemStack var2 = player.inventory.armorItemInSlot(2);
		ItemStack var1 = player.inventory.armorItemInSlot(1);
		ItemStack var0 = player.inventory.armorItemInSlot(0);

		if ((var3 != null) && (var2 != null) && (var1 != null)
				&& (var0 != null)) {
			Item item = var3.getItem();
			Item item1 = var2.getItem();
			Item item2 = var1.getItem();
			Item item3 = var0.getItem();

			if (item.equals(mod_RpgInventory.archerhood)
					&& item1.equals(mod_RpgInventory.archerchest)
					&& item2.equals(mod_RpgInventory.archerpants)
					&& item3.equals(mod_RpgInventory.archerboots)) {
				boolean flag = ((shield != null) && (shield.itemID == mod_RpgInventory.archerShield.itemID))
						|| player.capabilities.isCreativeMode;
				if (player.inventory.hasItem(Item.arrow.itemID) || flag) {

					float f = j / 20.0F;
					f = ((f * f) + (f * 2.0F)) / 3.0F;

					if ((double) f < (mod_RpgInventory.donators
							.contains(player.username) ? 0.2d : 0.5D)) {
						return;
					}

					if (f > 1.0F) {
						f = 1.0F;
					}

					EntityArrow entityarrow = new EntityArrow(par2World,
							player, f * 2.0F);
					boolean crit = mod_RpgInventory.donators
							.contains(player.username) ? true : false;
					entityarrow.setIsCritical(crit);

					if (f == 1.0F) {
						entityarrow.setIsCritical(true);
					}

					entityarrow.setDamage(entityarrow.getDamage()
							+ (flag ? 2D : 1D));
					entityarrow.setKnockbackStrength(mod_RpgInventory.donators
							.contains(player.username) ? 2 : 1);
					entityarrow.setFire(mod_RpgInventory.donators
							.contains(player.username) ? 10 : 5);

					if (flag) {
						entityarrow.canBePickedUp = 2;
					} else {
						player.inventory
								.consumeInventoryItem(Item.arrow.itemID);
					}
					if (!par2World.isRemote) {
						par2World.spawnEntityInWorld(entityarrow);
						if (mod_RpgInventory.donators.contains(player.username)) {
							par2World.spawnEntityInWorld(new EntityArrow(
									par2World, player, f * 2.0f));
						}
					}
					stack.damageItem(1, player);
					par2World.playSoundAtEntity(player, "random.bow", 1.0F,
							(1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F))
									+ (f * 0.5F));
				}
			}
		}
	}

	@Override
	public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) {
		player.setItemInUse(stack, count);
		usingItem++;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		this.IconArray = new Icon[ItemNameArray.length];

		for (int i = 0; i < this.IconArray.length; ++i) {
			String prefix = "rpginventorymod:";
			// System.out.println("rpginventorymod" + ":" + ItemNameArray[i]);
			this.IconArray[i] = par1IconRegister.registerIcon(prefix
					+ ItemNameArray[i]);
		}
		this.itemIcon = this.IconArray[0];
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return false;
	}
}
