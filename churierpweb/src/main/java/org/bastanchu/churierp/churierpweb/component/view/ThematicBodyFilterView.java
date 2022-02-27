package org.bastanchu.churierp.churierpweb.component.view;

import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodyFilterViewListener;
import org.springframework.context.ApplicationContext;

import java.util.List;


public abstract  class ThematicBodyFilterView<T, F> extends ThematicBodyElementView<T> {

    private ThematicBodyFilterViewListener<T, F> listener = null;

    public ThematicBodyFilterView(ApplicationContext appContext) {
        super(appContext);
    }

    public void setListener(ThematicBodyFilterViewListener<T, F> listener) {
        this.listener = listener;
    }

    protected void fireThematicBodyFilterView(F filter) {
        if (listener != null) {
            ThematicBodyFilterViewListener.FilterEvent<T, F> event = new ThematicBodyFilterViewListener.FilterEvent<>();
            event.setSourceView(this);
            event.setFilter(filter);
            listener.onFilteredAction(event);
        }
    }
}
