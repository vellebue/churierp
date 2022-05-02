package org.bastanchu.churierp.churierpweb.component.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

//@CssImport(value = "./styles/ThematicIcon.css")
public class ThematicIcon extends Div {

    private String iconText;
    private String iconColor;

    public ThematicIcon(String iconText, String iconColor) {
        this.iconText = iconText;
        this.iconColor = iconColor;
        init();
    }

    private void init() {
        //addClassName("divIcon");
        //addClassName("divInner");
        Div outerDiv = new Div();
        Div innerDiv = new Div();
        Span span = new Span();
        outerDiv.addClassName("divIcon");
        innerDiv.addClassName("divInner");
        outerDiv.add(innerDiv);
        span.setText(iconText);
        innerDiv.add(span);
        add(outerDiv);
    }
}
