package io.undervolt.gui.chat;

import com.google.common.collect.Lists;

import java.util.List;

public class ChatManager {

    private final Tab reservedServerTab = new Tab(true, "server_reserved", -1);
    private final Tab reservedLogTab = new Tab(true, "#debug-log", -1);

    public Tab getReservedServerTab() {
        return reservedServerTab;
    }

    private List<Tab> openTabs = Lists.newArrayList(
            reservedServerTab, reservedLogTab,
            new Tab(true, "#general", 2)
    );

    public List<Tab> getOpenTabs() {
        return openTabs;
    }

    public void addTab(final Tab tab) {
        this.getOpenTabs().add(tab);
    }

    public Tab getReservedLogTab() {
        return reservedLogTab;
    }
}
