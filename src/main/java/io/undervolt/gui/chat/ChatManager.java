package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;

import java.util.List;
import java.util.stream.Collectors;

public class ChatManager {

    private final Chocomint chocomint;
    private final Tab reservedServerTab = new Tab(true, "server_reserved", -1, true);
    private final Tab reservedLogTab = new Tab(true, "#comandos", -1, true);
    private final List<Tab> openTabs = Lists.newArrayList(reservedServerTab);
    private final List<Message> sentMessages = Lists.newArrayList();
    private Tab selectedTab;

    public ChatManager() {
        this.chocomint = GameBridge.getChocomint();
        String user = this.chocomint.getChocomintUser();
        this.reservedLogTab.setRead();
        this.reservedServerTab.setRead();
        String[] messages = {
            "Esta es la pestaña de #comandos.",
            "Acá vas a poder realizar comandos del cliente.",
            "Para acceder a los chats globales, iniciá sesión con tu cuenta de UnderVolt.",
            "Para ver los comandos disponibles, hacé /help.",
            "Si encontraste algún bug, entrá a https://github.com/UnderVolt/chocomint/. Abajo hay una guia sobre como reportarlos.",
            "\247cDisfrutá!"
        };

        for(String msg : messages){
            this.reservedLogTab.addMessage(user, msg);
        }
    }

    public boolean hasUnreadMessages() {
        return this.getOpenTabs().stream().anyMatch(tab -> !tab.isRead());
    }


    public Tab getReservedServerTab() {
        return reservedServerTab;
    }

    public List<Tab> getOpenTabs() {
        return openTabs;
    }

    public void removeTabs() {
        this.openTabs.removeIf(tab -> !tab.equals(this.reservedServerTab) || !tab.equals(this.reservedLogTab));
    }

    public Tab getOrCreateTabByName(final String tabName) {
        Tab tab;
        final List<Tab> filteredTabs = this.openTabs.stream().filter(t -> t.getName().equals(tabName)).collect(Collectors.toList());
        if(filteredTabs.size() >= 1) {
            System.out.println(tabName + " was already open. Switching to tab.");
            tab = filteredTabs.get(0);
        } else {
            if(this.chocomint.getAlmendra().getAvailableRooms().containsKey(tabName)) {
                System.out.println(tabName + " existed within available rooms. Opening tab with available values.");
                tab = this.chocomint.getAlmendra().getAvailableRooms().get(tabName);
            } else {
                System.out.println(tabName + " didn't exist in open tabs or available rooms. Creating tab and selecting it.");
                tab = new Tab(true, tabName, 0, false);
                tab.setUnread();
            }
            this.addTab(tab);
        }

        if(this.chocomint.getMinecraft().currentScreen != null && this.chocomint.getMinecraft().currentScreen instanceof Chat) {
            System.out.println("Updated chat screen");
            ((Chat) this.chocomint.getMinecraft().currentScreen).update(true);
        }

        return tab;
    }

    public void addTab(final Tab tab) {
        if (!this.getOpenTabs().contains(tab)) {
            this.getOpenTabs().add(tab);
        }
    }

    public Tab getReservedLogTab() {
        return reservedLogTab;
    }

    public void setSelectedTab(Tab selectedTab) {
        selectedTab.setRead();
        this.selectedTab = selectedTab;
    }

    public void removeTab(final Tab tab) {
        this.setSelectedTab(this.openTabs.get(this.openTabs.indexOf(tab) - 1));
        this.openTabs.remove(tab);
    }

    public void removeCurrentTab() {
        this.removeTab(this.selectedTab);

    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public Tab getSelectedTab() {
        return selectedTab;
    }
}
