package net.starmarine.ddblockstest;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.starmarine.ddblockstest.client.ClientSetup;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod("ddblockstest")
public class DdblockstestMod {
	public static final Logger LOGGER = LogManager.getLogger(DdblockstestMod.class);
	public static final String MODID = "ddblockstest";

	public DdblockstestMod(IEventBus modEventBus) {

		// Register setup listeners
		if (FMLEnvironment.dist.isClient()) {
			modEventBus.addListener(this::onClientSetup);
		}
	}

	private void onClientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(ClientSetup::registerColorHandlers);
	}
}