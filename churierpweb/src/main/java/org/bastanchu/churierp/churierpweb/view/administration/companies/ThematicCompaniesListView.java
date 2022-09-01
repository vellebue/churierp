package org.bastanchu.churierp.churierpweb.view.administration.companies;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto;

import org.bastanchu.churierp.churierpback.service.administration.CompanyService;
import org.bastanchu.churierp.churierpweb.component.button.ButtonBar;
import org.bastanchu.churierp.churierpweb.component.button.GreenButton;
import org.bastanchu.churierp.churierpweb.component.list.PagedTableComponent;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

public class ThematicCompaniesListView extends ThematicBodyListView<CompanyDto, CompanyFilterDto> {

    private Div pagedTableContainer;
    private ButtonBar buttonBar = new ButtonBar();
    private CompanyFilterDto companyFilter;
    private List<CompanyDto> listModel;
    @Autowired
    private CompanyService companyService;

    public ThematicCompaniesListView(ApplicationContext appContext) {
        super(appContext);
        setTitle(getMessageSource().getMessage("churierpweb.administration.companies.listView.title",
                null, LocaleContextHolder.getLocale())
        );
        pagedTableContainer = new Div();
        pagedTableContainer.getStyle().set("width", "100%");
        add(pagedTableContainer);
        add(buttonBar);
        buttonBar.addButton(new GreenButton(getMessageSource().getMessage("churierpweb.administration.companies.newCompany.button",
                null,
                LocaleContextHolder.getLocale()),
                e -> {
                    fireRequestedCreateItem();
                }));
        buttonBar.addButton(new Button(getMessageSource().getMessage("churierpweb.administration.companies.back.button",
                null,
                LocaleContextHolder.getLocale()),
                e -> {
                    fireBackAction();
                }));
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onEnding() {

    }

    @Override
    public void setFilter(CompanyFilterDto filter) {
        companyFilter = filter;
        List<CompanyDto> companies = companyService.filterCompanies(filter);
        setListModel(companies);
    }

    @Override
    protected CompanyFilterDto getFilter() {
        return companyFilter;
    }

    @Override
    public void setListModel(List<CompanyDto> listModel) {
        pagedTableContainer.removeAll();
        PagedTableComponent<CompanyDto> pagedTable = new PagedTableComponent<>(CompanyDto.class, getMessageSource());
        pagedTable.setItems(listModel);
        this.listModel = listModel;
        pagedTableContainer.add(pagedTable);
        pagedTable.setListener(new PagedTableComponent.PagedTableComponentListener<CompanyDto>() {

            @Override
            public void onClick(PagedTableComponent.PagedTableEvent<CompanyDto> event) {

            }

            @Override
            public void onDoubleClick(PagedTableComponent.PagedTableEvent<CompanyDto> event) {
                CompanyDto companyDto = companyService.getCompanyById(event.getTargetData().getCompanyId());
                fireSelectedItem(companyDto);
            }
        });
    }

    @Override
    protected List<CompanyDto> getListModel() {
        return null;
    }

    @Override
    protected void onStateChanged() {

    }
}
