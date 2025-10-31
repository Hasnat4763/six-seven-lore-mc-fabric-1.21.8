package net.hasnat4763;

import net.hasnat4763.ScreenHandler.SixSevenStoryBookScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.hasnat4763.SixSeven.MOD_ID;

public class SixSevenStoryBookScreen extends HandledScreen<SixSevenStoryBookScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/gui/six_seven.png");

    public SixSevenStoryBookScreen(SixSevenStoryBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 512;
        this.backgroundHeight = 256;
        SixSeven.LOGGER.info("Loading texture: {}", TEXTURE);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        Text pageText = Text.literal("Page " + (handler.getCurrentPage() + 1) + " / 67");
        context.drawText(this.textRenderer, pageText, x + 10, y + 10, 0xFF000000, false);
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
                512, 256
        );
    }
}
