package de.neocraftr.autochat.settings;

import java.util.ArrayList;
import java.util.List;

import de.neocraftr.autochat.AutoChat;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ArraySettingsElement extends SettingsElement {

    protected IconData iconData;
    private boolean selected;
    private ArrayList<String> elements;
    private Consumer<ArrayList<String>> changeListener;
    private boolean hoverable;
    private int lastMaxX;
    private boolean blocked = false;

    public ArraySettingsElement(String displayName, IconData iconData, ArrayList<String> elements, Consumer<ArrayList<String>> changeListener) {
        super(AutoChat.getAutoChat().colorize(displayName), null);
        this.iconData = iconData;
        this.elements = new ArrayList<>(elements);
        this.changeListener = changeListener;
    }

    public IconData getIconData() {
        return this.iconData;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return this.blocked;
    }

    @Override
    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        this.lastMaxX = maxX;
        if (this.displayName != null) {
            LabyMod.getInstance().getDrawUtils().drawRectangle(x, y, maxX, maxY,
                    ModColor.toRGB(80, 80, 80, this.selected ? 130 : ((this.hoverable && this.mouseOver) ? 80 : 60)));
            int iconWidth = (this.iconData != null) ? 25 : 2;
            if (this.iconData != null) {
                if (this.iconData.hasTextureIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawImageUrl(this.iconData.getTextureIcon().getResourcePath(),
                            (x + 2), y + 2.0D, 255.0D, 255.0D, 16.0D, 16.0D);
                } else if (this.iconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(this.iconData.getMaterialIcon().createItemStack(),
                            (x + 3), (y + 2), null);
                }
            }
            int minusModifier = (this.iconData == null) ? 30 : 50;
            List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(
                    getDisplayName().isEmpty() ? (ModColor.cl("4") + "Unknown") : getDisplayName(),
                    maxX - x + iconWidth - minusModifier);
            int listY = y + 7 - ((Math.min(list.size(), 2)) - 1) * 5;
            int i = 0;
            for (String line : list) {
                LabyMod.getInstance().getDrawUtils().drawString(line, (x + iconWidth), listY);
                listY += 10;
                i++;
                if (i > 1)
                    break;
            }
        }
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    @Override
    public void drawDescription(int x, int y, int screenWidth) {
        String description = getDescriptionText();
        if (description == null)
            return;
        if (x > this.lastMaxX - 48)
            return;
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        List<String> list = draw.listFormattedStringToWidth(description, screenWidth / 3);
        TooltipHelper.getHelper().pointTooltip(x, y, 500L, (String[]) list.toArray());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        Minecraft.getMinecraft().displayGuiScreen(new ArraySettingsElementGui(Minecraft.getMinecraft().currentScreen,
                this.getDisplayName(), elements, changeListener));
    }

    @Override
    public int getEntryHeight() {
        return 23;
    }

    public ArraySettingsElement setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public ArraySettingsElement setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
        return this;
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void unfocus(int mouseX, int mouseY, int mouseButton) {
    }
}
