package io.undervolt.gui.friends;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.UserCard;
import io.undervolt.gui.user.UserScreen;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendsPanel extends Gui {
    private final Minecraft mc;
    private final FriendsManager friendsManager;
    public boolean isActive;
    private final Chocomint chocomint;

    private final List<UserCard> friendUserCardList = Lists.newArrayList();
    private final List<UserCard> frUserCardList = Lists.newArrayList();

    private int clickCounter = 0;

    public FriendsPanel(Chocomint chocomint, FriendsManager friendsManager, boolean isActive) {
        this.chocomint = chocomint;
        this.mc = chocomint.getMinecraft();
        this.friendsManager = friendsManager;
        this.isActive = isActive;

        this.updateUserCardList();

    }

    public void drawPanel(int screenWidth, int screenHeight, int mouseX, int mouseY) {
        if(this.isActive) {
            this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 140, 22, 138,
                            screenHeight - 24, 2, new Color(28, 28, 28).getRGB());

            drawString(this.mc.fontRendererObj, "Amigos", screenWidth - 134, 30, Color.WHITE.getRGB());


            AtomicInteger y = new AtomicInteger(43);
            this.friendUserCardList.forEach(userCard -> {
                userCard.drawCard(screenWidth - 136, y.get(), mouseX, mouseY);
                y.set(y.get() + 40);
            });

            if(friendUserCardList.isEmpty()) {
                this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 136, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
                drawCenteredString(this.mc.fontRendererObj, "No tenés amigos", screenWidth - 68, y.get() + 13, Color.LIGHT_GRAY.getRGB());
                y.set(y.get() + 40);
            }

            this.chocomint.getRenderUtils().drawLine(screenWidth - 134, y.get() + 5, screenWidth - 8, y.get() + 6, 2, Color.LIGHT_GRAY.getRGB());

            drawString(this.mc.fontRendererObj, "Solicitudes de amistad", screenWidth - 134, y.get() + 15, Color.WHITE.getRGB());
            y.set(y.get() + 30);

            this.frUserCardList.forEach(userCard -> {
                userCard.drawCard(screenWidth - 136, y.get(), mouseX, mouseY);
                y.set(y.get() + 40);
            });

            if(frUserCardList.isEmpty()) {
                this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 136, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
                drawCenteredString(this.mc.fontRendererObj, "No hay solicitudes", screenWidth - 68, y.get() + 13, Color.LIGHT_GRAY.getRGB());
                y.set(y.get() + 40);
            }

        }
    }

    public void click(int mouseX, int mouseY) {
        this.friendUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });

        this.frUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });
    }

    public void updateUserCardList() {
        this.friendUserCardList.clear();
        this.friendsManager.friendsPool.forEach((username, user) -> this.friendUserCardList.add(new UserCard(this.mc, user, true, false,
                (u) -> this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, u)))));

        this.frUserCardList.clear();
        this.friendsManager.friendRequestPool.forEach((username, user) -> this.frUserCardList.add(new UserCard(this.mc, user, true, false,
                (u) -> this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, u)))));
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
        this.updateUserCardList();
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
