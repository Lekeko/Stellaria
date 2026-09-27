package com.keko.stellaria;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.blocks.ModBlocks;
import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.effects.ModStatusEffects;
import com.keko.stellaria.entity.ModEntities;
import com.keko.stellaria.entity.ModEntitiesAttributes;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.features.ModFeatures;
import com.keko.stellaria.items.ModGroupItems;
import com.keko.stellaria.items.ModItems;
import com.keko.stellaria.logic.overrides.VanillaItemsOverride;
import com.keko.stellaria.packet.StellarEventHudSwitchS2C;
import com.keko.stellaria.packet.networking.ModMessagesServer;
import com.keko.stellaria.particles.ModParticles;
import com.keko.stellaria.sounds.ModSounds;
import com.keko.stellaria.stellarEvent.artha.ArthaDimensionHandler;
import com.keko.stellaria.stellarEvent.artha.StellarEventHandler;
import com.keko.stellaria.stellarEvent.ebony.EbonyDimensionHandler;
import com.keko.stellaria.world.ModBiomes;
import com.keko.stellaria.world.dimension.ModDimensions;
import com.keko.stellaria.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Stellaria implements ModInitializer {

	public static final String MOD_ID = "stellaria";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Map<UUID, ListTag> SAVED_INVENTORIES = new HashMap<>();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
	public void onInitialize() {
        init();
        event();

        ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.parse("stellaria_particles"),
                FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.NORMAL);

    }

    private void init() {
        ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.parse("stellaria_particles"),
                FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
        ModSounds.registerSounds();
        ModBlockEntity.init();
        ModBlocks.init();
        ModItems.init();
        ModDimensions.init();
        ModMessagesServer.registerC2SPacket();
        ModEntities.init();
        ModEntitiesAttributes.init();
        ModParticles.init();
        ModProjectiles.init();
        ModStatusEffects.init();
        ModGroupItems.init();
        ModDataComponentTypes.init();
        ModFeatures.init();
        ModBiomes.init();
        ModWorldGen.init();
    }

    private void event(){
        UseItemCallback.EVENT.register((player, world, hand) ->{
            VanillaItemsOverride.listen(player, world, hand);

            return InteractionResultHolder.pass(ItemStack.EMPTY);

        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            Level level_artha = server.getLevel(ModDimensions.ARTHA_LEVEL_KEY);
            if (level_artha == null) return;

            if (!level_artha.isClientSide){
                if (!level_artha.players().isEmpty())
                    ArthaDimensionHandler.tick(level_artha);
                else {
                    StellarEventHandler.cancelEvent(level_artha);
                }

                if (StellarEventHandler.getStellarEventInstance() == null)
                    for (Player player : level_artha.players())
                        ServerPlayNetworking.send((ServerPlayer) player, new StellarEventHudSwitchS2C(
                                1,
                                0,0,0,0

                        ));
            }
            Level level_ebony = server.getLevel(ModDimensions.EBONY_LEVEL_KEY);
            if (level_ebony == null) return;
            if (!level_artha.isClientSide){
                if (!level_ebony.players().isEmpty())
                    EbonyDimensionHandler.tick(level_ebony);
                else EbonyDimensionHandler.remove(level_ebony);
            }

        });



        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            UUID id = oldPlayer.getUUID();

            boolean inArtha = oldPlayer.level()
                    .dimension()
                    .equals(ModDimensions.ARTHA_LEVEL_KEY);

            if (inArtha) {
                ListTag tag = new ListTag();
                oldPlayer.getInventory().save(tag);
                SAVED_INVENTORIES.put(id, tag);
            }

            ListTag saved = SAVED_INVENTORIES.remove(id);

            if (saved != null) {
                newPlayer.getInventory().load(saved);
            }
        });
    }
}