package org.bastanchu.churierp.churierpweb.view.administration.companies;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.service.administration.CompanyService;
import org.bastanchu.churierp.churierpback.service.administration.CountryService;
import org.bastanchu.churierp.churierpback.service.administration.RegionService;
import org.bastanchu.churierp.churierpweb.component.button.BlueButton;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.button.RedButton;
import org.bastanchu.churierp.churierpweb.component.form.CustomForm;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Map;

public class ThematicCompaniesSingleItemView extends ThematicBodySingleItemView<CompanyDto> {

    private CustomForm<CompanyDto> form = null;//new CustomForm<>(new CompanyDto(), getMessageSource());
    private Map<String,String> countriesMap = null;
    private Map<String,Map<String, String>> regionsMap = null;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private RegionService regionService;

    public ThematicCompaniesSingleItemView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.administration.companies.singleItemView.title",
                null,
                LocaleContextHolder.getLocale()));
        CompanyDto companyInitialFormModelDto = new CompanyDto();
        countriesMap = countryService.getCountriesMap(LocaleContextHolder.getLocale());
        regionsMap = regionService.getRegionsMap(LocaleContextHolder.getLocale());
        companyInitialFormModelDto.setCountriesMap(countriesMap);
        companyInitialFormModelDto.setRegionsMap(regionsMap);
        form = new CustomForm<>(companyInitialFormModelDto, getMessageSource());
        form.setWidthPercentage(50.0);
        add(form);
        add(getCreateButtonBar());
    }

    private ButtonBar getUpdateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicCompaniesSingleItemView thisView = this;
        // Back button
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.companies.back.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            ThematicBodySingleItemViewListener.SingleItemEvent<CompanyDto> event =
                    new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(this);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        // Update button
        buttonBar.addButton(new BlueButton(getMessageSource().getMessage("churierpweb.administration.companies.update.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            CompanyDto itemInForm = new CompanyDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                companyService.updateCompany(itemInForm);
                thisView.setItemModel(itemInForm);
                ThematicBodySingleItemViewListener.SingleItemEvent<CompanyDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                event.setSourceView(this);
                event.setItem(getItemModel());
                fireUpdateAction(event);
            }
        }));
        //Delete button
        buttonBar.addButton(new RedButton(getMessageSource().getMessage("churierpweb.administration.companies.delete.button",
                null,
                LocaleContextHolder.getLocale()), e -> {
            CompanyDto itemInForm = new CompanyDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                ConfirmDialog dialog = buildConfirmDialog();
                dialog.addConfirmListener(ce -> {
                    thisView.setItemModel(itemInForm);
                    ThematicBodySingleItemViewListener.SingleItemEvent<CompanyDto> event =
                            new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                    event.setSourceView(this);
                    event.setItem(getItemModel());
                    companyService.deleteCompany(itemInForm);
                    fireDeleteAction(event);
                });
            }
        }));
        return buttonBar;
    }

    private ButtonBar getCreateButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        ThematicCompaniesSingleItemView thisView = this;
        // Back button
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.companies.back.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            ThematicBodySingleItemViewListener.SingleItemEvent<CompanyDto> event =
                    new ThematicBodySingleItemViewListener.SingleItemEvent<>();
            event.setSourceView(this);
            event.setItem(getItemModel());
            fireBackAction(event);
        }));
        // Create button
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage("churierpweb.administration.companies.newCompany.button",
                null,
                LocaleContextHolder.getLocale()), e-> {
            CompanyDto itemInForm = new CompanyDto();
            boolean valid = form.writeBean(itemInForm);
            if (valid) {
                itemInForm = companyService.createCompany(itemInForm);
                thisView.setItemModel(itemInForm);
                ThematicBodySingleItemViewListener.SingleItemEvent<CompanyDto> event =
                        new ThematicBodySingleItemViewListener.SingleItemEvent<>();
                event.setSourceView(this);
                event.setItem(getItemModel());
                fireCreateAction(event);
            }
        }));
        return buttonBar;
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }

    @Override
    protected void onModelChanged(CompanyDto itemModel) {
        // Remove last component (Current active button bar)
        int numComponents = getComponentCount();
        Component component = getComponentAt(numComponents - 1);
        remove(component);
        if (itemModel != null) {
            // Update mode
            itemModel.setCountriesMap(countriesMap);
            itemModel.setRegionsMap(regionsMap);
            form.readBean(itemModel);
            add(getUpdateButtonBar());
        } else {
            // Create mode
            itemModel = new CompanyDto();
            itemModel.setCountriesMap(countriesMap);
            itemModel.setRegionsMap(regionsMap);
            form.readBean(itemModel);
            add(getCreateButtonBar());
        }
    }

    private ConfirmDialog buildConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(getMessageSource().getMessage("churierpweb.administration.companies.confirm.title",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setText(getMessageSource().getMessage("churierpweb.administration.companies.confirm.dialog",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setConfirmText(getMessageSource().getMessage("churierpweb.administration.companies.confirm.ok",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setCancelText(getMessageSource().getMessage("churierpweb.administration.companies.confirm.cancel",
                null,
                LocaleContextHolder.getLocale()));
        dialog.setCancelable(true);
        dialog.open();
        return dialog;
    }
}
