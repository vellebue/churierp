package org.bastanchu.churierp.churierpweb.view.administration.companies;

import com.vaadin.flow.component.button.Button;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyFilterView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class ThematicCompaniesFilterView extends ThematicBodyFilterView<CompanyDto, CompanyFilterDto> {

    private Logger logger = LoggerFactory.getLogger(ThematicCompaniesFilterView.class);

    private CompanyFilterDto formModel = new CompanyFilterDto();

    public ThematicCompaniesFilterView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource()
                .getMessage("churierpweb.administration.companies.filterView.title",
                        null, LocaleContextHolder.getLocale()));
        CustomForm<CompanyFilterDto> form = new CustomForm<>(formModel, getMessageSource());
        form.setWidthPercentage(50.0);
        ButtonBar buttonBar = new ButtonBar();
        Button button = new Button(getMessageSource().getMessage("churierpweb.administration.companies.filter.button",
                null,
                LocaleContextHolder.getLocale()),
                e -> {
                    logger.info("Filtering companies");
                    boolean valid = form.writeBean(formModel);
                    if (valid) {
                        fireThematicBodyFilterView(formModel);
                    }
                    logger.info("Companies filtered");
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
