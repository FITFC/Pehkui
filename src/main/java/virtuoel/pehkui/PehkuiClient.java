package virtuoel.pehkui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.ScaleUtils;

public class PehkuiClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		if (FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1"))
		{
			ClientPlayNetworking.registerGlobalReceiver(Pehkui.SCALE_PACKET, (client, handler, buf, sender) ->
			{
				final int id = buf.readVarInt();
				final Identifier typeId = buf.readIdentifier();
				
				final NbtCompound scaleData = ScaleUtils.buildScaleNbtFromPacketByteBuf(buf);
				
				if (!ScaleRegistries.SCALE_TYPES.containsKey(typeId))
				{
					return;
				}
				
				client.execute(() ->
				{
					final Entity e = client.world.getEntityById(id);
					
					if (e != null)
					{
						ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, typeId).getScaleData(e).readNbt(scaleData);
					}
				});
			});
		}
		else
		{
			Pehkui.LOGGER.fatal("Failed to register scale packet handler! Is Fabric API's networking module missing?");
		}
	}
}
