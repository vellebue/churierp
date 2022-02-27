package org.bastanchu.churierp.churierpweb.component.view;

import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodyListViewListener;
import org.bastanchu.churierp.churierpweb.component.view.listener.ThematicBodySingleItemViewListener;
import org.springframework.context.ApplicationContext;

public abstract class ThematicBodySingleItemView<T> extends ThematicBodyElementView<T> {

    private T itemModel;
    private ThematicBodySingleItemViewListener<T> listener;

    public ThematicBodySingleItemView(ApplicationContext appContext) {
        super(appContext);
    }

    public void setItemModel(T itemModel) {
        this.itemModel = itemModel;
        onModelChanged(itemModel);
    }

    public T getItemModel() {
        return itemModel;
    }

    public void setListener(ThematicBodySingleItemViewListener<T> listener) {
        this.listener = listener;
    }

    protected void fireBackAction(ThematicBodySingleItemViewListener.SingleItemEvent<T> event) {
        if (listener != null) {
            listener.onBackAction(event);
        }
    }

    protected void fireUpdateAction(ThematicBodySingleItemViewListener.SingleItemEvent<T> event) {
        if (listener != null) {
            listener.onUpdateAction(event);
        }
    }

    protected void fireCreateAction(ThematicBodySingleItemViewListener.SingleItemEvent<T> event) {
        if (listener != null) {
            listener.onCreateAction(event);
        }
    }

    protected abstract void onModelChanged(T itemModel);


}
