package org.bastanchu.churierp.churierpweb.view.accounting.taxes.vat;

import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto;
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto;
import org.bastanchu.churierp.churierpback.service.FormatContextHolder;
import org.bastanchu.churierp.churierpback.service.accounting.taxes.VatService;
import org.bastanchu.churierp.churierpback.service.administration.CountryService;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponent;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponentListener;
import org.bastanchu.churierp.churierpweb.component.tab.TabbedContainer;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VatMasterView extends BodyView {

    private TabbedContainer tabbedContainer = new TabbedContainer();
    private ListFormComponent<VatTypeDto> vatTypesListFormComponent = null;
    private ListFormComponent<VatValueDto> vatValuesListFormComponent = null;
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
            view.add(view.tabbedContainer);
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
        tabbedContainer.addTab(getMessageSource().getMessage("churierpweb.accounting.taxes.vattype.tab.title",
                null, LocaleContextHolder.getLocale()), vatTypesListFormComponent);
        Map<String,Map<String, String>> vatTypesMap = vatService.getVatTypesMap();
        VatValueDto vatValueDtoReferenceInstance = new VatValueDto();
        vatValueDtoReferenceInstance.setCountriesMap(countriesMap);
        vatValueDtoReferenceInstance.setVatTypesMap(vatTypesMap);
        vatValuesListFormComponent = new ListFormComponent<>(vatValueDtoReferenceInstance, getMessageSource());
        vatValuesListFormComponent.setWidthPercentage(100.0);
        vatValuesListFormComponent.setListener(new VatValueListener());
        vatValuesListFormComponent.loadItems();
        tabbedContainer.addTab(getMessageSource().getMessage("churierpweb.accounting.taxes.vatvalue.tab.title",
                null, LocaleContextHolder.getLocale()), vatValuesListFormComponent);
        tabbedContainer.setWidth("100%");
    }

    @Override
    protected void onEnding() {
        tabbedContainer.resetTabbedContainer();
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
            }
        }
    }

    private class VatValueListener implements ListFormComponentListener<VatValueDto> {

        @Override
        public List<VatValueDto> onLoadItems() {
            List<VatValueDto> vatValues = vatService.getVatValues();
            Map<String, String> countriesMap = countryService.getCountriesMap(LocaleContextHolder.getLocale());
            Map<String, Map<String, String>> vatTypesMap = vatService.getVatTypesMap();
            vatValues.forEach(it -> {
                it.setCountriesMap(countriesMap);
                it.setVatTypesMap(vatTypesMap);
            });
            return vatValues;
        }

        @Override
        public List<String> onInsertItem(VatValueDto item) {
            // Check if vat value exists for the same key
            VatValueDto vatValueDto = vatService.getVatValue(item.getCountryId(), item.getVatId(), item.getValidFrom());
            if (vatValueDto == null) {
                // Check if vat value overlaps previous register
                VatValueDto vatValueOverlappingDto = getVatValueOverlapping(item);
                if (vatValueOverlappingDto != null) {
                    String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vatvalue.dto.validation.invalidOverlapping",
                            new Object[]{vatValueOverlappingDto.getCountryId(), vatValueOverlappingDto.getVatId(),
                                         FormatContextHolder.instance.formatDate(vatValueOverlappingDto.getValidFrom())},
                            LocaleContextHolder.getLocale());
                    return Arrays.asList(message);
                } else {
                    vatService.createVatValue(item);
                    return null;
                }
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vatvalue.dto.validation.create",
                        new Object[]{item.getCountryId(), item.getVatId(),
                                     FormatContextHolder.instance.formatDate(item.getValidFrom())},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }
        }

        private VatValueDto getVatValueOverlapping(VatValueDto vatValueDto) {
            List<VatValueDto> vatValuesInOverlapping = vatService.getVatValueOverlapping(vatValueDto);
            if ((vatValuesInOverlapping != null) && (vatValuesInOverlapping.size() > 0)) {
                return vatValuesInOverlapping.get(0);
            } else {
                return null;
            }
        }

        @Override
        public List<String> onUpdateItem(VatValueDto item) {
            VatValueDto vatValueDto = vatService.getVatValue(item.getCountryId(), item.getVatId(), item.getValidFrom());
            if (vatValueDto != null) {
                vatService.updateVatValue(item);
                return null;
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vatvalue.dto.validation.notexists",
                        new Object[]{item.getCountryId(), item.getVatId(),
                                     FormatContextHolder.instance.formatDate(item.getValidFrom())},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }
        }

        @Override
        public List<String> onDeleteItem(VatValueDto item) {
            VatValueDto vatValueDto = vatService.getVatValue(item.getCountryId(), item.getVatId(), item.getValidFrom());
            if (vatValueDto != null) {
                vatService.deleteVatValue(item);
                return null;
            } else {
                String message = getMessageSource().getMessage("churierpweb.accounting.taxes.vatvalue.dto.validation.notexists",
                        new Object[]{item.getCountryId(), item.getVatId(),
                                FormatContextHolder.instance.formatDate(item.getValidFrom())},
                        LocaleContextHolder.getLocale());
                return Arrays.asList(message);
            }
        }
    }

}
