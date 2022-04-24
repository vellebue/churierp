package org.bastanchu.churierp.churierpweb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MenuDto {

    private String menuText;
    private String url;
    private String className;
    private String factoryClassName;
    private List<MenuDto> children = new ArrayList<>();

    public boolean isDefinedComponent() {
        return ((getClassName() != null) && !getClassName().trim().equals("")) ||
                ((getFactoryClassName() != null) && !getFactoryClassName().trim().equals(""));
    }

    public BodyView buildBodyViewComponent(ApplicationContext applicationContext)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        BodyView component = null;
        if ((getClassName() != null) && !getClassName().trim().equals("")) {
            // Retrieve component from class
            Class<? extends BodyView> viewClass = (Class<? extends BodyView>) loader.loadClass(getClassName());
            component = viewClass.getDeclaredConstructor(ApplicationContext.class).newInstance(applicationContext);
        } else {
            // Retrieve component from factory
            Class<? extends BodyViewFactory> viewClassfactory = (Class<? extends BodyViewFactory>) loader.loadClass(getFactoryClassName());
            component = viewClassfactory.getDeclaredConstructor().newInstance().build(applicationContext);
        }
        return component;
    }
}
