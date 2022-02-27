package org.bastanchu.churierp.churierpweb.component.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

public class IconButton extends Button {

    public IconButton(String pathResource, String text, ComponentEventListener<ClickEvent<Button>> listener) {
        //super(text, listener);
        /*
        String [] split = pathResource.split("\\/");
        StreamResource imageResourceButton = new StreamResource(split[split.length - 1],
                () -> getClass().getResourceAsStream(pathResource));
        Image buttonImage = new Image(imageResourceButton, text);
         */
        super(getButtonImage(pathResource, text));
        this.addClickListener(listener);
    }

    private static Image getButtonImage(String pathResource, String text) {
        String [] split = pathResource.split("\\/");
        StreamResource imageResourceButton = new StreamResource(split[split.length - 1],
                () -> Thread.currentThread().getContextClassLoader().getResourceAsStream(pathResource));
        Image buttonImage = new Image(imageResourceButton, text);
        return buttonImage;
    }

}
