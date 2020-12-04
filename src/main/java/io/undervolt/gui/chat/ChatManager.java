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
        final Tab tab;
        final List<Tab> filteredTabs = this.openTabs.stream().filter(t -> t.getName().equals(tabName)).collect(Collectors.toList());
        if(filteredTabs.get(0) != null) tab = filteredTabs.get(0);
        else {
            tab = new Tab(true, tabName, 0, false);
            this.openTabs.add(tab);
        }
        return tab;
    }

    public void addTab(final Tab tab) {
        this.getOpenTabs().add(tab);
    }

    public Tab getReservedLogTab() {
        return reservedLogTab;
    }
}
