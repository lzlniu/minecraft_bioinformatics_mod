package com.lzlniu.bioinformatics.events;

import com.lzlniu.bioinformatics.Bioinformatics;
import com.lzlniu.bioinformatics.util.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Bioinformatics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MicrobeSampling {
    @SubscribeEvent
    public static void RightClickWithCottonStick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        Item PlayerHandItem = player.getHeldItemMainhand().getItem();

        if (PlayerHandItem == RegistryHandler.SWAB.get()) {
            // Bioinformatics.LOGGER.info("Player tried collect microbe!");

            World ThisWorld = event.getWorld();
            BlockPos ThisBlockPos = event.getPos();
            Direction BlockDirection = event.getFace(); // get click direction of block
            Biome biome = ThisWorld.getBiome(ThisBlockPos);
            float CreatureProb = biome.getMobSpawnInfo().getCreatureSpawnProbability();

            BlockPos ThisBlockClickSidePos;
            if (BlockDirection == Direction.UP) ThisBlockClickSidePos = ThisBlockPos.up();
            else if (BlockDirection == Direction.DOWN) ThisBlockClickSidePos = ThisBlockPos.down();
            else if (BlockDirection == Direction.EAST) ThisBlockClickSidePos = ThisBlockPos.east();
            else if (BlockDirection == Direction.WEST) ThisBlockClickSidePos = ThisBlockPos.west();
            else if (BlockDirection == Direction.SOUTH) ThisBlockClickSidePos = ThisBlockPos.south();
            else if (BlockDirection == Direction.NORTH) ThisBlockClickSidePos = ThisBlockPos.north();
            else ThisBlockClickSidePos = ThisBlockPos;

            int ThisBlockLight = ThisWorld.getLight(ThisBlockPos); // brightness 0-15
            boolean IsOpenSky = ThisWorld.canBlockSeeSky(ThisBlockClickSidePos); // true or false, click side neighbor block
            if (ThisBlockLight<=0) {
                ThisBlockLight = ThisWorld.getLight(ThisBlockClickSidePos);
            } // if a block is transparent (with brightness), then light is itself, otherwise it's decide by the click neighbor block

            // float ThisWorldDayTime = ThisWorld.getDayTime();

            BlockPos WaterBase=null; // for under water pressure calculation
            float Humidity = biome.getDownfall(); // assign humidity (also check its click side has water or not)
            if (ThisWorld.hasWater(ThisBlockPos)) {
                WaterBase = ThisBlockPos;
                Humidity = 2;
            }
            else if (ThisWorld.hasWater(ThisBlockClickSidePos)) {
                WaterBase = ThisBlockClickSidePos;
                Humidity = 2;
            }
            if (IsOpenSky && ThisWorld.isRaining()) {
                if (biome.getPrecipitation() == Biome.RainType.RAIN) Humidity+=0.2; // during raining humidity add 0.2
                else if (biome.getPrecipitation() == Biome.RainType.SNOW) Humidity+=0.1; // during snowing humidity add 0.1
                if (Humidity > 2) Humidity=2; // set humidity always less or equal to 2
                //else if (Humidity < 0) Humidity=0; // set humidity always greater or equal to 0
            }

            int Height = ThisBlockPos.getY();
            float Pressure;
            if (WaterBase==null) Pressure = 256-Height;
            else { // block under or the player click side of block close water
                int WaterDepth;
                for (WaterDepth = 1; WaterDepth < 256 && ThisWorld.hasWater(WaterBase.up()) ; WaterDepth++) {
                    WaterBase = WaterBase.up();
                }
                Pressure = (256 - WaterBase.getY()) + 192*((float)WaterDepth/10); // above water level air pressure add water pressure
                // I assume 10m (block) water pressure = 1 sea level air pressure (mc sea level is 64, 256-64=192)
            }

            float Temperature = biome.getTemperature(ThisBlockPos);
            Temperature += (ThisBlockLight-8)*0.01; // minor adjustment on temperature depending on light strength
            //if (Temperature > 2) Temperature=2; // set temperature always less or equal than 2

            if (!player.getEntityWorld().isRemote && !ThisWorld.isRemote) {
                String msg; // initial an empty string
                if (IsOpenSky) msg = "Humidity: " + Humidity + "\nTemperature: " + Temperature + "\nPressure: " + Pressure + "\nLight: " + ThisBlockLight + "\nCreature: " + CreatureProb + "\nOpen sky.\nHeight: " + Height;
                else if (!IsOpenSky) msg = "Humidity: " + Humidity + "\nTemperature: " + Temperature + "\nPressure: " + Pressure + "\nLight: " + ThisBlockLight + "\nCreature: " + CreatureProb + "\nIn shadow or underground.\nHeight: " + Height;
                else msg = "Oh no, strange block!";
                List<ITextComponent> list = null;
                list.add(new StringTextComponent(msg));
                PlayerHandItem.addInformation(new ItemStack(RegistryHandler.SWAB.get()), ThisWorld, list, ITooltipFlag.TooltipFlags.NORMAL);
                
                // player.sendMessage(new StringTextComponent(msg), player.getUniqueID());
            }
        }
    }
}
