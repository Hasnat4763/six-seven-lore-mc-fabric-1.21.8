package net.hasnat4763;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.hasnat4763.items.ModItems.RegisterModItems;
import static net.hasnat4763.items.ScreenHandler.ModScreenHandler.RegisterModScreenHandler;

public class SixSeven implements ModInitializer {
	public static final String MOD_ID = "six-seven";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
        RegisterModItems();
        RegisterModScreenHandler();
	}
}