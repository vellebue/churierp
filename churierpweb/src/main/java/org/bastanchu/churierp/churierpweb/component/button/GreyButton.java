package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class GreyButton extends ColoredButton {

    public GreyButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
    }

    @Override
    protected String getColor() {
        return "#000000";
    }

    @Override
    protected String getBackgroundColor() {
        return "#FFFFFF";
    }

    @Override
    protected String getDisabledColor() {
        return "#808080";
    }

    @Override
    protected String getDisabledBorderColor() {
        return "#808080";
    }
}
