package com.lzlniu.bioinformatics.events;

import com.lzlniu.bioinformatics.Bioinformatics;
import com.lzlniu.bioinformatics.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bioinformatics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MicrobeSampling {
    @SubscribeEvent
    public static void RightClickWithCottonStick(PlayerInteractEvent.RightClickBlock event) {

        PlayerEntity player = event.getPlayer();
        Item PlayerHandItem = player.getHeldItemMainhand().getItem();

        if (PlayerHandItem == RegistryHandler.DNA.get()) {
            // Bioinformatics.LOGGER.info("Player tried collect microbe!");

            World ThisWorld = event.getWorld();
            BlockPos ThisBlockPos = event.getPos();
            boolean IsOpenSky = ThisWorld.canBlockSeeSky(ThisBlockPos.up());
            boolean IsRaining = ThisWorld.isRaining();
            Biome biome = ThisWorld.getBiome(ThisBlockPos);
            String BName = biome.getCategory().getName();

            int ThisBlockLight = ThisWorld.getLight(ThisBlockPos);
            if (ThisBlockLight<=0) {
                Direction BlockDirection = event.getFace(); // get click direction of block
                if (BlockDirection==Direction.UP) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.up()); // top of this block light
                else if (BlockDirection==Direction.DOWN) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.down());
                else if (BlockDirection==Direction.EAST) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.east());
                else if (BlockDirection==Direction.WEST) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.west());
                else if (BlockDirection==Direction.SOUTH) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.south());
                else if (BlockDirection==Direction.NORTH) ThisBlockLight = ThisWorld.getLight(ThisBlockPos.north());
                else  ThisBlockLight = ThisWorld.getLight(ThisBlockPos);
            } // if a block is transparent, then the light is itself, otherwise it's decide by the click direction neighboring block
            // float ThisWorldDayTime = ThisWorld.getDayTime();

            float Humidity = biome.getDownfall();
            if (IsRaining && IsOpenSky) Humidity=1; // during raining or snowing humidity is 1.0

            float Temperature = biome.getTemperature(ThisBlockPos);
            Temperature += (ThisBlockLight-8)*0.01; // minor adjustment on temperature depending on light strength

            if (!player.getEntityWorld().isRemote && !ThisWorld.isRemote) {
                String msg; // initial an empty string
                //String pos = ThisBlockPos.getCoordinatesAsString();
                int pos  = ThisBlockPos.getY();
                if (IsOpenSky) msg = BName + "\nHumidity: " + Humidity + "\nTemperature: " + Temperature + "\nLight: " + ThisBlockLight + "\nOpen sky.\nY: " + pos;
                else if (!IsOpenSky) msg = BName + "\nHumidity: " + Humidity + "\nTemperature: " + Temperature + "\nLight: " + ThisBlockLight + "\nIn shadow or underground.\nY: " + pos;
                else msg = "Oh no, strange block!";
                player.sendMessage(new StringTextComponent(msg), player.getUniqueID());
            }
        }
    }
}
