package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bastanchu.churierp.churierpweb.component.button.IconButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwoListCombo extends HorizontalLayout {

    private MultiSelectListBox<String> selectedItems = new MultiSelectListBox<>();
    private MultiSelectListBox<String> availableItems = new MultiSelectListBox<>();
    private VerticalLayout centralBar = new VerticalLayout();
    private IconButton  rightButton = null;
    private IconButton  leftButton = null;
    //Model
    private List<String> selectedItemsModel = null;
    private List<String> availableItemsModel = null;

    public TwoListCombo() {
        add(selectedItems);
        rightButton = new IconButton("/icons/right-arrow.png", "right", e -> {
            Set<String> selectedMarkedItemsSet = selectedItems.getSelectedItems();
            List<String> selectedTotalItemsList = selectedItemsModel;
            List<String> selectedRemainingItemsList = selectedTotalItemsList.stream().filter(
                    el -> !selectedMarkedItemsSet.contains(el)
            ).collect(Collectors.toList());
            List<String> currentTargetList = availableItemsModel;
            currentTargetList.addAll(selectedMarkedItemsSet);
            selectedItems.setItems(selectedRemainingItemsList);
            selectedItemsModel = selectedRemainingItemsList;
            availableItems.setItems(currentTargetList);
            availableItemsModel = currentTargetList;
        });
        leftButton = new IconButton("/icons/left-arrow.png", "right", e -> {
            Set<String> availableMarkedItemsSet = availableItems.getSelectedItems();
            List<String> availableTotalItemsList = availableItemsModel;
            List<String> availableRemainingItemsList = availableTotalItemsList.stream().filter(
                    el -> !availableMarkedItemsSet.contains(el)
            ).collect(Collectors.toList());
            List<String> currentTargetSelectedList = selectedItemsModel;
            currentTargetSelectedList.addAll(availableMarkedItemsSet);
            selectedItems.setItems(currentTargetSelectedList);
            selectedItemsModel = currentTargetSelectedList;
            availableItems.setItems(availableRemainingItemsList);
            availableItemsModel = availableRemainingItemsList;
        });
        centralBar.setJustifyContentMode(JustifyContentMode.CENTER);
        centralBar.add(rightButton);
        centralBar.add(leftButton);
        add(centralBar);
        add(availableItems);
    }

    // TODO add move and sort actions
    private Component buildSelectedItemsContainer() {
        return null;
    }

    private Component buildAvailableItemsContainer() {
        return null;
    }

    public List<String> getSelectedItemsModel() {
        return selectedItemsModel;
    }

    public void setSelectedItemsModel(List<String> selectedItemsModel) {
        this.selectedItemsModel = selectedItemsModel;
        selectedItems.setItems(selectedItemsModel);
    }

    public List<String> getAvailableItemsModel() {
        return availableItemsModel;
    }

    public void setAvailableItemsModel(List<String> availableItemsModel) {
        this.availableItemsModel = availableItemsModel;
        availableItems.setItems(availableItemsModel);
    }
}
