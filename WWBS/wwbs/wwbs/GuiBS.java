package WWBS.wwbs.wwbs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBS extends InventoryEffectRenderer {

	private static InventoryBasic invGetinv = new InventoryBasic("tmp", true,
			54);

	private float currentScroll = 0.0F;

	private boolean isScrolling = false;

	EntityPlayer player;
	private float xSize_lo;
	private float ySize_lo;
	public String hi;

	public static String inv;

	// public void handleMouseInput()
	// {
	// super.handleMouseInput();
	// int i = Mouse.getEventDWheel();
	//
	// if (i != 0 && this.needsScrollBars())
	// {
	// int j = ((ContainerBank)this.inventorySlots).itemList.size() / 9 - 5 + 1;
	//
	// if (i > 0)
	// {
	// i = 1;
	// }
	//
	// if (i < 0)
	// {
	// i = -1;
	// }
	//
	// this.currentScroll = (float)((double)this.currentScroll - (double)i /
	// (double)j);
	//
	// if (this.currentScroll < 0.0F)
	// {
	// this.currentScroll = 0.0F;
	// }
	//
	// if (this.currentScroll > 1.0F)
	// {
	// this.currentScroll = 1.0F;
	// }
	//
	// ((ContainerBank)this.inventorySlots).scrollTo(this.currentScroll);
	// }
	// }
	static InventoryBasic getInventory() {
		return invGetinv;
	}

	public GuiBS(EntityPlayer player, WwbsTe te) {
		super(new ContainerBank(player.inventory, te));
		this.player = player;
		hi = "Welcome " + player.username;
		inv = player.username + "'s Inventory";
	}

	@Override
	public void actionPerformed(GuiButton button) {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		if (button.id == 0) {
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation("gui/container.png"));
		int var5 = 222;
		int var6 = 176;
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		drawTexturedModalRect(posX, posY - 30, 0, 0, var6, var5);
		drawString(fontRenderer, inv, (this.width / 2) - 80,
				(this.height / 2) + 15, 0xffffff);
		drawString(fontRenderer, hi, (this.width / 2) - 80,
				(this.height / 2) - 107, 0xffffff);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		this.xSize_lo = par1;
		this.ySize_lo = par2;

		int k = this.guiLeft;
		int l = this.guiTop;
		int i1 = k + 175;
		int j1 = l + 18;
		int k1 = i1 + 14;
		int l1 = j1 + 112;
		if (this.isScrolling) {
			this.currentScroll = (par2 - j1 - 7.5F) / (l1 - j1 - 15.0F);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((ContainerBank) this.inventorySlots).scrollTo(this.currentScroll);
		}

	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();

		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;

		// this.buttonList.add(new GuiButton(0, posX + 130, posY + 1, 50, 20,
		// "button"));
	}

	private boolean needsScrollBars() {
		return ((ContainerBank) this.inventorySlots)
				.hasMoreThan1PageOfItemsInList();
	}
}
