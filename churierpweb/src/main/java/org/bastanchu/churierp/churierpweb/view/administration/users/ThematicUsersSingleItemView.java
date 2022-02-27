package org.bastanchu.churierp.churierpweb.view.administration.users;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.service.UserService;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicUsersSingleItemView extends ThematicBodySingleItemView<UserDto> {

    private CustomForm<UserDto> form = new CustomForm<>(new UserDto(), getMessageSource());
    @Autowired
    private UserService userService;

    public ThematicUsersSingleItemView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.administration.users.singleItemView.title",
                null,
                LocaleContextHolder.getLocale()));
        form.setWidthPercentage(50.0);
        add(form);
        add(getCreateButtonBar());
    }

    private ButtonBar getUpdateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicUsersSingleItemView thisView = this;
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.users.back.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                    new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(this);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        buttonBar.addButton(new BlueButton(getMessageSource().getMessage("churierpweb.administration.users.update.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            UserDto itemInForm = new UserDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                thisView.setItemModel(itemInForm);
                ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                event.setSourceView(this);
                event.setItem(getItemModel());
                userService.updateUser(itemInForm);
                fireUpdateAction(event);
            }
        }));
        return buttonBar;
    }

    private ButtonBar getCreateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicUsersSingleItemView thisView = this;
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.users.back.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                    new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(this);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage("churierpweb.administration.users.create.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            UserDto itemInForm = new UserDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                thisView.setItemModel(itemInForm);
                ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                event.setSourceView(this);
                event.setItem(getItemModel());
                userService.createUser(itemInForm);
                fireUpdateAction(event);
            }
        }));
        return buttonBar;
    }

    @Override
    protected void onModelChanged(UserDto itemModel) {
        // Remove last component (Current active button bar)
        int numComponents = getComponentCount();
        Component component = getComponentAt(numComponents - 1);
        remove(component);
        if (itemModel != null) {
            // Update mode
            form.readBean(itemModel);
            add(getUpdateButtonBar());
        } else {
            // Create mode
            form.readBean(new UserDto());
            add(getCreateButtonBar());
        }

    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }
}
