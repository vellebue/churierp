package org.bastanchu.churierp.churierpweb.view.administration.users;

import com.vaadin.flow.component.button.Button;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserFilterDto;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyFilterView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicUsersFilterView extends ThematicBodyFilterView<UserDto, UserFilterDto> {

    private Logger logger = LoggerFactory.getLogger(ThematicUsersFilterView.class);

    private UserFilterDto formModel = new UserFilterDto();

    public ThematicUsersFilterView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource()
                .getMessage("churierpweb.administration.users.filterView.title",
                        null, LocaleContextHolder.getLocale()));
        CustomForm<UserFilterDto> form = new CustomForm<>(formModel, getMessageSource());
        form.setWidthPercentage(50.0);
        ButtonBar buttonBar = new ButtonBar();
        Button button = new Button(getMessageSource().getMessage("churierpweb.administration.users.filter.button",
                null,
                LocaleContextHolder.getLocale()),
                e -> {
                    logger.info("Filtering users");
                    boolean valid = form.writeBean(formModel);
                    if (valid) {
                        fireThematicBodyFilterView(formModel);
                    }
                    logger.info("Users filtered");
                });
        add(form);
        buttonBar.addButton(button);
        add(buttonBar);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }
}
