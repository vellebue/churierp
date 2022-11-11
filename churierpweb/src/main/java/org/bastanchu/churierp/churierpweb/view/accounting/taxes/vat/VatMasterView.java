package org.bastanchu.churierp.churierpweb.view.accounting.taxes.vat;

import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto;
import org.bastanchu.churierp.churierpback.service.administration.CountryService;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponent;
import org.bastanchu.churierp.churierpweb.component.list.ListFormComponentListener;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Map;

public class VatMasterView extends BodyView {

    private ListFormComponent<VatTypeDto> vatTypesListFormComponent = null;
    @Autowired
    private CountryService countryService;

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
        add(vatTypesListFormComponent);
    }

    @Override
    protected void onEnding() {
        removeAll();
    }

    private class VatTypeListener implements ListFormComponentListener<VatTypeDto> {

        @Override
        public List<VatTypeDto> onLoadItems() {
            return null;
        }

        @Override
        public boolean onInsertItem(VatTypeDto item) {
            return true;
        }

        @Override
        public boolean onUpdateItem(VatTypeDto item) {
            return true;
        }

        @Override
        public boolean onDeleteItem(VatTypeDto item) {
            return true;
        }
    }
}
