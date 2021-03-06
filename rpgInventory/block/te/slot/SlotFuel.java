/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpgInventory.block.te.slot;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFuel extends Slot {

	public final int slotID;

	public SlotFuel(IInventory inv, int slotID, int y, int z) {
		super(inv, slotID, y, z);
		this.slotID = slotID;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {

		if ((par1ItemStack.getItem() == Items.coal)
				|| par1ItemStack.getItem().equals(Items.lava_bucket)
				|| par1ItemStack.getItem().equals(Items.blaze_rod)
				|| (Item.getItemFromBlock(Blocks.coal_block) == par1ItemStack.getItem())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		inventory.setInventorySlotContents(slotID, par2ItemStack);
	}
}
