package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class RedButton extends Button {

    public RedButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
        getStyle().set("color", "#FFFFFF");
        getStyle().set("background-color", "#FF0000");
    }
}
