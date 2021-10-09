package io.undervolt.gui.friends;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.Panel;
import io.undervolt.gui.user.UserCard;
import io.undervolt.gui.user.UserManager;
import io.undervolt.gui.user.UserScreen;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendsScreen extends Panel {

    private final Chocomint chocomint;
    private final FriendsManager friendsManager;

    private final List<UserCard> friendUserCardList = Lists.newArrayList();
    private final List<UserCard> frUserCardList = Lists.newArrayList();

    public FriendsScreen(GuiScreen previousScreen) {
        super(previousScreen, "", 0);
        this.chocomint = GameBridge.getChocomint();
        this.friendsManager = this.chocomint.getFriendsManager();
    }

    @Override
    public void initGui() {
        this.updateUserCardList();
        super.initGui();
    }

    public void updateUserCardList() {
        this.friendUserCardList.clear();
        this.friendsManager.friendsPool.forEach((username, user) -> this.friendUserCardList.add(new UserCard(this.mc, user, true, false,
                (u) -> this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, u)))));

        this.frUserCardList.clear();
        this.friendsManager.friendRequestPool.forEach((username, user) -> this.frUserCardList.add(new UserCard(this.mc, user, true, false,
                (u) -> this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, u)))));
    }

    @Override
    public void drawContent(int mouseX, int mouseY, float partialTicks, int margin, int scroll) {
        AtomicInteger y = new AtomicInteger(43 + scroll);
        drawString(this.mc.fontRendererObj, "Amigos", margin + 5, y.get() - 15, Color.WHITE.getRGB());
        this.friendUserCardList.forEach(userCard -> {
            userCard.drawCard(margin + 5, y.get(), getPanelWidth() - 10, 38, mouseX, mouseY);
            y.set(y.get() + 40);
        });

        if(friendUserCardList.isEmpty()) {
            this.chocomint.getRenderUtils().drawRoundedRect(margin + 5, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
            drawCenteredString(this.mc.fontRendererObj, "No tenés amigos", margin + 5 + (this.getPanelWidth() - 10) / 2, y.get() + 13, Color.LIGHT_GRAY.getRGB());
            y.set(y.get() + 40);
        }

        this.chocomint.getRenderUtils().drawLine(margin + 5, y.get() + 5, margin + getPanelWidth() - 8, y.get() + 6, 2, Color.LIGHT_GRAY.getRGB());

        drawString(this.mc.fontRendererObj, "Solicitudes de amistad", margin + 5, y.get() + 15, Color.WHITE.getRGB());
        y.set(y.get() + 30);

        this.frUserCardList.forEach(userCard -> {
            userCard.drawCard(margin + 5, y.get(), getPanelWidth() - 10, 38, mouseX, mouseY);
            y.set(y.get() + 40);
        });

        if(frUserCardList.isEmpty()) {
            this.chocomint.getRenderUtils().drawRoundedRect(margin + 5, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
            drawCenteredString(this.mc.fontRendererObj, "No hay solicitudes", margin + 5 + (this.getPanelWidth() - 10) / 2, y.get() + 13, Color.LIGHT_GRAY.getRGB());
            y.set(y.get() + 65);
        }

        this.pageSize = y.get();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.friendUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });

        this.frUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
