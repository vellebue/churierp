package org.bastanchu.churierp.churierpweb.component.view;

import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodyListViewListener;
import org.springframework.context.ApplicationContext;

import java.util.List;

public abstract class ThematicBodyListView<T, F> extends ThematicBodyElementView<T> {

    private ThematicBodyListViewListener<T, F> listener;

    public ThematicBodyListView(ApplicationContext appContext) {
        super(appContext);
    }

    public void setListener(ThematicBodyListViewListener<T, F> listener) {
        this.listener = listener;
    }

    public abstract void setFilter(F filter);

    protected abstract F getFilter();

    public abstract void setListModel(List<T> listModel);

    protected abstract List<T> getListModel();

    protected abstract void onStateChanged();

    public void fireStateChanged() {
        onStateChanged();
    }

    protected void fireSelectedItem(T item) {
        if (listener != null) {
            ThematicBodyListViewListener.ListEvent<T, F> event = new ThematicBodyListViewListener.ListEvent<>();
            event.setSourceView(this);
            event.setItem(item);
            event.setFilter(getFilter());
            listener.onSelectedAction(event);
        }
    }

    protected void fireBackAction() {
        if (listener != null) {
            listener.onBackAction();
        }
    }

    protected void fireRequestedCreateItem() {
        if (listener != null) {
            ThematicBodyListViewListener.ListEvent<T, F> event = new ThematicBodyListViewListener.ListEvent<>();
            event.setSourceView(this);
            event.setFilter(getFilter());
            listener.onRequestCreateAction(event);
        }
    }
}
