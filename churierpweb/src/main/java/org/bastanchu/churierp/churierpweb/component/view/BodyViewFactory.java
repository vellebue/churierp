package org.bastanchu.churierp.churierpweb.component.view;

import org.springframework.context.ApplicationContext;

public abstract class BodyViewFactory {

    public abstract BodyView build(ApplicationContext applicationContext);

}
