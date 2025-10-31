package net.hasnat4763;

import net.hasnat4763.ScreenHandler.SixSevenStoryBookScreenHandler;
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
    private static final String TOTAL_PAGES = "10";

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

        this.addDrawableChild(ButtonWidget.builder(Text.literal("< Previous"), button -> {
            if (handler.getCurrentPage() > 0) {
                handler.previousPage();
            }
        }).dimensions(x + 10, y + backgroundHeight - 30, 50, 20). build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Next >"), button -> {
            if (handler.getCurrentPage() < 9) {
                handler.nextPage();
            }
        }).dimensions(x + backgroundWidth - 60, y + backgroundHeight - 30, 50, 20). build());

    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        Text pageText = Text.literal("Page " + (handler.getCurrentPage() + 1) + " / " + TOTAL_PAGES);
        int pageTextWidth = this.textRenderer.getWidth(pageText);
        context.drawText(this.textRenderer, pageText,
                x + (backgroundWidth - pageTextWidth) / 2,
                y + backgroundHeight - 15,
                0xFF404040, false);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFF000000, true);
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
    protected void handledScreenTick() {
        super.handledScreenTick();
    }
}
