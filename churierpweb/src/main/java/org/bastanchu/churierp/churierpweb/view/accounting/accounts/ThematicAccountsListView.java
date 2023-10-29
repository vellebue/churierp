package org.bastanchu.churierp.churierpweb.view.accounting.accounts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto;
import org.bastanchu.churierp.churierpback.service.accounting.accounts.AccountService;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.list.PagedTableComponent;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyListView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

public class ThematicAccountsListView extends ThematicBodyListView<AccountDto, AccountFilterDto> {

    private Div pagedTableContainer;
    private ButtonBar buttonBar = new ButtonBar();
    private List<AccountDto> listModel = null;
    private AccountFilterDto filter;
    @Autowired
    private AccountService accountService;

    public ThematicAccountsListView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.accounting.accounts.listView.title",
                null, LocaleContextHolder.getLocale()));
        pagedTableContainer = new Div();
        pagedTableContainer.getStyle().set("width", "100%");
        add(pagedTableContainer);
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage("churierpweb.accounting.accounts.newAccount.button",
                                                                    null, LocaleContextHolder.getLocale()),
                                      e -> {
                                        fireRequestedCreateItem();
                                      }));
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.accounting.accounts.back.button",
                null, LocaleContextHolder.getLocale()),
                e -> {
                    fireBackAction();
                }));
        add(buttonBar);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }

    @Override
    public void setFilter(AccountFilterDto filter) {
        this.filter = filter;
        List<AccountDto> accountsList = accountService.filterAccounts(filter);
        setListModel(accountsList);
    }

    @Override
    protected AccountFilterDto getFilter() {
        return filter;
    }

    @Override
    public void setListModel(List<AccountDto> listModel) {
        pagedTableContainer.removeAll();
        PagedTableComponent<AccountDto> pagedTable = new PagedTableComponent<>(AccountDto.class, getMessageSource());
        pagedTable.setItems(listModel);
        this.listModel = listModel;
        pagedTableContainer.add(pagedTable);
        pagedTable.setListener(new PagedTableComponent.PagedTableComponentListener<AccountDto>() {

            @Override
            public void onClick(PagedTableComponent.PagedTableEvent<AccountDto> event) {

            }

            @Override
            public void onDoubleClick(PagedTableComponent.PagedTableEvent<AccountDto> event) {
                AccountDto accountDto = accountService.getAccountById(event.getTargetData().getAccountId());
                fireSelectedItem(accountDto);
            }
        });
    }

    @Override
    protected List<AccountDto> getListModel() {
        return null;
    }

    @Override
    protected void onStateChanged() {
        if (getFilter() != null) {
            setFilter(getFilter());
        }
    }
}
