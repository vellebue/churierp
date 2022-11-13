package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bastanchu.churierp.churierpback.dto.Copiable;
import org.bastanchu.churierp.churierpback.dto.Reseteable;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.util.CopyUtil;
import org.bastanchu.churierp.churierpback.util.ListKey;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListFormComponent<T extends Reseteable & Copiable<T>> extends VerticalLayout {

    private PagedTableComponent<T> pagedTableComponent = null;
    private CustomForm<T> customForm = null;
    private Div customFormContainer = new Div();
    private ButtonBar buttonBar = null;
    private RedButton deleteButton;
    private BlueButton updateButton;
    private GreenButton insertButton;
    private GreenButton newButton;
    private double widthPercentage = 80.0;

    private T referenceInstanceEmptyModel;
    private List<T> listModel;
    private ListFormComponentListener<T> listener = null;

    public ListFormComponent(T referenceInstance, MessageSource messageSource) {
        pagedTableComponent = new PagedTableComponent<T>((Class<T>) referenceInstance.getClass(), messageSource);
        pagedTableComponent.setWidthPercentage(100.0);
        referenceInstanceEmptyModel = referenceInstance;
        customForm = new CustomForm<T>(referenceInstance, messageSource);
        customForm.setWidthPercentage(100.0);
        add(pagedTableComponent);
        customFormContainer.getStyle().set("width", "100%");
        customFormContainer.add(customForm);
        add(customFormContainer);
        buttonBar = buildButtonBar(messageSource);
        add(buttonBar);
        listModel = new ArrayList<>();
    }

    public void loadItems() {
        if (listener != null) {
            listModel = listener.onLoadItems();
            pagedTableComponent.setItems(listModel);
        }
    }

    protected ListFormComponentListener<T> getListener() {
        return listener;
    }

    protected void setCustomForm(CustomForm<T> customForm) {
        this.customForm = customForm;
    }

    protected CustomForm<T> getCustomForm() {
        return customForm;
    }

    public void setListener(ListFormComponentListener<T> listener) {
        this.listener = listener;
    }

    public void setWidthPercentage(double widthPercentage) {
        this.widthPercentage = widthPercentage;
        pagedTableComponent.setWidthPercentage(widthPercentage);
        customForm.setWidthPercentage(100.0);
        customFormContainer.getStyle().set("width", "100%");
    }

    private ButtonBar buildButtonBar(MessageSource messageSource) {
        ButtonBar buttonBar = new ButtonBar();
        updateButton = new BlueButton(messageSource.getMessage("churierpweb.listformcomponent.button.update",
                null, LocaleContextHolder.getLocale()), event -> {
            T instanceCopy = referenceInstanceEmptyModel.copy();
            boolean valid = customForm.writeBean(instanceCopy);
            if (valid && (listener != null)) {
                List<String> errors = listener.onUpdateItem(instanceCopy);
                if ((errors == null) || (errors.size() == 0)) {
                    T listEntry = getListModelEntry(instanceCopy);
                    CopyUtil.Instance.copyValues(instanceCopy, listEntry);
                    pagedTableComponent.setItems(listModel);
                    insertButton.setVisible(false);
                    newButton.setVisible(true);
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    customForm.readBean(referenceInstanceEmptyModel);
                    customForm.setEnabled(false);
                } else {
                    errors.forEach(it -> {
                        customForm.addErrorMessageText(it);
                    });
                }
            }
        });
        deleteButton = new RedButton(messageSource.getMessage("churierpweb.listformcomponent.button.delete",
                null, LocaleContextHolder.getLocale()), event -> {
            T instanceCopy = referenceInstanceEmptyModel.copy();
            customForm.writeBean(instanceCopy);
            if (listener != null) {
                ConfirmDialog dialog = buildConfirmDialog(messageSource);
                dialog.addConfirmListener(dialogEvent -> {
                    List<String> errors = listener.onDeleteItem(instanceCopy);
                    if ((errors == null) || (errors.size() == 0)) {
                        deleteListModel(instanceCopy);
                        pagedTableComponent.setItems(listModel);
                        insertButton.setVisible(false);
                        newButton.setVisible(true);
                        updateButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        customForm.readBean(referenceInstanceEmptyModel);
                        customForm.setEnabled(false);
                    } else {
                        errors.forEach(it -> {
                            customForm.addErrorMessageText(it);
                        });
                    }
                });
            }
        });
        newButton = new GreenButton(messageSource.getMessage("churierpweb.listformcomponent.button.new",
                null, LocaleContextHolder.getLocale()), event -> {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            insertButton.setVisible(true);
            newButton.setVisible(false);
            customFormContainer.removeAll();
            customForm = new CustomForm<>(referenceInstanceEmptyModel, messageSource);
            customFormContainer.add(customForm);
            customForm.setEnabled(true);
        });
        insertButton = new GreenButton(messageSource.getMessage("churierpweb.listformcomponent.button.insert",
                null, LocaleContextHolder.getLocale()), event -> {
            T instanceCopy = referenceInstanceEmptyModel.copy();
            boolean valid = customForm.writeBean(instanceCopy);
            if (valid && (listener != null)) {
                List<String> errors = listener.onInsertItem(instanceCopy);
                if ((errors == null) || (errors.size() == 0)) {
                    listModel.add(instanceCopy);
                    pagedTableComponent.setItems(listModel);
                    insertButton.setVisible(false);
                    newButton.setVisible(true);
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    customForm.readBean(referenceInstanceEmptyModel);
                    customForm.setEnabled(false);
                } else {
                    errors.forEach(it -> {
                        customForm.addErrorMessageText(it);
                    });
                }
            }
        });
        insertButton.setVisible(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        buttonBar.addButton(updateButton);
        buttonBar.addButton(deleteButton);
        buttonBar.addButton(newButton);
        buttonBar.addButton(insertButton);
        pagedTableComponent.setListener(new CustomTableComponentListener<T>(updateButton,
                deleteButton, insertButton, newButton, customForm, messageSource ));
        customForm.setEnabled(false);
        return buttonBar;
    }

    private T getListModelEntry(T sourceEntry) {
        ListKey sourceKey = ListKey.Instance.buildListKey(sourceEntry);
        Optional<T> targetEntryOptional = listModel.stream().filter(it -> {
            ListKey targetPotentialKey = ListKey.Instance.buildListKey(it);
            return sourceKey.equals(targetPotentialKey);
        }).findFirst();
        return targetEntryOptional.get();
    }

    private boolean deleteListModel(T sourceEntry) {
        boolean deleted = false;
        ListKey sourceKey = ListKey.Instance.buildListKey(sourceEntry);
        for (int i = 0 ; (i < listModel.size()) && !deleted ; i++) {
            ListKey currentKey = ListKey.Instance.buildListKey(listModel.get(i));
            if (sourceKey.equals(currentKey)) {
                listModel.remove(i);
                deleted = true;
            }
        }
        return deleted;
    }

    private ConfirmDialog buildConfirmDialog(MessageSource messageSource) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(messageSource.getMessage("churierpweb.listformcomponent.delete.dialog.title",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setText(messageSource.getMessage("churierpweb.listformcomponent.delete.dialog.dialog",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setConfirmText(messageSource.getMessage("churierpweb.listformcomponent.delete.dialog.ok",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setCancelText(messageSource.getMessage("churierpweb.listformcomponent.delete.dialog.cancel",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setCancelable(true);
        dialog.open();
        return dialog;
    }

    private class CustomTableComponentListener<S extends T> implements PagedTableComponent.PagedTableComponentListener<S> {

        private BlueButton updateButton;
        private RedButton deleteButton;
        private GreenButton insertButton;
        private GreenButton newButton;
        private CustomForm<S> customForm ;
        private MessageSource messageSource;

        public CustomTableComponentListener(BlueButton updateButton, RedButton deleteButton, GreenButton insertButton,
                                            GreenButton newButton, CustomForm<S> customForm, MessageSource messageSource) {
            this.updateButton = updateButton;
            this.deleteButton = deleteButton;
            this.insertButton = insertButton;
            this.newButton = newButton;
            this.customForm = customForm;
            this.messageSource = messageSource;
        }

        @Override
        public void onClick(PagedTableComponent.PagedTableEvent<S> event) {

        }

        @Override
        public void onDoubleClick(PagedTableComponent.PagedTableEvent<S> event) {
            getCustomForm().readBean(event.getTargetData());
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
            insertButton.setVisible(false);
            newButton.setVisible(true);
            newButton.setEnabled(true);
            customFormContainer.removeAll();
            setCustomForm(new CustomForm<>(event.getTargetData(), messageSource, true));
            getCustomForm().setWidthPercentage(100.0);
            getCustomForm().readBean(event.getTargetData());
            customFormContainer.add(getCustomForm());
        }
    }

}

