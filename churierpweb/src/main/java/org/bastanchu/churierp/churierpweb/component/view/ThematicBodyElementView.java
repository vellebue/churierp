package org.bastanchu.churierp.churierpweb.component.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class ThematicBodyElementView<T> extends VerticalLayout implements Autowirable {

    private ApplicationContext applicationContext;
    @Autowired
    private MessageSource messageSource;
    private Label bodyLabel = new Label();

    public ThematicBodyElementView(ApplicationContext appContext) {
        applicationContext = appContext;
        autowireComponents(applicationContext);
        buildTitleDiv();
    }

    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    private void buildTitleDiv() {
        Div divLabel = new Div();
        divLabel.getStyle().set("width", "100%");
        divLabel.getStyle().set("text-align", "center");
        bodyLabel.getStyle().set("font-size", "18pt");
        bodyLabel.getStyle().set("font-style", "normal");
        bodyLabel.getStyle().set("font-weight", "bold");
        divLabel.add(bodyLabel);
        add(divLabel);
    }

    public void setTitle(String title) {
        bodyLabel.setText(title);
    }

    protected Label getBodyLabel() {
        return bodyLabel;
    }

    public void fireInit() {
        onInit();
    }

    public void fireEnd() {
        onEnding();
    }

    protected abstract void onInit();

    protected abstract void onEnding();
}
