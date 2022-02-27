package org.bastanchu.churierp.churierpweb.delegate.impl;

import org.bastanchu.churierp.churierpweb.delegate.MenuDelegate;
import org.bastanchu.churierp.churierpweb.dto.MenuDto;
import org.bastanchu.churierp.churierpweb.dto.MenuListDto;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MenuDelegateImpl implements MenuDelegate {

    private static final String MENU_FILE = "org/bastanchu/churierp/churierpweb/delegate/menu.yaml";

    @Override
    public List<MenuDto> getMenuItems() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(MENU_FILE);
        Yaml yaml = new Yaml(new Constructor(MenuListDto.class));
        MenuListDto menuList = yaml.load(stream);
        return menuList.getList();
        //return fakeMenuItems();
    }

    private List<MenuDto> fakeMenuItems() {
        // Sales
        List<MenuDto> menuList = new ArrayList<>();
        MenuDto menuEntrySales = new MenuDto();
        menuEntrySales.setMenuText("Sales");
        MenuDto menuEntrySalesOrders = new MenuDto();
        menuEntrySalesOrders.setMenuText("Orders");
        menuEntrySales.getChildren().add(menuEntrySalesOrders);
        MenuDto menuEntrySalesOrdersCreate = new MenuDto();
        menuEntrySalesOrdersCreate.setMenuText("Create");
        menuEntrySalesOrders.getChildren().add(menuEntrySalesOrdersCreate);
        MenuDto menuEntrySalesOrdersList = new MenuDto();
        menuEntrySalesOrdersList.setMenuText("List");
        menuEntrySalesOrders.getChildren().add(menuEntrySalesOrdersList);
        menuList.add(menuEntrySales);
        // Administration
        MenuDto menuEntryAdministration = new MenuDto();
        menuEntryAdministration.setMenuText("Administration");
        MenuDto menuEntryAdministrationUsers = new MenuDto();
        menuEntryAdministrationUsers.setMenuText("Users");
        MenuDto menuEntryAdministrationUsersCreate = new MenuDto();
        menuEntryAdministrationUsersCreate.setMenuText("Create");
        menuEntryAdministrationUsers.getChildren().add(menuEntryAdministrationUsersCreate);
        MenuDto menuEntryAdministrationUsersList = new MenuDto();
        menuEntryAdministrationUsersList.setMenuText("List");
        menuEntryAdministrationUsers.getChildren().add(menuEntryAdministrationUsersList);
        menuEntryAdministration.getChildren().add(menuEntryAdministrationUsers);
        MenuDto menuEntryAdministrationCompanies = new MenuDto();
        menuEntryAdministrationCompanies.setMenuText("Companies");
        menuEntryAdministration.getChildren().add(menuEntryAdministrationCompanies);
        menuList.add(menuEntryAdministration);
        return menuList;
    }
}
