package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public abstract class ColoredButton extends Button {

    public ColoredButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        super(text, listener);
        getStyle().set("border-with", "thin");
        getStyle().set("color", getColor());
        getStyle().set("background-color", getBackgroundColor());
    }

    @Override
    public void onEnabledStateChanged(boolean enabled) {
        super.onEnabledStateChanged(enabled);
        if (enabled) {
            getStyle().set("color", getColor());
            getStyle().set("background-color", getBackgroundColor());
            getStyle().set("border-style", "none");
        } else {
            getStyle().set("color", getDisabledColor());
            getStyle().set("background-color", "#FFFFFF");
            getStyle().set("border-color", getDisabledBorderColor());
            getStyle().set("border-style", "solid");
        }
    }

    abstract protected String getColor();

    abstract protected String getBackgroundColor();

    abstract protected String getDisabledColor();

    abstract protected String getDisabledBorderColor();
}
