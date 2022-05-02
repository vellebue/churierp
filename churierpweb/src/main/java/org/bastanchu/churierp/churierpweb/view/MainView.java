package org.bastanchu.churierp.churierpweb.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.service.UserService;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.bastanchu.churierp.churierpweb.delegate.MenuDelegate;
import org.bastanchu.churierp.churierpweb.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Route("/main")
@CssImport(value = "./css/ThematicIcon.css")
public class MainView extends VerticalLayout {

    private MenuDelegate menuDelegate;
    private MessageSource messageSource;
    private ApplicationContext applicationContext;
    private UserService userService;
    private Div bodyDiv = new Div();

    public MainView(@Autowired MenuDelegate menuDelegate,
                    @Autowired UserService userService,
                    @Autowired MessageSource messageSource,
                    @Autowired ApplicationContext applicationContext) {
        this.menuDelegate = menuDelegate;
        this.messageSource = messageSource;
        this.applicationContext = applicationContext;
        this. userService = userService;
        List<UserDto> users = userService.listUsers();
        Locale locale = LocaleContextHolder.getLocale();
        Label mainLabel = new Label(messageSource.getMessage("churierpweb.mainview.mainTitle", null, locale));
        mainLabel.getStyle().set("font-size", "50pt");
        mainLabel.getStyle().set("font-style", "italic");
        mainLabel.getStyle().set("font-weight", "bold");
        mainLabel.getStyle().set("text-align", "center");
        mainLabel.getStyle().set("width", "100%");
        Div menuDiv = new Div();
        menuDiv.getStyle().set("width", "100%");
        menuDiv.getStyle().set("border-top", "1px solid #000000");
        menuDiv.getStyle().set("border-bottom", "1px solid #000000");
        MenuBar menuBar = buildMenuBar();
        add(mainLabel);
        menuDiv.add(menuBar);
        menuDiv.add(buildLeftBar());
        add(menuDiv);
        bodyDiv.getStyle().set("width", "100%");
        add(bodyDiv);
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyle().set("float","left");
        List<MenuDto> menuList = menuDelegate.getMenuItems();
        for (MenuDto item:menuList) {
            //menuBar.addItem(item.getMenuText());
            addMenuItem(menuBar, item);
        }
        return menuBar;
    }

    private void addMenuItem(MenuBar menuBar, MenuDto item) {
        try {
            MenuItem menuItem = menuBar.addItem(messageSource.getMessage(item.getMenuText(), null, LocaleContextHolder.getLocale()));
            initMenuComponent(menuItem, item);
            for (MenuDto aChild:item.getChildren()) {
                SubMenu subMenu = menuItem.getSubMenu();
                addSubItem(subMenu, aChild);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addSubItem(SubMenu subMenu, MenuDto item) {
        try {
            MenuItem menuItem = subMenu.addItem(messageSource.getMessage(item.getMenuText(), null, LocaleContextHolder.getLocale()));
            initMenuComponent(menuItem,item);
            for (MenuDto aChild:item.getChildren()) {
                addSubItem(menuItem.getSubMenu(), aChild);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initMenuComponent(MenuItem menuItem, MenuDto item) {
        try {
            if (item.isDefinedComponent()) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                final BodyView component = item.buildBodyViewComponent(applicationContext);
                menuItem.addClickListener(e -> {
                    // Remove previous body view if present
                    Stream<? extends Component> children = bodyDiv.getChildren();
                    children.forEach(element -> {
                        if (element instanceof BodyView) {
                            BodyView previousBodyView = (BodyView) element;
                            previousBodyView.end();
                            bodyDiv.remove(previousBodyView);
                        }
                    });
                    //bodyDiv.removeAll();
                    // Add new body view
                    bodyDiv.add(component);
                    component.init();
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Div buildLeftBar() {
        Div div = new Div();
        div.getStyle().set("text-decoration", "underline");
        div.getStyle().set("float", "right");
        div.getStyle().set("text-align", "left");
        div.getStyle().set("height", "36px");
        div.getStyle().set("display", "inline-flex");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLoginName = authentication.getName();
        UserDto currentUserDto = userService.getUserByLogin(userLoginName);
        Div span = new Div();
        span.getStyle().set("height", "36px");
        span.getStyle().set("padding", "5px");
        span.setText(messageSource.getMessage("churierpweb.mainview.welcome",
                null, LocaleContextHolder.getLocale()) + " " +
                currentUserDto.getName() + " " + currentUserDto.getSurname() + " ");
        Anchor anchor = new Anchor();
        anchor.getStyle().set("height", "36px");
        anchor.getStyle().set("padding", "5px");
        anchor.setText(messageSource.getMessage("churierpweb.mainview.logout", null, LocaleContextHolder.getLocale()));
        anchor.getElement().addEventListener("click", e -> {
            UI.getCurrent().getPage().setLocation("./logout");
        });
        div.add(span);
        div.add(anchor);
        return div;
    }
}
