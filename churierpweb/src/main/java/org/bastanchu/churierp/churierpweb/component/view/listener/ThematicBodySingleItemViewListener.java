package org.bastanchu.churierp.churierpweb.component.view.listener;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodySingleItemView;

public interface ThematicBodySingleItemViewListener<T> {

    @Data
    @NoArgsConstructor
    public static class SingleItemEvent<T> {

        ThematicBodySingleItemView<T> sourceView;
        T item;
    }

    public void onBackAction(SingleItemEvent<T> event);

    public void onUpdateAction(SingleItemEvent<T> event);

    public void onCreateAction(SingleItemEvent<T> event);

    public void onDeleteAction(SingleItemEvent<T> event);
}
