package org.bastanchu.churierp.churierpweb.component.view.listener;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyFilterView;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyListView;

import java.util.List;

public interface ThematicBodyListViewListener<T, F> {

    @Data
    @NoArgsConstructor
    public static class ListEvent<T, F> {

        ThematicBodyListView<T, F> sourceView;
        T item;
        F filter;
    }

    public void onSelectedAction(ListEvent<T,F> event);

    public void onRequestCreateAction(ListEvent<T,F> event);

    public void onBackAction();
}
