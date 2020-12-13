package io.undervolt.gui;

import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField
{
    public GuiPasswordField(int componentId, int x, int y, int par5Width, int par6Height)
    {
        super(componentId, Minecraft.getMinecraft().fontRendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void drawTextBox()
    {
        String password = getText();
        replaceText(Strings.repeat('*', getText().length()));
        super.drawTextBox();
        replaceText(password);
    }

    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode)
    {
        // Ignore ctrl+c and ctrl+x to prevent copying the contents of the field
        return  !GuiScreen.isKeyComboCtrlC(keyCode) && !GuiScreen.isKeyComboCtrlX(keyCode) && super.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        String password = getText();
        replaceText(Strings.repeat('*', getText().length()));
        super.mouseClicked(mouseX, mouseY, mouseButton);
        replaceText(password);
    }

    /**
     * Sets the text of the field while maintaining the cursor positions
     * @param newText
     */
    private void replaceText(String newText)
    {
        int cursorPosition = getCursorPosition();
        int selectionEnd = getSelectionEnd();
        setText(newText);
        setCursorPosition(cursorPosition);
        setSelectionPos(selectionEnd);
    }
}