package org.bastanchu.churierp.churierpweb.view.accounting.taxes.vat;

import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto;
import org.bastanchu.churierp.churierpback.service.accounting.taxes.VatService;
import org.bastanchu.churierp.churierpback.service.administration.CountryService;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponent;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponentListener;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VatMasterView extends BodyView {

    private ListFormComponent<VatTypeDto> vatTypesListFormComponent = null;
    @Autowired
    private CountryService countryService;
    @Autowired
    private VatService vatService;

    public static class Factory extends BodyViewFactory  {

        @Override
        public BodyView build(ApplicationContext applicationContext) {
            VatMasterView view = new VatMasterView(applicationContext);
            view.setBodyTitle(applicationContext.getMessage("churierpweb.accounting.taxes.vat.title",
                    null, LocaleContextHolder.getLocale()));
            view.setBoydIcon(applicationContext.getMessage("churierpweb.accounting.taxes.vat.icon.text",
                    null, LocaleContextHolder.getLocale()),
                    applicationContext.getMessage("churierpweb.accounting.taxes.vat.icon.color",
                            null, LocaleContextHolder.getLocale()));
            return view;
        }
    }

    public VatMasterView(ApplicationContext appContext) {
        super(appContext);
    }

    @Override
    protected void onInit() {
        VatTypeDto vatTypeDtoReferenceInstance = new VatTypeDto();
        Map<String,String> countriesMap = countryService.getCountriesMap(LocaleContextHolder.getLocale());
        vatTypeDtoReferenceInstance.setCountriesMap(countriesMap);
        vatTypesListFormComponent = new ListFormComponent<>(vatTypeDtoReferenceInstance, getMessageSource());
        vatTypesListFormComponent.setWidthPercentage(80.0);
        vatTypesListFormComponent.setListener(new VatTypeListener());
        vatTypesListFormComponent.loadItems();
        add(vatTypesListFormComponent);
    }

    @Override
    protected void onEnding() {
        remove(vatTypesListFormComponent);
    }

    private class VatTypeListener implements ListFormComponentListener<VatTypeDto> {

        @Override
        public List<VatTypeDto> onLoadItems() {
            List<VatTypeDto> vatTypes = vatService.getVatTypes();
            Map<String, String> countriesMap = countryService.getCountriesMap(LocaleContextHolder.getLocale());
            vatTypes.forEach(it -> {
                it.setCountriesMap(countriesMap);
            });
            return vatTypes;
        }

        @Override
        public List<String> onInsertItem(VatTypeDto item) {
            VatTypeDto vatTypeDto = vatService.getVatType(item.getCountryId(), item.getVatId());
            if (vatTypeDto == null) {
                vatService.createVatType(item);
                return null;
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vattype.dto.validation.create",
                        new Object[]{item.getCountryId(), item.getVatId()},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }
        }

        @Override
        public List<String> onUpdateItem(VatTypeDto item) {
            VatTypeDto vatTypeDto = vatService.getVatType(item.getCountryId(), item.getVatId());
            if (vatTypeDto != null) {
                vatService.updateVatType(item);
                return null;
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vattype.dto.validation.notexists",
                        new Object[]{item.getCountryId(), item.getVatId()},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }
        }

        @Override
        public List<String> onDeleteItem(VatTypeDto item) { VatTypeDto vatTypeDto = vatService.getVatType(item.getCountryId(), item.getVatId());
            if (vatTypeDto != null) {
                vatService.deleteVatType(item.getCountryId(), item.getVatId());
                return null;
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vattype.dto.validation.notexists",
                        new Object[]{item.getCountryId(), item.getVatId()},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }}
    }
}
