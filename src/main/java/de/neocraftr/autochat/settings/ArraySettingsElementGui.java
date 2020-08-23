package de.neocraftr.autochat.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.neocraftr.autochat.AutoChat;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.codec.language.bm.Lang;

public class ArraySettingsElementGui extends GuiScreen {
    private Scrollbar scrollbar = new Scrollbar(29);
    private GuiScreen lastScreen;
    private boolean addElementScreen = false;
    private int hoveredIndex = -1;
    private GuiButton buttonEdit;
    private GuiButton buttonRemove;
    private String title;
    public ArrayList<String> elements;
    public Consumer<ArrayList<String>> changeListener;
    public int selectedIndex = -1;

    public ArraySettingsElementGui(GuiScreen lastScreen, String title, ArrayList<String> elements, Consumer<ArrayList<String>> changeListener) {
        this.elements = elements;
        this.title = title;
        this.lastScreen = lastScreen;
        this.changeListener = changeListener;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar.init();
        this.scrollbar.setPosition(this.width / 2 + 102, 44, this.width / 2 + 106, this.height - 32 - 3);
        this.scrollbar.setSpeed(10);
        this.buttonList
                .add(this.buttonRemove = new GuiButton(1, this.width / 2 - 120, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_remove")));
        this.buttonList
                .add(this.buttonEdit = new GuiButton(2, this.width / 2 - 37, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_edit")));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 120 - 75, this.height - 26, 75, 20, LanguageManager.translateOrReturnKey("button_add")));
        this.buttonList.add(new GuiButton(4, 20, 5, 58, 20, "< "+ LanguageManager.translateOrReturnKey("button_done")));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        final GuiScreen lastScreen;
        super.actionPerformed(button);
        switch (button.id) {
        case 1:
            lastScreen = (Minecraft.getMinecraft()).currentScreen;
            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo((result, id) -> {
                if (result) {
                    elements.remove(selectedIndex);
                    changeListener.accept(elements);
                }
                Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                selectedIndex = -1;
            }, "Soll diese Nachricht wirklich gelöscht werden?", AutoChat.getAutoChat().colorize(elements.get(this.selectedIndex)),
                    1));
            break;
        case 2:
            Minecraft.getMinecraft().displayGuiScreen(new ArraySettingsElementGuiAdd(this, this.selectedIndex));
            break;
        case 3:
            Minecraft.getMinecraft().displayGuiScreen(new ArraySettingsElementGuiAdd(this, -1));
            break;
        case 4:
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        if (this.addElementScreen)
            return;
        this.hoveredIndex = -1;
        double yPos = 45.0D + this.scrollbar.getScrollY() + 3.0D;
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            yPos += drawEntry(i, element, yPos, mouseX, mouseY);
        }
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, 41);
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(this.height - 32, this.height);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowTop(41.0D, 0.0D, this.width);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowBottom((this.height - 32), 0.0D, this.width);
        LabyMod.getInstance().getDrawUtils().drawCenteredString(title, (this.width / 2), 25.0D);
        this.scrollbar.update(elements.size());
        this.scrollbar.draw();
        this.buttonEdit.enabled = (this.selectedIndex != -1);
        this.buttonRemove.enabled = (this.selectedIndex != -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private double drawEntry(int index, String value, double y, int mouseX, int mouseY) {
        int x = this.width / 2 - 100;
        List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(value, x + 40);
        boolean hovered = (mouseX > x && mouseX < x + 200 && mouseY > y && mouseY < y + 24.0D && mouseX > 32
                && mouseY < this.height - 32);
        if (hovered)
            this.hoveredIndex = index;
        int borderColor = (this.selectedIndex == index) ? ModColor.toRGB(240, 240, 240, 240) : Integer.MIN_VALUE;
        int backgroundColor = hovered ? ModColor.toRGB(50, 50, 50, 120) : ModColor.toRGB(30, 30, 30, 120);
        drawRect(x - 5, (int) y - 4, x + 200, (int) y + 24 + ((list.size() > 1) ? (5 * list.size()) : 0),
                backgroundColor);
        LabyMod.getInstance().getDrawUtils().drawRectBorder((x - 5), ((int) y - 4), (x + 200),
                ((int) y + 24 + ((list.size() > 1) ? (5 * list.size()) : 0)), borderColor, 1.0D);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < list.size(); i++) {
            String element = list.get(i);
            LabyMod.getInstance().getDrawUtils().drawString(AutoChat.getAutoChat().colorize(element), (x + 5), y + 5.0D + (i * 10.0D));
        }
        return 29.0D + ((list.size() > 1) ? (list.size() * 5.0D) : 0);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoveredIndex != -1)
            this.selectedIndex = this.hoveredIndex;
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}
