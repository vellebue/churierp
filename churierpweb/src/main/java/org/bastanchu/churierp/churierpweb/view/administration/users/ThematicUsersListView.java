package org.bastanchu.churierp.churierpweb.view.administration.users;

import com.vaadin.flow.component.html.Div;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto;
import org.bastanchu.churierp.churierpback.service.UserService;
import org.bastanchu.churierp.churierpweb.component.PagedTableComponent;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

public class ThematicUsersListView extends ThematicBodyListView<UserDto, UserFilterDto> {

    //private PagedTableComponent<UserDto> pagedTable;

    private Div pagedTableContainer;
    private ButtonBar buttonBar = new ButtonBar();
    private List<UserDto> listModel;
    private UserFilterDto filter;
    @Autowired
    private UserService userService;

    public ThematicUsersListView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.administration.users.listView.title",
                null, LocaleContextHolder.getLocale())
        );
        pagedTableContainer = new Div();
        pagedTableContainer.getStyle().set("width", "100%");
        add(pagedTableContainer);
        add(buttonBar);
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage("churierpweb.administration.users.newUser.button",
                null,
                LocaleContextHolder.getLocale()),
                e -> {
                    fireRequestedCreateItem();
        }));
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }

    @Override
    public void setFilter(UserFilterDto filter) {
        this.filter = filter;
        List<UserDto> usersList = userService.filterUsers(filter);
        setListModel(usersList);
    }

    @Override
    protected UserFilterDto getFilter() {
        return filter;
    }

    @Override
    public void setListModel(List<UserDto> listModel) {
        pagedTableContainer.removeAll();
        PagedTableComponent<UserDto> pagedTable = new PagedTableComponent<>(UserDto.class, getMessageSource());
        pagedTable.setItems(listModel);
        this.listModel = listModel;
        pagedTableContainer.add(pagedTable);
        pagedTable.setListener(new PagedTableComponent.PagedTableComponentListener<UserDto>() {

            @Override
            public void onClick(PagedTableComponent.PagedTableEvent<UserDto> event) {

            }

            @Override
            public void onDoubleClick(PagedTableComponent.PagedTableEvent<UserDto> event) {
                UserDto user = userService.getUserById(event.getTargetData().getUserId());
                fireSelectedItem(user);
            }
        });
    }

    @Override
    protected List<UserDto> getListModel() {
        return listModel;
    }

    @Override
    protected void onStateChanged() {
        if (getFilter() != null) {
            setFilter(getFilter());
        }
    }
}
