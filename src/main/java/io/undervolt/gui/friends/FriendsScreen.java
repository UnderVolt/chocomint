package io.undervolt.gui.friends;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.Panel;
import io.undervolt.gui.clickable.dropdown.ClickableDropDownMenu;
import io.undervolt.gui.clickable.dropdown.Option;
import io.undervolt.gui.user.User;
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

    private final UserCard currentSessionUserCard;
    private Option.OptionSet optionSet;
    private ClickableDropDownMenu profileOptions;

    private final List<UserCard> friendUserCardList = Lists.newArrayList();
    private final List<UserCard> frUserCardList = Lists.newArrayList();

    public FriendsScreen(GuiScreen previousScreen) {
        super(previousScreen, "", 0);
        this.chocomint = GameBridge.getChocomint();
        this.currentSessionUserCard = new UserCard(GameBridge.getMinecraft(), this.chocomint.getUser(), true, false, user -> {
            this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, this.chocomint.getUser()));
        });
        this.friendsManager = this.chocomint.getFriendsManager();
    }

    @Override
    public void initGui() {
        this.optionSet = Option.OptionSet.newOptionSet();
        List<Option> options = Lists.newArrayList(
                new Option(this.optionSet, "En línea", a -> this.chocomint.getUser().setStatus(User.Status.ONLINE)),
                new Option(this.optionSet, "Ausente", a -> this.chocomint.getUser().setStatus(User.Status.AWAY)),
                new Option(this.optionSet, "Ocupado", a -> this.chocomint.getUser().setStatus(User.Status.BUSY)),
                new Option(this.optionSet, "Desconectar", a -> this.disconnect())
        );
        this.optionSet.setOptions(options);
        switch(this.chocomint.getUser().getStatus()) {
            case ONLINE:
                this.optionSet.selectOption(0);
                break;
            case AWAY:
                this.optionSet.selectOption(1);
                break;
            case BUSY:
                this.optionSet.selectOption(2);
                break;
            case OFFLINE:
                this.optionSet.selectOption(3);
                break;
        }
        this.profileOptions = new ClickableDropDownMenu(width - getPanelWidth() + 5, scroll + 90, getPanelWidth() - 10, optionSet);
        this.updateUserCardList();
        super.initGui();
    }

    public void disconnect() {
        this.chocomint.getUser().setStatus(User.Status.OFFLINE);
        this.chocomint.getConfig().setToken(null);
        this.chocomint.getFriendsManager().friendsPool.clear();
        this.chocomint.getFriendsManager().friendRequestPool.clear();
        this.chocomint.setUser(this.chocomint.getUserManager().setUser((String) null));
        this.chocomint.getAlmendra().disconnect();
        this.chocomint.getChatManager().removeTabs();
        this.fadeOut();
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

        this.mc.fontRendererObj.drawString("Sesión iniciada", margin + 5, 28 + scroll, Color.WHITE.getRGB());
        this.currentSessionUserCard.drawCard(margin + 5, 43 + scroll, getPanelWidth() - 10, 38, mouseX, mouseY);

        this.profileOptions.draw(mouseX, mouseY);
        this.profileOptions.setY(scroll + 90);

        this.chocomint.getRenderUtils().drawLine(margin + 5, scroll + 95 + this.profileOptions.height + 5, margin + getPanelWidth() - 8, scroll + 95 + this.profileOptions.height + 5, 2, Color.LIGHT_GRAY.getRGB());

        AtomicInteger y = new AtomicInteger(118 + scroll);

        drawString(this.mc.fontRendererObj, "Amigos", margin + 5, y.get() - 15 + this.profileOptions.height + 5, Color.WHITE.getRGB());
        this.friendUserCardList.forEach(userCard -> {
            userCard.drawCard(margin + 5, y.get() + this.profileOptions.height + 5, getPanelWidth() - 10, 38, mouseX, mouseY);
            y.set(y.get() + 45);
        });

        if(friendUserCardList.isEmpty()) {
            this.chocomint.getRenderUtils().drawRoundedRect(margin + 5, y.get() + this.profileOptions.height + 5, 130, 36, 4, new Color(22, 22, 22).getRGB());
            drawCenteredString(this.mc.fontRendererObj, "No tenés amigos", margin + 5 + (this.getPanelWidth() - 10) / 2, y.get() + 13 + this.profileOptions.height + 5, Color.LIGHT_GRAY.getRGB());
            y.set(y.get() + 45);
        }

        this.chocomint.getRenderUtils().drawLine(margin + 5, y.get() + 5 + this.profileOptions.height + 5, margin + getPanelWidth() - 8, y.get() + 6 + this.profileOptions.height + 5, 2, Color.LIGHT_GRAY.getRGB());

        drawString(this.mc.fontRendererObj, "Solicitudes de amistad", margin + 5, y.get() + 15 + this.profileOptions.height + 5, Color.WHITE.getRGB());
        y.set(y.get() + 30);

        this.frUserCardList.forEach(userCard -> {
            userCard.drawCard(margin + 5, y.get() + this.profileOptions.height + 5, getPanelWidth() - 10, 38, mouseX, mouseY);
            y.set(y.get() + 45);
        });

        if(frUserCardList.isEmpty()) {
            this.chocomint.getRenderUtils().drawRoundedRect(margin + 5, y.get(), 130, 36, 4, new Color(22, 22, 22).getRGB());
            drawCenteredString(this.mc.fontRendererObj, "No hay solicitudes", margin + 5 + (this.getPanelWidth() - 10) / 2, y.get() + 18 + this.profileOptions.height, Color.LIGHT_GRAY.getRGB());
            y.set(y.get() + 65);
        }

        this.pageSize = y.get() + this.profileOptions.height + 18;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.currentSessionUserCard.click(mouseY, mouseX);
        this.profileOptions.click(mouseX, mouseY, mouseButton);

        this.friendUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });

        this.frUserCardList.forEach(userCard -> {
            userCard.click(mouseY, mouseX);
        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
