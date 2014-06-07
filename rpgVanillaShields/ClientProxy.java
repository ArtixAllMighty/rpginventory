package rpgVanillaShields;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		MinecraftForgeClient.registerItemRenderer(
				RpgVanillaShields.shieldDiamond, new VanillaShieldRenderer(
						new VanillaShield(),
						"rpginventorymod:jewels/ShieldDiamond.png"));
		MinecraftForgeClient.registerItemRenderer(
				RpgVanillaShields.shieldGold, new VanillaShieldRenderer(
						new VanillaShield(), "rpginventorymod:jewels/ShieldGold.png"));
		MinecraftForgeClient.registerItemRenderer(
				RpgVanillaShields.shieldIron, new VanillaShieldRenderer(
						new VanillaShield(), "rpginventorymod:jewels/ShieldIron.png"));
		MinecraftForgeClient.registerItemRenderer(
				RpgVanillaShields.shieldWood, new VanillaShieldRenderer(
						new VanillaShield(), "rpginventorymod:jewels/ShieldWood.png"));
	}
}
