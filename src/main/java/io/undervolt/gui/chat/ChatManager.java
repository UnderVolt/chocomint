package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.instance.Chocomint;

import java.util.List;
import java.util.stream.Collectors;

public class ChatManager {

    private final Chocomint chocomint;
    private final Tab reservedServerTab = new Tab(true, "server_reserved", -1, true);
    private final Tab reservedLogTab = new Tab(true, "#debug-log", -1, true);
    private final List<Tab> openTabs = Lists.newArrayList(reservedServerTab);
    private Tab selectedTab;

    public ChatManager(final Chocomint chocomint) {
        this.chocomint = chocomint;
    }


    public Tab getReservedServerTab() {
        return reservedServerTab;
    }

    public List<Tab> getOpenTabs() {
        return openTabs;
    }

    public Tab getOrCreateTabByName(final String tabName) {
        Tab tab;
        final List<Tab> filteredTabs = this.openTabs.stream().filter(t -> t.getName().equals(tabName)).collect(Collectors.toList());
        if(filteredTabs.size() >= 1) {
            tab = filteredTabs.get(0);
        } else {
            if(this.chocomint.getAlmendra().getAvailableRooms().containsKey(tabName)) {
                tab = this.chocomint.getAlmendra().getAvailableRooms().get(tabName);
            } else {
                tab = new Tab(true, tabName, 0, false);
            }
            this.openTabs.add(tab);
        }

        if(this.chocomint.getMc().currentScreen != null && this.chocomint.getMc().currentScreen instanceof Chat) {
            this.chocomint.getMc().currentScreen.updateScreen();
        }

        return tab;
    }

    public void addTab(final Tab tab) {
        this.getOpenTabs().add(tab);
    }

    public Tab getReservedLogTab() {
        return reservedLogTab;
    }

    public void setSelectedTab(Tab selectedTab) {
        this.selectedTab = selectedTab;
    }

    public Tab getSelectedTab() {


        return selectedTab;
    }
}
