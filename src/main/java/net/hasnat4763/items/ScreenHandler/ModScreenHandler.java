package net.hasnat4763.items.ScreenHandler;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.hasnat4763.SixSeven;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ModScreenHandler {

    private static final PacketCodec<ByteBuf, Hand> HAND_CODEC =
            PacketCodecs.indexed(i -> Hand.values()[i], Hand::ordinal);

    public static final ScreenHandlerType<SixSevenStoryBookScreenHandler> SIX_SEVEN_STORY_BOOK_HANDLER =
            new ExtendedScreenHandlerType<>(
                    SixSevenStoryBookScreenHandler::new,
                    HAND_CODEC
            );

    public static void registerScreenHandlers() {
        Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of(SixSeven.MOD_ID, "six_seven_story_book_handler"),
                SIX_SEVEN_STORY_BOOK_HANDLER
        );
    }

    public static void RegisterModScreenHandler() {
        registerScreenHandlers();
        SixSeven.LOGGER.info("Registering SIX SEVEN Screen Handlers for " + SixSeven.MOD_ID);
    }
}