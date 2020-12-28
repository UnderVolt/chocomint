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

    private final List<UserCard> userCardList = Lists.newArrayList();

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
            this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 154, 22, 130,
                            350, 4, new Color(28, 28, 28).getRGB());

            drawCenteredString(this.mc.fontRendererObj, "Amigos", screenWidth - 90, 26, Color.WHITE.getRGB());

            AtomicInteger y = new AtomicInteger(40);
            this.userCardList.forEach(userCard -> {
                userCard.drawCard(screenWidth - 154, y.get());
                y.set(y.get() + 40);
            });
        }
    }

    public void click(int mouseX, int mouseY) {
        this.userCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });
    }

    public void updateUserCardList() {
        this.userCardList.clear();
        this.friendsManager.friendsPool.forEach((username, user) -> this.userCardList.add(new UserCard(this.chocomint, this.mc, user, true, false,
                (u) -> {
                    this.mc.displayGuiScreen(new UserScreen(null, this.chocomint, u));
                })));
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
        this.updateUserCardList();
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
