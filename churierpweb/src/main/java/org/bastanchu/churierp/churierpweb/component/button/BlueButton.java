package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class BlueButton extends Button {

    public BlueButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
        getStyle().set("color", "#FFFFFF");
        getStyle().set("background-color", "hsl(214, 90%, 52%)");
    }
}
