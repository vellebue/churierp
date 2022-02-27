package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class GreenButton extends Button {

    public GreenButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
        getStyle().set("color", "#FFFFFF");
        getStyle().set("background-color", "#048C43");
    }

}
