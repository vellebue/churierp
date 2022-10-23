package org.bastanchu.churierp.churierpweb.view.administration.users;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto;
import org.bastanchu.churierp.churierpback.service.LanguageService;
import org.bastanchu.churierp.churierpback.service.administration.UserService;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Map;

public class ThematicUsersSingleItemView extends ThematicBodySingleItemView<UserDto> {

    private CustomForm<UserDto> form = null;//new CustomForm<>(new UserDto(), getMessageSource());
    private Map<String, String> languagesMap = null;
    @Autowired
    private UserService userService;
    @Autowired
    private LanguageService languageService;

    public ThematicUsersSingleItemView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.administration.users.singleItemView.title",
                null,
                LocaleContextHolder.getLocale()));
        languagesMap = languageService.getAllLanguagesMap(LocaleContextHolder.getLocale());
        UserDto userInitialModelDto = new UserDto();
        userInitialModelDto.setLanguagesMap(languagesMap);
        form = new CustomForm<>(userInitialModelDto, getMessageSource());
        form.setWidthPercentage(50.0);
        add(form);
        add(getCreateButtonBar());
    }

    private ButtonBar getUpdateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicUsersSingleItemView thisView = this;
        // Back button
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.users.back.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                    new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(this);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        // Update button
        buttonBar.addButton(new BlueButton(getMessageSource().getMessage("churierpweb.administration.users.update.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            UserDto itemInForm = new UserDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                if (validLogin(itemInForm)) {
                    thisView.setItemModel(itemInForm);
                    ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                            new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setSourceView(this);
                    event.setItem(getItemModel());
                    userService.updateUser(itemInForm);
                    fireUpdateAction(event);
                } else {
                    form.addErrorMessageKey("churierpweb.administration.users.dto.validation.loginExists",
                            new Object[]{itemInForm.getLogin()});
                }
            }
        }));
        // Delete button
        buttonBar.addButton(new RedButton(getMessageSource().getMessage("churierpweb.administration.users.delete.button",
                null,
                LocaleContextHolder.getLocale()), e -> {
            UserDto itemInForm = new UserDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader(getMessageSource().getMessage("churierpweb.administration.users.confirm.title",
                        null,
                        LocaleContextHolder.getLocale()));
                dialog.setText(getMessageSource().getMessage("churierpweb.administration.users.confirm.dialog",
                        null,
                        LocaleContextHolder.getLocale()));
                dialog.setConfirmText(getMessageSource().getMessage("churierpweb.administration.users.confirm.ok",
                        null,
                        LocaleContextHolder.getLocale()));
                dialog.setCancelText(getMessageSource().getMessage("churierpweb.administration.users.confirm.cancel",
                        null,
                        LocaleContextHolder.getLocale()));
                dialog.addConfirmListener(ce -> {
                    thisView.setItemModel(itemInForm);
                    ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                            new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setSourceView(this);
                    event.setItem(getItemModel());
                    userService.deleteUser(itemInForm);
                    fireDeleteAction(event);
                });
                dialog.setCancelable(true);
                dialog.open();
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
                if (validLogin(itemInForm)) {
                    thisView.setItemModel(itemInForm);
                    ThematicBodySingleItemViewListener.SingleItemEvent<UserDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setSourceView(this);
                    event.setItem(getItemModel());
                    userService.createUser(itemInForm);
                    fireCreateAction(event);
                } else {
                    form.addErrorMessageKey("churierpweb.administration.users.dto.validation.loginExists",
                            new Object[]{itemInForm.getLogin()});
                }
            }
        }));
        return buttonBar;
    }

    /**
     * Validates if there is another user id with the same login.
     * @param userDto userDto object to test.
     * @return <code>true</code> if there are no other user ids with the same login <code>false</code> otherwise.
     *
     */
    private boolean validLogin(UserDto userDto) {
        UserFilterDto userFilterDto = new UserFilterDto();
        userFilterDto.setLogin(userDto.getLogin());
        List<UserDto> users = userService.filterUsers(userFilterDto);
        if ((users == null) || (users.size() == 0) ||
            ((users.size() == 1) && (users.get(0).getLogin().equals(userDto.getLogin())
              && (users.get(0).getUserId().equals(userDto.getUserId()))))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onModelChanged(UserDto itemModel) {
        // Remove last component (Current active button bar)
        int numComponents = getComponentCount();
        Component component = getComponentAt(numComponents - 1);
        remove(component);
        if (itemModel != null) {
            // Update mode
            itemModel.setLanguagesMap(languagesMap);
            form.readBean(itemModel);
            add(getUpdateButtonBar());
        } else {
            // Create mode
            itemModel = new UserDto();
            itemModel.setLanguagesMap(languagesMap);
            form.readBean(itemModel);
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
