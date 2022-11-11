package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bastanchu.churierp.churierpback.dto.Copiable;
import org.bastanchu.churierp.churierpback.dto.Reseteable;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

public class ListFormComponent<T extends Reseteable & Copiable<T>> extends VerticalLayout {

    private PagedTableComponent<T> pagedTableComponent = null;
    private CustomForm<T> customForm = null;
    private ButtonBar buttonBar = null;
    private RedButton deleteButton;
    private BlueButton updateButton;
    private GreenButton insertButton;
    private GreenButton newButton;

    private T referenceInstanceEmptyModel;
    private List<T> listModel;
    private ListFormComponentListener<T> listener = null;

    public ListFormComponent(T referenceInstance, MessageSource messageSource) {
        pagedTableComponent = new PagedTableComponent<T>((Class<T>) referenceInstance.getClass(), messageSource);
        referenceInstanceEmptyModel = referenceInstance;
        customForm = new CustomForm<T>(referenceInstance, messageSource);
        customForm.setWidthPercentage(100.0);
        add(pagedTableComponent);
        add(customForm);
        buttonBar = buildButtonBar(messageSource);
        add(buttonBar);
        listModel = new ArrayList<>();
    }

    protected ListFormComponentListener<T> getListener() {
        return listener;
    }

    public void setListener(ListFormComponentListener<T> listener) {
        this.listener = listener;
    }

    public void setWidthPercentage(double widthPercentage) {
        pagedTableComponent.setWidthPercentage(widthPercentage);
        customForm.setWidthPercentage(widthPercentage);
    }

    private ButtonBar buildButtonBar(MessageSource messageSource) {
        ButtonBar buttonBar = new ButtonBar();
        updateButton = new BlueButton(messageSource.getMessage("churierpweb.listformcomponent.button.update",
                null, LocaleContextHolder.getLocale()), event -> {

        });
        deleteButton = new RedButton(messageSource.getMessage("churierpweb.listformcomponent.button.delete",
                null, LocaleContextHolder.getLocale()), event -> {

        });
        newButton = new GreenButton(messageSource.getMessage("churierpweb.listformcomponent.button.new",
                null, LocaleContextHolder.getLocale()), event -> {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            insertButton.setVisible(true);
            newButton.setVisible(false);
            customForm.readBean(referenceInstanceEmptyModel);
            customForm.setEnabled(true);
        });
        insertButton = new GreenButton(messageSource.getMessage("churierpweb.listformcomponent.button.insert",
                null, LocaleContextHolder.getLocale()), event -> {
            T instanceCopy = referenceInstanceEmptyModel.copy();
            boolean valid = customForm.writeBean(instanceCopy);
            if (valid && (listener != null)) {
                boolean inserted = listener.onInsertItem(instanceCopy);
                if (inserted) {
                    listModel.add(instanceCopy);
                    pagedTableComponent.setItems(listModel);
                    insertButton.setVisible(false);
                    newButton.setVisible(true);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    customForm.readBean(referenceInstanceEmptyModel);
                    customForm.setEnabled(false);
                }
            }
        });
        insertButton.setVisible(false);
        buttonBar.addButton(updateButton);
        buttonBar.addButton(deleteButton);
        buttonBar.addButton(newButton);
        buttonBar.addButton(insertButton);
        pagedTableComponent.setListener(new CustomTableComponentListener<T>(updateButton,
                deleteButton, insertButton, newButton, customForm ));
        customForm.setEnabled(false);
        return buttonBar;
    }

    private class CustomTableComponentListener<S extends T> implements PagedTableComponent.PagedTableComponentListener<S> {

        private BlueButton updateButton;
        private RedButton deleteButton;
        private GreenButton insertButton;
        private GreenButton newButton;
        private CustomForm<S> customForm ;

        public CustomTableComponentListener(BlueButton updateButton, RedButton deleteButton, GreenButton insertButton,
                                            GreenButton newButton, CustomForm<S> customForm) {
            this.updateButton = updateButton;
            this.deleteButton = deleteButton;
            this.insertButton = insertButton;
            this.newButton = newButton;
            this.customForm = customForm;
        }

        @Override
        public void onClick(PagedTableComponent.PagedTableEvent<S> event) {

        }

        @Override
        public void onDoubleClick(PagedTableComponent.PagedTableEvent<S> event) {
            customForm.readBean(event.getTargetData());
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
            insertButton.setVisible(false);
            newButton.setVisible(true);
            newButton.setEnabled(true);
            customForm.setEnabledButKeyFields(true);
        }
    }

}

