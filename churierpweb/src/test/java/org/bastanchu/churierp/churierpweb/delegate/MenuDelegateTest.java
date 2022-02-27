package org.bastanchu.churierp.churierpweb.delegate;

import static org.junit.Assert.*;

import org.bastanchu.churierp.churierpweb.dto.MenuDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"file:./src/test/resources/applicationContext.xml"})
@WebAppConfiguration
public class MenuDelegateTest {

    @Autowired
    private MenuDelegate menuDelegate;

    @Test
    public void shouldRetrieveMenuTreeCorrectly() {
        List<MenuDto> items = menuDelegate.getMenuItems();
        assertNotNull(items);
    }

}
