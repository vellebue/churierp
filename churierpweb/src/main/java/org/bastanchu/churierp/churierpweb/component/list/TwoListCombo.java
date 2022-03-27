package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bastanchu.churierp.churierpweb.component.button.IconButton;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwoListCombo extends HorizontalLayout {

    private ListBox<String> selectedItems = new ListBox<>();
    private MultiSelectListBox<String> availableItems = new MultiSelectListBox<>();
    private HorizontalLayout iconsLeftBar = new HorizontalLayout();
    //private VerticalLayout leftBar = new VerticalLayout();
    private VerticalLayout centralBar = new VerticalLayout();
    private HorizontalLayout iconsRightBar = new HorizontalLayout();
    //private VerticalLayout rightBar = new VerticalLayout();
    private IconButton  rightButton = null;
    private IconButton  leftButton = null;
    //Model
    private List<String> selectedItemsModel = null;
    private List<String> availableItemsModel = null;
    private Boolean availableItemsModelItemsSort = null;

    public TwoListCombo() {
        add(buildSelectedItemsContainer());
        rightButton = new IconButton("/icons/right-arrow.png", "right", e -> {
            Set<String> selectedMarkedItemsSet = new HashSet<>();
            if (selectedItems.getValue() != null) {
                selectedMarkedItemsSet.add(selectedItems.getValue());
            }
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
        leftButton = new IconButton("/icons/left-arrow.png", "left", e -> {
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
        centralBar.getStyle().set("width", "10%");
        centralBar.setJustifyContentMode(JustifyContentMode.CENTER);
        centralBar.add(rightButton);
        centralBar.add(leftButton);
        add(centralBar);
        add(buildAvailableItemsContainer());
    }

    private Component buildSelectedItemsContainer() {
        iconsLeftBar.setJustifyContentMode(JustifyContentMode.START);
        IconButton upArrowButton = new IconButton("/icons/up-arrow.png", "up", e -> {
            String selectedItem = selectedItems.getValue();
            Integer selectedIndex = null;
            if (selectedItem != null) {
                for (int i = 0; (i < selectedItemsModel.size()) && (selectedIndex == null); i++) {
                    if (selectedItemsModel.get(i).equals(selectedItem)) {
                        selectedIndex = i;
                    }
                }
                if ((selectedIndex != null) && (selectedIndex.intValue() > 0)) {
                    selectedItem = selectedItemsModel.remove(selectedIndex.intValue());
                    selectedItemsModel.add(selectedIndex.intValue() - 1, selectedItem);
                    selectedItems.setItems(selectedItemsModel);
                    selectedItems.setValue(selectedItem);
                }
            }
        });
        IconButton downArrowButton = new IconButton("/icons/down-arrow.png", "down", e -> {
            String selectedItem = selectedItems.getValue();
            Integer selectedIndex = null;
            if (selectedItem != null) {
                for (int i = 0; (i < selectedItemsModel.size()) && (selectedIndex == null); i++) {
                    if (selectedItemsModel.get(i).equals(selectedItem)) {
                        selectedIndex = i;
                    }
                }
                if ((selectedIndex != null) &&
                    (selectedIndex.intValue() < selectedItemsModel.size() - 1)) {
                    selectedItem = selectedItemsModel.remove(selectedIndex.intValue());
                    selectedItemsModel.add(selectedIndex.intValue() + 1, selectedItem);
                    selectedItems.setItems(selectedItemsModel);
                    selectedItems.setValue(selectedItem);
                }
            }
        });
        iconsLeftBar.add(upArrowButton);
        iconsLeftBar.add(downArrowButton);
        VerticalLayout leftBar = new VerticalLayout();
        leftBar.getStyle().set("width", "45%");
        leftBar.add(iconsLeftBar);
        leftBar.add(selectedItems);
        return leftBar;
    }

    private Component buildAvailableItemsContainer() {
        iconsRightBar.setJustifyContentMode(JustifyContentMode.END);
        IconButton sortButton = new IconButton("/icons/sort.png", "sort", e -> {
            if (availableItemsModelItemsSort == null) {
                availableItemsModelItemsSort = true;
            } else {
                availableItemsModelItemsSort = !availableItemsModelItemsSort;
            }
            if (availableItemsModelItemsSort) {
                availableItemsModel.sort(Comparator.naturalOrder());
            } else {
                availableItemsModel.sort((e1, e2) -> - e1.compareTo(e2));
            }
            availableItems.setItems(availableItemsModel);
        });
        iconsRightBar.add(sortButton);
        VerticalLayout rightBar = new VerticalLayout();
        rightBar.getStyle().set("width", "45%");
        rightBar.add(iconsRightBar);
        rightBar.add(availableItems);
        return rightBar;
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
