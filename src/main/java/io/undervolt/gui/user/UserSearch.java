package io.undervolt.gui.user;

import com.google.common.collect.Lists;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.chat.AvailableRooms;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.menu.Menu;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserCard;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserSearch extends Menu {
    private final GuiScreen previous;
    private final Chocomint chocomint;
    private final ChatManager chatManager;
    private final Almendra almendra;
    private final List<UserCard> filteredUserList = Lists.newArrayList();

    private GuiTextField textField;

    public UserSearch(final GuiScreen previous, final Chocomint chocomint) {
        super(previous, chocomint, "Enviar un mensaje privado", 0);
        this.chocomint = chocomint;
        this.previous = previous;
        this.chatManager = chocomint.getChatManager();
        this.almendra = chocomint.getAlmendra();
    }

    @Override
    public void initGui() {

        this.textField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100,
                22, 200, 23);

        this.textField.setFocused(true);
        super.initGui();
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.textField.drawTextBox();

        if(this.filteredUserList != null) {
            AtomicInteger y = new AtomicInteger(50);
            AtomicInteger x = new AtomicInteger(2);
            this.filteredUserList.forEach(u -> {
                    u.drawCard(this.width / 2 - 65, y.get());
                y.set(y.get() + 43);
            });
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(this.height);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if(filteredUserList.size() > 0) {
            for (UserCard userCard : this.filteredUserList) {
                if (mouseX >= userCard.x && mouseY >= userCard.y && mouseX <= userCard.x + 130 && mouseY <= userCard.y + 38) {
                    userCard.getConsumer().accept(userCard.getUser());
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1337097) {
            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
            if(this.previous instanceof Chat) {
                this.mc.displayGuiScreen(previous);
            } else if(this.previous instanceof AvailableRooms) {
                this.mc.displayGuiScreen(((AvailableRooms) this.previous).previous);
            } else {
                this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
            }
        } else {
            super.actionPerformed(button);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
            this.textField.textboxKeyTyped(typedChar, keyCode);

            if(this.textField.getText().length() >= 3) {
                this.almendra.getConnectedUsers().stream().filter(user -> user.toLowerCase()
                        .startsWith(this.textField.getText().toLowerCase())).collect(Collectors.toList()).forEach(username ->
                            this.filteredUserList.add(
                                new UserCard(chocomint, this.mc, new User(username, User.Status.ONLINE, "", false, "default"),
                                        true, false, (u) -> {
                                            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(username));
                                            if(this.previous instanceof Chat) {
                                                this.mc.displayGuiScreen(previous);
                                            } else if(this.previous instanceof AvailableRooms) {
                                                this.mc.displayGuiScreen(((AvailableRooms) this.previous).previous);
                                            } else {
                                                this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
                                            }
                                        }
                                )
                            )
                );
            }

        } else {
            if(!this.textField.getText().isEmpty()) {
                if(this.almendra.getConnectedUsers().contains(this.textField.getText().trim()) && !this.textField.getText().trim().equalsIgnoreCase(this.chocomint.getUser().getUsername())) {
                    this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
                    if(this.previous instanceof Chat) {
                        this.mc.displayGuiScreen(previous);
                    } else if(this.previous instanceof AvailableRooms) {
                        this.mc.displayGuiScreen(((AvailableRooms) this.previous).previous);
                    } else {
                        this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
                    }
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
