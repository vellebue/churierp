package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ButtonBar extends HorizontalLayout {

    private HorizontalLayout divContainer = new HorizontalLayout();

    public ButtonBar() {
        this.getStyle().set("width", "100%");
        this.getStyle().set("text-align", "center");
        divContainer.getStyle().set("display","block");
        divContainer.getStyle().set("margin-left","auto");
        divContainer.getStyle().set("margin-right","auto");
        super.add(divContainer);
    }

    public void addButton(Button button) {
        if (divContainer.getChildren().count() > 0) {
            Span paddingDiv = new Span("##");
            paddingDiv.getStyle().set("min-width", "25pt");
            paddingDiv.getStyle().set("color", "#FFFFFF");
            divContainer.add(paddingDiv);
        }
        divContainer.add(button);
    }

    @Override
    public void add(Component... components) {
        throw new UnsupportedOperationException("Not allowed to add components. Use addButton to add buttons instead.");
    }

    @Override
    public void add(String text) {
        throw new UnsupportedOperationException("Not allowed to add texts. Use addButton to add buttons instead.");
    }
}
