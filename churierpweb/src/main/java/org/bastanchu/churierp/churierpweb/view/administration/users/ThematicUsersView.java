package org.bastanchu.churierp.churierpweb.view.administration.users;

import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto;
import org.bastanchu.churierp.churierpweb.component.view.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicUsersView extends FullThematicBodyView<UserDto, UserFilterDto> {

    public static class Factory extends BodyViewFactory {

        @Override
        public BodyView build(ApplicationContext applicationContext) {
            ThematicUsersView userView = new ThematicUsersView(applicationContext);
            userView.addBodyTitle("churierpweb.administration.users.mainView.title")
                    .addFilterViewClass(ThematicUsersFilterView.class)
                    .addListViewClass(ThematicUsersListView.class)
                    .addSingleItemViewClass(ThematicUsersSingleItemView.class);
            return userView;
        }
    }

    public ThematicUsersView(ApplicationContext appContext) {
        super(appContext);
        /*
        setBodyTitle(getMessageSource().getMessage("churierpweb.administration.users.mainView.title",
                null, LocaleContextHolder.getLocale()));
        ThematicUsersFilterView usersFilterView = new ThematicUsersFilterView(getApplicationContext());
        setFilterView(usersFilterView);
        ThematicUsersListView usersListView = new ThematicUsersListView(getApplicationContext());
        setListView(usersListView);
        ThematicBodySingleItemView usersSingleItemView = new ThematicUsersSingleItemView(getApplicationContext());
        setSingleItemView(usersSingleItemView);
        */
    }
}
