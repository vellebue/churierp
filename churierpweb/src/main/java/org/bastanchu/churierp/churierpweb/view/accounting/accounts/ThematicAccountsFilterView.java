package org.bastanchu.churierp.churierpweb.view.accounting.accounts;

import com.vaadin.flow.component.button.Button;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto;
import org.bastanchu.churierp.churierpback.service.accounting.accounts.AccountService;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyFilterView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicAccountsFilterView extends ThematicBodyFilterView<AccountDto, AccountFilterDto> {

    private Logger logger = LoggerFactory.getLogger(ThematicAccountsFilterView.class);

    @Autowired
    private AccountService accountService;

    private AccountFilterDto formModel = null;

    public ThematicAccountsFilterView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.accounting.accounts.filterView.title",
                 null, LocaleContextHolder.getLocale()));
        formModel = accountService.buildInitializedAccountFilterDto();
        CustomForm<AccountFilterDto> form = new CustomForm<>(formModel, getMessageSource());
        form.setWidthPercentage(60.0);
        add(form);
        ButtonBar buttonBar = new ButtonBar();
        Button button = new Button(getMessageSource().getMessage("churierpweb.administration.accounts.filter.button",
                            null, LocaleContextHolder.getLocale()),
                                   e -> {
                logger.info("Filtering accounts");
                boolean valid = form.writeBean(formModel);
                if (valid) {
                    fireThematicBodyFilterView(formModel);
                }
                logger.info("Accounts filtered");
        });
        buttonBar.addButton(button);
        add(buttonBar);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }
}
