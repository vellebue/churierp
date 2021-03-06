package org.bastanchu.churierp.churierpweb.component.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BodyView extends VerticalLayout implements Autowirable {

    private Label bodyLabel = new Label();
    private Div iconContainer = new Div();
    @Autowired
    private MessageSource messageSource;
    private ApplicationContext applicationContext;

    public BodyView(ApplicationContext appContext) {
        applicationContext = appContext;
        autowireComponents(applicationContext);
        buildBodyLabel();
    }

    protected void setBodyTitle(String title) {
        bodyLabel.setText(title);
    }

    protected void setBoydIcon(String iconText, String iconColor) {
        ThematicIcon icon = new ThematicIcon(iconText, iconColor);
        iconContainer.getStyle().set("width","fit-content");
        iconContainer.add(icon);
    }

    protected MessageSource getMessageSource() {
        return messageSource;
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void init() {
        onInit();
    }

    public void end() {
        onEnding();
    }

    protected abstract void onInit();

    protected abstract void onEnding();

    private void buildBodyLabel() {
        Div divLabel = new Div();
        divLabel.getStyle().set("width", "100%");
        divLabel.getStyle().set("text-align", "center");
        divLabel.getStyle().set("display","flex");
        divLabel.getStyle().set("flex-direction","row");
        Div labelContainer = new Div();
        labelContainer.getStyle().set("display", "inline-block");
        labelContainer.getStyle().set("width", "100%");
        bodyLabel.getStyle().set("font-size", "24pt");
        bodyLabel.getStyle().set("font-style", "normal");
        bodyLabel.getStyle().set("font-weight", "bold");
        bodyLabel.getStyle().set("border-width", "thin");
        bodyLabel.getStyle().set("border-top-style", "inset");
        bodyLabel.getStyle().set("border-bottom-style", "inset");
        bodyLabel.getStyle().set("border-color", "#000000");
        bodyLabel.getStyle().set("background-color", "#E0E0E0");
        divLabel.add(iconContainer);
        divLabel.add(labelContainer);
        labelContainer.add(bodyLabel);
        add(divLabel);
    }
}
