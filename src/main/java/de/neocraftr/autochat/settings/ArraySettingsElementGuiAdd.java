package de.neocraftr.autochat.settings;

import java.io.IOException;
import java.util.ArrayList;

import net.labymod.main.lang.LanguageManager;
import org.lwjgl.input.Keyboard;

import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ArraySettingsElementGuiAdd extends GuiScreen {
    private ArraySettingsElementGui lastScreen;
    private int editIndex;
    private ModTextField fieldText;
    private String newText = "";
    private GuiButton buttonDone;

    public ArraySettingsElementGuiAdd(ArraySettingsElementGui lastScreen, int editIndex) {
        this.lastScreen = lastScreen;
        this.editIndex = editIndex;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.fieldText = new ModTextField(-1, (LabyMod.getInstance().getDrawUtils()).fontRenderer, this.width / 2 - 100,
                this.height / 2 - 50, 200, 20);
        this.fieldText.setMaxStringLength(100);
        this.fieldText.setDisabledTextColour(1);
        this.buttonList
                .add(this.buttonDone = new GuiButton(0, this.width / 2 + 3, this.height / 2 + 35, 98, 20, LanguageManager.translateOrReturnKey("button_save")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 101, this.height / 2 + 35, 98, 20, LanguageManager.translateOrReturnKey("button_cancel")));
        this.buttonDone.enabled = false;
        if (this.editIndex != -1) {
            this.fieldText.setText(this.lastScreen.elements.get(editIndex));
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.fieldText.textboxKeyTyped(typedChar, keyCode))
            this.newText = this.fieldText.getText();
        this.buttonDone.enabled = (!this.newText.isEmpty());
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.fieldText.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
        case 0:
            if (this.editIndex != -1) {
                lastScreen.elements.set(editIndex, this.newText);
            } else {
                lastScreen.elements.add(newText);
            }
            lastScreen.selectedIndex = -1;
            lastScreen.changeListener.accept(lastScreen.elements);

            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            break;
        case 1:
            lastScreen.selectedIndex = -1;
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            break;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldText.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.fieldText.drawTextBox();
        LabyMod.getInstance().getDrawUtils().drawString("Nachricht:", (this.width / 2 - 100), (this.height / 2 - 65));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
