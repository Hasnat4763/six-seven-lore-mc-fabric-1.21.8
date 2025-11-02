package net.hasnat4763;

import net.hasnat4763.ScreenHandler.SixSevenStoryBookScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.hasnat4763.SixSeven.MOD_ID;

public class SixSevenStoryBookScreen extends HandledScreen<SixSevenStoryBookScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/gui/six_seven.png");
    private static final int TOTAL_PAGES = 10; // display only

    public SixSevenStoryBookScreen(SixSevenStoryBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 180;
        SixSeven.LOGGER.info("Loading texture: {}", TEXTURE);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

        // Next page (client -> server)
        addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> {
            var im = MinecraftClient.getInstance().interactionManager;
            if (im != null) im.clickButton(this.handler.syncId, SixSevenStoryBookScreenHandler.BTN_NEXT);
        }).dimensions(this.x + this.backgroundWidth - 28, this.y + this.backgroundHeight - 24, 20, 20).build());

        // Previous page (client -> server)
        addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> {
            var im = MinecraftClient.getInstance().interactionManager;
            if (im != null) im.clickButton(this.handler.syncId, SixSevenStoryBookScreenHandler.BTN_PREV);
        }).dimensions(this.x + 8, this.y + this.backgroundHeight - 24, 20, 20).build());
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                x, y,
                0, 0,
                this.backgroundWidth,
                this.backgroundHeight,
                256, 180
        );
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Title with shadow
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFF000000, true);

        // Page label (use server-synced page; convert 0-based to 1-based)
        int page0 = this.handler.getCurrentPage();
        int displayPage = Math.min(page0 + 1, TOTAL_PAGES);
        String label = "Page " + displayPage + " / " + TOTAL_PAGES;

        int centerX = this.x + this.backgroundWidth / 2;
        int labelY = this.y + this.backgroundHeight - 18; // bottom-center inside the frame
        context.drawCenteredTextWithShadow(this.textRenderer, label, centerX, labelY, 0xFFFFFFFF);

        // Optional: debug marker to prove foreground draws
        // context.drawCenteredTextWithShadow(this.textRenderer, "DEBUG", centerX, this.y + 8, 0xFFFF5555);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}