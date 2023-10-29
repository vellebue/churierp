package org.bastanchu.churierp.churierpweb.view.accounting.accounts;

import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto;
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountFilterDto;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.bastanchu.churierp.churierpweb.component.view.FullThematicBodyView;
import org.springframework.context.ApplicationContext;

public class ThematicAccountsView extends FullThematicBodyView<AccountDto, AccountFilterDto> {

    public static class Factory extends BodyViewFactory {

        @Override
        public BodyView build(ApplicationContext applicationContext) {
            ThematicAccountsView accountsView = new ThematicAccountsView(applicationContext);
            accountsView.addBodyTitle("churierpweb.accounting.accounts.mainView.title")
                     .addBodyIcon("churierpweb.accounting.accounts.mainView.icon.text",
                                  "churierpweb.accounting.accounts.mainView.icon.color")
            .addFilterViewClass(ThematicAccountsFilterView.class)
            .addListViewClass(ThematicAccountsListView.class)
            .addSingleItemViewClass(ThematicAccountsSingleItemView.class);
            return accountsView;
        }
    }

    public ThematicAccountsView(ApplicationContext appContext) {
        super(appContext);
    }
}
