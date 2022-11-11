package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class RedButton extends ColoredButton {

    public RedButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
    }

    @Override
    protected String getColor() {
        return "#FFFFFF";
    }

    @Override
    protected String getBackgroundColor() {
        return "#FF0000";
    }

    @Override
    protected String getDisabledColor() {
        return "#808080";
    }

    @Override
    protected String getDisabledBorderColor() {
        return "#FF0000";
    }

}
