package io.undervolt.gui.user;

import com.google.common.collect.Maps;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.menu.Menu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserSearch extends Menu {
    private final GuiScreen previous;
    private final ChatManager chatManager;
    private final Almendra almendra;
    private final Map<String, UserCard> filteredUserMap = Maps.newHashMap();

    private GuiTextField textField;

    public UserSearch(final GuiScreen previous ) {
        super(previous, "Buscar un usuario", 0);
        this.previous = previous;
        this.chatManager = this.chocomint.getChatManager();
        this.almendra = this.chocomint.getAlmendra();
    }

    @Override
    public void initGui() {

        this.textField = new GuiTextField(0, this.fontRendererObj, 20,
                25, this.width - 40, 20);

        this.textField.setFocused(true);
        super.initGui();
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.textField.drawTextBox();

        AtomicInteger y = new AtomicInteger(60);
        AtomicInteger x = new AtomicInteger(2);
        this.filteredUserMap.forEach((u, c) -> {
                c.drawCard(this.width / 2 - 65, y.get());
            y.set(y.get() + 43);
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(this.height);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if(filteredUserMap.size() > 0) {
            this.filteredUserMap.forEach((user, userCard) -> {
                if (mouseX >= userCard.x && mouseY >= userCard.y && mouseX <= userCard.x + 130 && mouseY <= userCard.y + 38) {
                    userCard.getConsumer().accept(userCard.getUser());
                }
            });
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
            this.textField.textboxKeyTyped(typedChar, keyCode);

            this.filteredUserMap.clear();

            if(this.textField.getText().length() >= 3) {
                this.almendra.getConnectedUsers().stream().filter(user -> user.toLowerCase()
                        .startsWith(this.textField.getText().toLowerCase())).collect(Collectors.toList()).forEach(username -> {
                            User user = new User(username, User.Status.ONLINE, "", false, "default");
                            this.filteredUserMap.put(username,
                                new UserCard(chocomint, this.mc, user,true, false, (u) -> this.mc.displayGuiScreen(new UserScreen(this.previous, u)))
                            );
                        }
                );
            }

        } else {
            if(!this.textField.getText().isEmpty()) {
                if(this.almendra.getConnectedUsers().contains(this.textField.getText().trim()) && !this.textField.getText().trim().equalsIgnoreCase(this.chocomint.getUser().getUsername())) {
                    User user = new User(this.textField.getText().trim(), User.Status.ONLINE, "", false, "default");
                    this.mc.displayGuiScreen(new UserScreen(this.previous, user));
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
