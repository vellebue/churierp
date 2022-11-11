package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class GreenButton extends ColoredButton {

    public GreenButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
    }

    @Override
    protected String getColor() {
        return "#FFFFFF";
    }

    @Override
    protected String getBackgroundColor() {
        return "#048C43";
    }

    @Override
    protected String getDisabledColor() {
        return "#808080";
    }

    @Override
    protected String getDisabledBorderColor() {
        return "#048C43";
    }

}
