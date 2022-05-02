package org.bastanchu.churierp.churierpweb.component.view;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class FullThematicBodyView<T,F> extends ThematicBodyView<T, F> {

    public FullThematicBodyView(ApplicationContext appContext) {
        super(appContext);
    }

    public FullThematicBodyView<T,F> addBodyTitle(String bodyTitleKey) {
        String title = getMessageSource().getMessage(bodyTitleKey,
                null, LocaleContextHolder.getLocale());
        setBodyTitle(title);
        return this;
    }

    public FullThematicBodyView<T,F> addBodyIcon(String textKey, String colorKey) {
        String iconText = getMessageSource().getMessage(textKey, null , LocaleContextHolder.getLocale());
        String iconColor = getMessageSource().getMessage(colorKey, null, LocaleContextHolder.getLocale());
        setBoydIcon(iconText, iconColor);
        return this;
    }

    public FullThematicBodyView<T,F> addFilterViewClass(Class<? extends ThematicBodyFilterView<T,F>> filterViewClass) {
        try {
            ThematicBodyFilterView<T, F> filterView = filterViewClass.getConstructor(ApplicationContext.class).newInstance(getApplicationContext());
            setFilterView(filterView);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FullThematicBodyView<T,F> addListViewClass(Class<? extends ThematicBodyListView<T,F>> listViewClass) {
        try {
            ThematicBodyListView<T, F> listView = listViewClass.getConstructor(ApplicationContext.class).newInstance(getApplicationContext());
            setListView(listView);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FullThematicBodyView<T,F> addSingleItemViewClass(Class<? extends ThematicBodySingleItemView<T>> singleItemViewClass) {
        try {
            ThematicBodySingleItemView<T> singleItemView = singleItemViewClass.getConstructor(ApplicationContext.class).newInstance(getApplicationContext());
            setSingleItemView(singleItemView);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
