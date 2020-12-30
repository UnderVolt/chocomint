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

    public void drawPanel(int screenWidth, int screenHeight) {
        if(this.isActive) {
            this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 132, 22, 130,
                            screenHeight - 24, 4, new Color(28, 28, 28).getRGB());

            drawCenteredString(this.mc.fontRendererObj, "Amigos", screenWidth - 66, 28, Color.WHITE.getRGB());


            AtomicInteger y = new AtomicInteger(40);
            this.friendUserCardList.forEach(userCard -> {
                userCard.drawCard(screenWidth - 132, y.get());
                y.set(y.get() + 40);
            });

            if(friendUserCardList.isEmpty()) {
                this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 132, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
                drawCenteredString(this.mc.fontRendererObj, "No tenés amigos", screenWidth - 66, y.get() + 13, Color.LIGHT_GRAY.getRGB());
                y.set(y.get() + 40);
            }

            drawCenteredString(this.mc.fontRendererObj, "Solicitudes de amistad", screenWidth - 66, y.get(), Color.WHITE.getRGB());
            y.set(y.get() + 20);

            this.frUserCardList.forEach(userCard -> {
                userCard.drawCard(screenWidth - 132, y.get());
                y.set(y.get() + 40);
            });

            if(frUserCardList.isEmpty()) {
                this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 132, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
                drawCenteredString(this.mc.fontRendererObj, "No hay solicitudes", screenWidth - 66, y.get() + 13, Color.LIGHT_GRAY.getRGB());
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
        this.friendsManager.friendsPool.forEach((username, user) -> this.friendUserCardList.add(new UserCard(this.chocomint, this.mc, user, true, false,
                (u) -> this.mc.displayGuiScreen(new UserScreen(null, this.chocomint, u)))));

        this.frUserCardList.clear();
        this.friendsManager.friendRequestPool.forEach((username, user) -> this.frUserCardList.add(new UserCard(this.chocomint, this.mc, user, true, false,
                (u) -> this.mc.displayGuiScreen(new UserScreen(null, this.chocomint, u)))));
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
        this.updateUserCardList();
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
