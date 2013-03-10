package RpgRB.weapons.dagger;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import RpgInventory.playerjewels.models.weapons.ModelDaggerR;

public class RenderDagger implements IItemRenderer {

	ModelDaggerR daggerModel;

	public RenderDagger()
	{
		daggerModel = new ModelDaggerR();
	}
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type)
		{
		case  EQUIPPED: return true;
		case ENTITY: return true;
		default: break;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch(type)
		{
		case  EQUIPPED:
		{
			GL11.glPushMatrix();
			float scale = 0.9F;
			GL11.glScalef(scale,scale,scale);
			
			if(((Entity)data[1]).worldObj.getWorldTime() < 12500)
			{
				ForgeHooksClient.bindTexture("/subaraki/weapons/dagger.png", 0);
			}
			else
			{
				ForgeHooksClient.bindTexture("/subaraki/weapons/daggerNight.png", 0);

			}

			GL11.glRotatef(-150F, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(-100F, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(160F, 0.0f, 0.0f, 1.0f);

			GL11.glTranslatef(0.2F, -0.2F, -0.45F);

			daggerModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			ForgeHooksClient.unbindTexture();

			GL11.glPopMatrix();
			
		}
		break;

		case  ENTITY:
		{
			GL11.glPushMatrix();

			float scale = 1.5F;
			GL11.glScalef(scale,scale,scale);
			if(((Entity)data[1]).worldObj.getWorldTime() < 12500)
			{
				ForgeHooksClient.bindTexture("/subaraki/weapons/dagger.png", 0);
			}
			else
			{
				ForgeHooksClient.bindTexture("/subaraki/weapons/daggerNight.png", 0);

			}

			GL11.glRotatef(0F, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(0F, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(0F, 0.0f, 0.0f, 1.0f);

			GL11.glTranslatef(0F, 0F, 0F);

			daggerModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			ForgeHooksClient.unbindTexture();

			GL11.glPopMatrix();
		}
		break;

		default: break;
		}
	}

}
