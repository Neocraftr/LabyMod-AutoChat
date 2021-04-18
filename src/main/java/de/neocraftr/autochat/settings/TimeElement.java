package de.neocraftr.autochat.settings;

import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class TimeElement extends ControlElement {
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");

    private GuiTextField textField;
    private Consumer<Integer> callback;
    private int[] currentValue;
    private int selectedIndex;
    private int cursorPosition;
    private boolean hoverUp;
    private boolean hoverDown;
    private long fastTickerCounterValue;

    public TimeElement(String displayName, IconData iconData, int currentTime) {
        super(displayName, null, iconData);

        int[] currentTimeArr = new int[2];
        currentTimeArr[0] = (int) Math.floor((double) currentTime / 60D);
        currentTimeArr[1] = currentTime % 60;
        this.currentValue = currentTimeArr;

        this.fastTickerCounterValue = 0L;
        this.setSelectedIndex(-1);
        this.createTextfield();
    }

    public void createTextfield() {
        this.textField = new GuiTextField(-2, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, this.getObjectWidth(), 20);
        this.updateValue();
        this.textField.setFocused(false);
    }

    private void updateValue() {
        this.textField.setText(String.format("%02d:%02d", this.currentValue[0], this.currentValue[1]));
    }

    private boolean increaseValue() {
        if (this.currentValue[1] < 59) {
            this.currentValue[1]++;
            return true;
        } else if(this.currentValue[0] < 23) {
            this.currentValue[1] = 0;
            this.currentValue[0]++;
            return true;
        }
        return false;
    }

    private boolean decreaseValue() {
        if(this.currentValue[1] > 0) {
            this.currentValue[1]--;
            return true;
        } else if(this.currentValue[0] > 0) {
            this.currentValue[1] = 59;
            this.currentValue[0]--;
            return true;
        }
        return false;
    }

    private void setSelectedIndex(int i) {
        this.cursorPosition = 0;
        this.selectedIndex = i;
    }

    private boolean hoverTextField(int mouseX, int mouseY) {
        return mouseX >= this.textField.xPosition && mouseX < this.textField.xPosition + this.textField.width
                && mouseY >= this.textField.yPosition && mouseY < this.textField.yPosition + this.textField.height;
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.textField != null) {
            int width = this.getObjectWidth();
            LabyModCore.getMinecraft().setTextFieldXPosition(this.textField, maxX - width - 2);
            LabyModCore.getMinecraft().setTextFieldYPosition(this.textField, y + 1);
            this.textField.drawTextBox();
            LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
            DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            Minecraft.getMinecraft().getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            this.hoverUp = mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 2 && mouseY < y + 2 + 7;
            this.hoverDown = mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 12 && mouseY < y + 12 + 7;
            draw.drawTexture(maxX - 15, y + 2, 99.0D, this.hoverUp ? 37.0D : 5.0D, 11.0D, 7.0D, 11.0D, 7.0D);
            draw.drawTexture(maxX - 15, y + 12, 67.0D, this.hoverDown ? 52.0D : 20.0D, 11.0D, 7.0D, 11.0D, 7.0D);
            if (this.isMouseOver() && this.fastTickerCounterValue != 0L) {
                if (this.fastTickerCounterValue > 0L && this.fastTickerCounterValue + 80L < System.currentTimeMillis()) {
                    this.fastTickerCounterValue = System.currentTimeMillis();
                    increaseValue();
                }

                if (this.fastTickerCounterValue < 0L && this.fastTickerCounterValue - 80L > System.currentTimeMillis() * -1L) {
                    this.fastTickerCounterValue = System.currentTimeMillis() * -1L;
                    decreaseValue();
                }
            } else {
                this.mouseRelease(mouseX, mouseY, 0);
            }

            this.updateValue();

            if(this.selectedIndex == 0) {
                this.textField.setCursorPosition(0);
                this.textField.setSelectionPos(2);
            } else if(this.selectedIndex == 1) {
                this.textField.setCursorPosition(3);
                this.textField.setSelectionPos(5);
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoverUp && this.increaseValue()) {
            this.fastTickerCounterValue = System.currentTimeMillis() + 500L;
        } else if (this.hoverDown && this.decreaseValue()) {
            this.fastTickerCounterValue = System.currentTimeMillis() * -1L - 500L;
        } else if(this.hoverTextField(mouseX, mouseY)) {
            int i = LabyModCore.getMinecraft().getFontRenderer().trimStringToWidth(this.textField.getText(), mouseX - this.textField.xPosition).length();
            if(i < 3) {
                this.setSelectedIndex(0);
            } else if(i > 3) {
                this.setSelectedIndex(1);
            }
        }

    }

    public void unfocus(int mouseX, int mouseY, int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        this.setSelectedIndex(-1);
    }

    public void init() {
        super.init();
        this.setSelectedIndex(-1);
    }

    public void mouseRelease(int mouseX, int mouseY, int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.fastTickerCounterValue != 0L) {
            this.fastTickerCounterValue = 0L;
            if(this.callback != null) this.callback.accept(this.getCurrentTime());
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);

        if(!Character.isDigit(typedChar)) return;
        int number = Character.getNumericValue(typedChar);

        if(this.selectedIndex == 0) {
            if(this.cursorPosition == 0) {
                // first digit
                this.cursorPosition++;
                this.currentValue[0] = number;
                if(this.currentValue[0] > 5) this.setSelectedIndex(1);
            } else {
                // second digit
                this.currentValue[0] = this.currentValue[0]*10 + number;
                if(this.currentValue[0] > 59)  this.currentValue[0] = 59;
                this.setSelectedIndex(1);
            }
        } else if(this.selectedIndex == 1) {
            if(this.cursorPosition == 0) {
                // first digit
                this.cursorPosition++;
                this.currentValue[1] = number;
                if(this.currentValue[1] > 5) this.setSelectedIndex(-1);
            } else {
                // second digit
                this.currentValue[1] = this.currentValue[1]*10 + number;
                if(this.currentValue[1] > 59)  this.currentValue[1] = 59;
                this.setSelectedIndex(-1);
            }
        }

        if(this.callback != null) this.callback.accept(this.getCurrentTime());
    }

    public GuiTextField getTextField() {
        return this.textField;
    }

    public void setCallback(Consumer<Integer> callback) {
        this.callback = callback;
    }

    public int getObjectWidth() {
        return 50;
    }

    public int getCurrentTime() {
        return this.currentValue[0]*60 + this.currentValue[1];
    }
}
