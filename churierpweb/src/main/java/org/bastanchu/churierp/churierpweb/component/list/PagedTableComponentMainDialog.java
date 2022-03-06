package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bastanchu.churierp.churierpback.util.annotation.ListField;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PagedTableComponentMainDialog extends Dialog {

    private TwoListCombo combo = new TwoListCombo();
    private MessageSource messageSource;
    private PagedTableMainDialogListener pagedTableMainDialogListener;
    // Model
    private List<Field> selectedItems;
    private List<Field> availableItems;

    @Data
    @AllArgsConstructor
    public class PagedTableMainDialogEvent {

        PagedTableComponentMainDialog source;
        private List<Field> selectedItems;
        private List<Field> availableItems;

    }

    public interface PagedTableMainDialogListener {

        public void onCloseAction(PagedTableMainDialogEvent event);

    }

    public PagedTableComponentMainDialog(List<Field> selectedItems, List<Field> availableItems, MessageSource messageSource) {
        super();
        setCloseOnOutsideClick(false);
        this.selectedItems = selectedItems;
        this.availableItems = availableItems;
        this.messageSource = messageSource;
        List<String> selectedItemsList = toColumnTitlesList(selectedItems);
        List<String> availableItemsList = toColumnTitlesList(availableItems);
        combo.setSelectedItemsModel(selectedItemsList);
        combo.setAvailableItemsModel(availableItemsList);
        add(combo);
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        PagedTableComponentMainDialog thisDialog = this;
        Button acceptButton = new Button(
                messageSource.getMessage("churierpweb.pagedtablecomponent.maindialog.button.accept",
                        null, LocaleContextHolder.getLocale()),
                e -> {
                    List<Field> newSelectedItems = toFieldList(combo.getSelectedItemsModel());
                    List<Field> newAvailableItems = toFieldList(combo.getAvailableItemsModel());
                    this.selectedItems = newSelectedItems;
                    this.availableItems = newAvailableItems;
                    thisDialog.close();
                    firePagedTableMainDialogListener();
        });
        Button cancelButton = new Button(
                messageSource.getMessage("churierpweb.pagedtablecomponent.maindialog.button.cancel",
                null, LocaleContextHolder.getLocale()),
                e -> {
                    thisDialog.close();
        });
        bottomLayout.add(acceptButton);
        bottomLayout.add(cancelButton);
        add(bottomLayout);
    }

    private List<String> toColumnTitlesList(List<Field> list) {
        return list.stream().map(e -> e.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class))
                .map(e -> e.key())
                .map(e -> messageSource.getMessage(e, null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
    }

    private List<Field> toFieldList(List<String> stringItems) {
        List<Field> allFieldsList = new ArrayList<>();
        allFieldsList.addAll(selectedItems);
        allFieldsList.addAll(availableItems);
        List<Field> matchedFields = new ArrayList<>();
        for (Field field :allFieldsList) {
            org.bastanchu.churierp.churierpback.util.annotation.Field fieldAnnotation =
                    field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class);
            String fieldAnnotationKey = fieldAnnotation.key();
            String fieldAnnotationText = messageSource.getMessage(fieldAnnotationKey, null,
                    LocaleContextHolder.getLocale());
            if (stringItems.contains(fieldAnnotationText)) {
                matchedFields.add(field);
            }
        }
        return matchedFields;
    }

    protected void firePagedTableMainDialogListener() {
        if (pagedTableMainDialogListener != null) {
            PagedTableMainDialogEvent event = new PagedTableMainDialogEvent(this,
                    selectedItems, availableItems);
            pagedTableMainDialogListener.onCloseAction(event);
        }
    }

    public void setPagedTableMainDialogListener(PagedTableMainDialogListener pagedTableMainDialogListener) {
        this.pagedTableMainDialogListener = pagedTableMainDialogListener;
    }

    public List<Field> getSelectedItems() {
        return selectedItems;
    }

    public List<Field> getAvailableItems() {
        return availableItems;
    }
}
