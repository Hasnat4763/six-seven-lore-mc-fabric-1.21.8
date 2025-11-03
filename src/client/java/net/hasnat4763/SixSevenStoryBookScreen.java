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
    private static final int TOTAL_PAGES = 10;

    public SixSevenStoryBookScreen(SixSevenStoryBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 180;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

        SixSeven.LOGGER.info("[Screen] Init called - x: {}, y: {}", this.x, this.y);
        addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> {
            var im = MinecraftClient.getInstance().interactionManager;
            if (im != null) im.clickButton(this.handler.syncId, SixSevenStoryBookScreenHandler.BTN_NEXT);
        }).dimensions(this.x + this.backgroundWidth - 28, this.y + this.backgroundHeight - 24, 20, 20).build());
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
        SixSeven.LOGGER.info("[Screen] drawBackground called - texture at x: {}, y: {}", x, y);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        SixSeven.LOGGER.info("[Screen] drawForeground called");
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFF000000, false);
        SixSeven.LOGGER.info("[Screen] Title drawn at x: {}, y: {}", this.titleX, this.titleY);
        int currentPage = this.handler.getCurrentPage();
        SixSeven.LOGGER.info("[Screen] Current page: {}", currentPage);
        drawStoryContent(context, currentPage);

        int displayPage = Math.min(currentPage + 1, TOTAL_PAGES);
        String label = "Page " + displayPage + " / " + TOTAL_PAGES;
        int labelWidth = this.textRenderer.getWidth(label);
        int labelX = (this.backgroundWidth - labelWidth) / 2;
        int labelY = this.backgroundHeight - 30;
        context.drawText(this.textRenderer, label, labelX, labelY, 0xFF000000, false);
        SixSeven.LOGGER.info("[Screen] Page label drawn: {}", label);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void drawStoryContent(DrawContext context, int currentPage) {
        SixSeven.LOGGER.info("[Screen] drawStoryContent called for page: {}", currentPage);

        int textX = 35;
        int textY = 35;
        int lineHeight = 11;

        String[] story = getStoryForPage(currentPage);
        SixSeven.LOGGER.info("[Screen] Story has {} lines", story.length);

        for (int i = 0; i < story.length; i++) {
            SixSeven.LOGGER.info("[Screen] Drawing line {}: '{}' at x: {}, y: {}",
                    i, story[i], textX, textY + (i * lineHeight));

            context.drawText(this.textRenderer, story[i],
                    textX, textY + (i * lineHeight),
                    0xFF000000, false);
        }

        SixSeven.LOGGER.info("[Screen] drawStoryContent finished");
    }

    private String[] getStoryForPage(int page) {
        return switch (page) {
            case 0 -> new String[]{
                    "The Book of 67",
                    "",
                    "Once there lived a man",
                    "in an ancient village.",
                    "He was known as Rick Ashley.",
                    "He had some supernatural",
                    "power.",
                    "",
                    "This is his story."
            };

            case 1 -> new String[]{
                    "Rick's Powers",
                    "",
                    "Rick had a secret power.",
                    "He could rickroll people.",
                    "Everytime, he'd say:",
                    "'Never gonna give you up,'",
                    "'Never gonna let you down.'",
                    "",
                    "One day,",
                    "everything changed."
            };

            case 2 -> new String[]{
                    "The New Boy",
                    "",
                    "A new boy came to",
                    "the village.",
                    "He had a mysterious number:",
                    "",
                    "6 7",
                    "",
                    "His powers were like Rick's,",
                    "but... different."
            };

            case 3 -> new String[]{
                    "The Difference",
                    "",
                    "The boy's power was",
                    "strange and unsettling.",
                    "Instead of rickrolling,",
                    "he would 'six seven' them.",
                    "",
                    "It felt... wrong.",
                    "It felt... cursed."
            };

            case 4 -> new String[]{
                    "The Villagers' Anger",
                    "",
                    "People were annoyed.",
                    "Before, Rick rolled them.",
                    "Now this boy six sevens",
                    "them constantly.",
                    "",
                    "They thought of doing",
                    "something about it."
            };

            case 5 -> new String[]{
                    "The Summoning",
                    "",
                    "One day, everyone",
                    "gathered without them.",
                    "They summoned",
                    "Tung Tung Sahur",
                    "and asked for help.",
                    "",
                    "He had a dark solution."
            };

            case 6 -> new String[]{
                    "The Warning",
                    "",
                    "Tung tung sahur said,",
                    "'I can help you,'",
                    "'but I can't make sure",
                    " that nothing bad",
                    " will happen.'",
                    "",
                    "The villagers agreed.",
                    "They wanted it to end."
            };

            case 7 -> new String[]{
                    "The Plan",
                    "",
                    "Villagers agreed.",
                    "They wanted a solution,",
                    "no matter the cost.",
                    "",
                    "He told them to capture",
                    "both the boy and Rick.",
                    "",
                    "The ritual would begin."
            };

            case 8 -> new String[]{
                    "The Execution",
                    "",
                    "Tung tung sahur made",
                    "a guillotine.",
                    "",
                    "They were beheaded",
                    "in front of everyone.",
                    "",
                    "The village celebrated.",
                    "But something was wrong..."
            };

            case 9 -> new String[]{
                    "The Final Words",
                    "",
                    "But they said something.",
                    "'You can't stop us",
                    " from rickrolling.'",
                    "",
                    "'Something bad will happen,'",
                    "said the boy.",
                    "",
                    "You have been cursed."
            };
            default -> new String[]{
                    "Error: Page not found"
            };
        };
    }
}