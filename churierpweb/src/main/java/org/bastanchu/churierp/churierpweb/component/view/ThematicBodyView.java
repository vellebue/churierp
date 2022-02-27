package org.bastanchu.churierp.churierpweb.component.view;

import com.vaadin.flow.component.html.Div;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodyFilterViewListener;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodyListViewListener;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public abstract class ThematicBodyView<T, F> extends BodyView
       implements ThematicBodyFilterViewListener<T, F>,
                  ThematicBodyListViewListener<T, F>,
                  ThematicBodySingleItemViewListener<T> {

    private Logger logger = LoggerFactory.getLogger(ThematicBodyView.class);

    private ThematicBodyFilterView<T, F> filterView;
    private ThematicBodyListView<T, F> listView;
    private ThematicBodySingleItemView<T> singleItemView;
    private Div mainContainer = new Div();

    public ThematicBodyView(ApplicationContext appContext) {
        super(appContext);
        mainContainer.getStyle().set("width", "100%");
        add(mainContainer);
    }

    public void setFilterView(ThematicBodyFilterView<T, F> filterView) {
        this.filterView = filterView;
    }

    public void setListView(ThematicBodyListView<T, F> listView) {
        this.listView = listView;
    }

    public void setSingleItemView(ThematicBodySingleItemView<T> singleItemView) {
        this.singleItemView = singleItemView;
    }

    @Override
    protected void onInit() {
        if (filterView != null) {
            mainContainer.add(filterView);
            filterView.setListener(this);
            filterView.fireInit();
        }
    }

    @Override
    protected void onEnding() {
        if (filterView != null) {
            filterView.fireEnd();
        }
        mainContainer.removeAll();
        /*
        if (filterView != null) {
            mainContainer.remove(filterView);
            filterView.fireEnd();
        }
        */
    }

    @Override
    public void onFilteredAction(FilterEvent<T, F> event) {
        logger.info("Filter performed");
        filterView.fireEnd();
        mainContainer.removeAll();
        if (event.getFilter() != null) {
            mainContainer.add(listView);
            listView.setFilter(event.getFilter());
            listView.setListener(this);
            listView.fireInit();
        }
        /*
        if (event.getResultList() != null && event.getResultList().size() > 1) {
            // Show list view
            mainContainer.add(listView);
            listView.setListModel(event.getResultList());
            listView.setListener(this);
            listView.fireInit();
        }
        else
        if (event.getResultList() != null && event.getResultList().size() == 1) {
            // Show single item
            mainContainer.add(singleItemView);
            singleItemView.setItemModel(event.getResultList().get(0));
            singleItemView.setListener(this);
            singleItemView.fireInit();
        }
        else {
            // TODO notify no items found
        }
        */
    }

    @Override
    public void onSelectedAction(ListEvent<T, F> event) {
        logger.info("Item selected");
        listView.fireEnd();
        mainContainer.removeAll();
        mainContainer.add(singleItemView);
        singleItemView.setItemModel(event.getItem());
        singleItemView.setListener(this);
        singleItemView.fireInit();
    }

    @Override
    public void onBackAction(SingleItemEvent<T> event) {
        logger.info("Item back");
        singleItemView.fireEnd();
        mainContainer.removeAll();
        mainContainer.add(listView);
        listView.fireInit();
    }

    @Override
    public void onRequestCreateAction(ListEvent<T, F> event) {
        logger.info("Item requested to be created");
        listView.fireEnd();
        mainContainer.removeAll();
        mainContainer.add(singleItemView);
        singleItemView.setItemModel(null);
        singleItemView.setListener(this);
        singleItemView.fireInit();
    }

    @Override
    public void onCreateAction(SingleItemEvent<T> event) {
        logger.info("Item created");
        singleItemView.fireEnd();
        mainContainer.removeAll();
        mainContainer.add(listView);
        listView.fireInit();
        listView.fireStateChanged();
    }

    @Override
    public void onUpdateAction(SingleItemEvent<T> event) {
        logger.info("Item updated");
        singleItemView.fireEnd();
        mainContainer.removeAll();
        mainContainer.add(listView);
        listView.fireInit();
        listView.fireStateChanged();
    }

}
