package org.bastanchu.churierp.churierpweb.view.accounting.accounts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto;
import org.bastanchu.churierp.churierpback.service.accounting.accounts.AccountService;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.dialog.CustomConfirmDialog;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicAccountsSingleItemView extends ThematicBodySingleItemView<AccountDto> {

    private CustomForm<AccountDto> form = null;
    @Autowired
    private AccountService accountingService;

    public ThematicAccountsSingleItemView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.accounting.accounts.singleItemView.title",
                                               null, LocaleContextHolder.getLocale()));
        AccountDto accountInitialModelDto = accountingService.buildInitializedAccountDto();
        form = new CustomForm<>(accountInitialModelDto, getMessageSource());
        form.setWidthPercentage(50.0);
        add(form);
        add(getCreateButtonBar());
    }

    private ButtonBar getCreateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicAccountsSingleItemView thisView = this;
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage(
                "churierpweb.accounting.accounts.singleItemView.button.create",
                 null, LocaleContextHolder.getLocale()),
                e -> {
                    AccountDto itemInForm = new AccountDto();
                    boolean valid = form.writeBean(itemInForm);
                    if (valid) {
                        thisView.setItemModel(itemInForm);
                        ThematicBodySingleItemViewListener.SingleItemEvent<AccountDto> event =
                                new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                        event.setSourceView(thisView);
                        event.setItem(itemInForm);
                        accountingService.createAccount(itemInForm);
                        fireCreateAction(event);
                    }
                }));
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.accounting.accounts.back.button",
                null, LocaleContextHolder.getLocale()),
                e -> {
                    ThematicBodySingleItemViewListener.SingleItemEvent<AccountDto> event =
                            new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setItem(getItemModel());
                    event.setSourceView(thisView);
                    fireBackAction(event);
                }));
        return buttonBar;
    }

    private ButtonBar getUpdateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicAccountsSingleItemView thisView = this;
        // Update Button
        buttonBar.addButton(new BlueButton(getMessageSource().getMessage(
                "churierpweb.accounting.accounts.singleItemView.button.update"
                ,null, LocaleContextHolder.getLocale()), e -> {
            AccountDto itemInForm = new AccountDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                thisView.setItemModel(itemInForm);
                ThematicBodySingleItemViewListener.SingleItemEvent<AccountDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                event.setSourceView(thisView);
                event.setItem(itemInForm);
                accountingService.updateAccount(itemInForm);
                fireUpdateAction(event);
            }
        }));
        //Delete button
        buttonBar.addButton(new RedButton(getMessageSource().getMessage(
                "churierpweb.accounting.accounts.singleItemView.button.delete"
                ,null, LocaleContextHolder.getLocale()), e -> {
            AccountDto itemInForm = new AccountDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                CustomConfirmDialog dialog = new CustomConfirmDialog(
                        getMessageSource().getMessage("churierpweb.accounting.accounts.singleItemView.dialog.headerText",
                                null, LocaleContextHolder.getLocale()),
                        getMessageSource().getMessage("churierpweb.accounting.accounts.singleItemView.dialog.contentText",
                                null, LocaleContextHolder.getLocale()));
                dialog.setCustomConfirmDialogConfirmedListener(e1 -> {
                    thisView.setItemModel(itemInForm);
                    ThematicBodySingleItemViewListener.SingleItemEvent<AccountDto>
                            event = new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setSourceView(thisView);
                    event.setItem(itemInForm);
                    accountingService.deleteAccount(itemInForm.getAccountId());
                    fireDeleteAction(event);
                });
                dialog.open();
            }
        }));
        // Back biutton
        buttonBar.addButton(new Button(getMessageSource().getMessage(
                "churierpweb.accounting.accounts.back.button"
                ,null, LocaleContextHolder.getLocale()), e -> {
            ThematicBodySingleItemViewListener.SingleItemEvent<AccountDto>
                    event = new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(thisView);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        return buttonBar;
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }

    @Override
    protected void onModelChanged(AccountDto itemModel) {
        // Remove last component (Current active button bar)
        int numComponents = getComponentCount();
        Component component = getComponentAt(numComponents - 1);
        remove(component);
        if (itemModel != null) {
            // Update mode
            accountingService.fillAccountDtoMaps(itemModel);
            form.readBean(itemModel);
            add(getUpdateButtonBar());
        } else {
            // Create model
            itemModel = accountingService.buildInitializedAccountDto();
            form.readBean(itemModel);
            add(getCreateButtonBar());
        }
    }
}
