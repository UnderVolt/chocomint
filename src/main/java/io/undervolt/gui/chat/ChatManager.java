package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.User;

import java.util.List;

public class ChatManager {

    private List<Tab> openTabs = Lists.newArrayList(
            new Tab("#general", 2),
            new Tab("#general-2", 1)
    );

    public List<Tab> getOpenTabs() {
        return openTabs;
    }

    public void addTab(final Tab tab) {
        this.getOpenTabs().add(tab);
    }
}
