package org.bastanchu.churierp.churierpweb.component.view.listener;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpweb.component.view.ThematicBodyFilterView;

import java.util.List;

public interface ThematicBodyFilterViewListener<T, F> {

    @Data
    @NoArgsConstructor
    public static class FilterEvent<T, F> {

        ThematicBodyFilterView<T, F> sourceView;
        List<T> resultList;
        F filter;
    }

    public void onFilteredAction(FilterEvent<T, F> event);

}
