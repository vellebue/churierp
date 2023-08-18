package org.bastanchu.churierp.churierpweb.component.tab;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;

public class TabbedContainer extends VerticalLayout {

    private Tabs tabsContainer = null;
    private Div viewportContainer;
    private Map<String, Component> tabbedContainerModel = new HashMap<>();
    private Tab selectedTab = null;
    private String DEFAULT_WIDTH = "80%";

    private TabbedListener tabbedListener;

    public TabbedContainer() {
        tabsContainer = new Tabs();
        viewportContainer = new Div();
        add(tabsContainer);
        add(viewportContainer);
        tabsContainer.addSelectedChangeListener(event -> {
           Tab selectedTab = event.getSelectedTab();
           if (selectedTab != null) {
               Component selectedComponent = tabbedContainerModel.get(selectedTab.getLabel());
               this.selectedTab = selectedTab;
               viewportContainer.removeAll();
               viewportContainer.add(selectedComponent);
               if (tabbedListener != null) {
                   tabbedListener.onSelectedTab(event.getSelectedTab());
               }
           }
        });
        tabsContainer.setWidth(DEFAULT_WIDTH);
        viewportContainer.setWidth(DEFAULT_WIDTH);
    }

    public void addTab(String tabLabel, Component bodyComponent) {
        tabbedContainerModel.put(tabLabel, bodyComponent);
        Tab tab = new Tab(tabLabel);
        tabsContainer.add(tab);
        if (tabbedContainerModel.keySet().size() == 1) {
            tabsContainer.setSelectedTab(tab);
            selectedTab = tab;
            viewportContainer.removeAll();
            viewportContainer.add(bodyComponent);
        }
    }

    public void setWidth(String width) {
        super.setWidth(width);
        if (tabsContainer != null) {
            tabsContainer.setWidth(width);
        }
        if (viewportContainer != null) {
            viewportContainer.setWidth(width);
        }
    }

    public void resetTabbedContainer() {
        tabbedContainerModel = new HashMap<>();
        tabsContainer.removeAll();
        viewportContainer.removeAll();
    }

    public Tab getSelectedTab() {
        return selectedTab;
    }

    public void setTabbedListener(TabbedListener tabbedListener) {
        this.tabbedListener = tabbedListener;
    }
}
