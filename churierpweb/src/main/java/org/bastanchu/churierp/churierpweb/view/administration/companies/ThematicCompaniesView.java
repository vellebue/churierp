package org.bastanchu.churierp.churierpweb.view.administration.companies;

import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto;
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto;
import org.bastanchu.churierp.churierpweb.component.view.BodyView;
import org.bastanchu.churierp.churierpweb.component.view.BodyViewFactory;
import org.bastanchu.churierp.churierpweb.component.view.FullThematicBodyView;
import org.springframework.context.ApplicationContext;

public class ThematicCompaniesView extends FullThematicBodyView<CompanyDto, CompanyFilterDto> {

    public static class Factory extends BodyViewFactory {

        @Override
        public BodyView build(ApplicationContext applicationContext) {
            ThematicCompaniesView view = new ThematicCompaniesView(applicationContext);
            view.addBodyTitle("churierpweb.administration.companies.mainView.title");
            return view;
        }
    }

    public ThematicCompaniesView(ApplicationContext appContext) {
        super(appContext);
    }
}
