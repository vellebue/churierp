package org.bastanchu.churierp.churierpweb.view.administration.users;

import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicUsersView extends ThematicBodyView<UserDto, UserFilterDto> {

    public ThematicUsersView(ApplicationContext appContext) {
        super(appContext);
        setBodyTitle(getMessageSource().getMessage("churierpweb.administration.users.mainView.title",
                null, LocaleContextHolder.getLocale()));
        ThematicUsersFilterView usersFilterView = new ThematicUsersFilterView(getApplicationContext());
        setFilterView(usersFilterView);
        ThematicUsersListView usersListView = new ThematicUsersListView(getApplicationContext());
        setListView(usersListView);
        ThematicBodySingleItemView usersSingleItemView = new ThematicUsersSingleItemView(getApplicationContext());
        setSingleItemView(usersSingleItemView);
    }
}
