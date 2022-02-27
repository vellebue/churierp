package org.bastanchu.churierp.churierpweb.component;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpweb.component.button.IconButton;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.klaudeta.PaginatedGrid;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PagedTableComponent<T> extends VerticalLayout
       implements ComponentEventListener<ItemClickEvent<T>>  {

    public enum EventType{CLICK, DOUBLE_CLICK};

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedTableEvent<T> {

        private PagedTableComponent<T> source;
        private EventType eventType;
        private T targetData;
    }

    public interface PagedTableComponentListener<T> {

        public void onClick(PagedTableEvent<T> event);

        public void onDoubleClick(PagedTableEvent<T> event);

    }

    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_PAGINATOR_SIZE = 3;
    private static final int DEFAULT_WIDTH_PERCENTAGE = 80;

    private Class<T> modelClass;
    private PaginatedGrid<T> grid;
    private PagedTableComponentListener<T> listener;

    public PagedTableComponent(Class<T> modelClass, MessageSource messageSource) {
        this.modelClass = modelClass;
        this.grid = new PaginatedGrid<T>();
        setHeadings();
        grid.setPageSize(DEFAULT_PAGE_SIZE);
        grid.setPaginatorSize(DEFAULT_PAGINATOR_SIZE);
        configureGridColumns(messageSource);
        grid.addItemClickListener(this);
        add(buildIconsBar());
        add(grid);
    }

    public void setItems(List<T> items) {
        grid.setItems(items);
        //this.grid.setColumns("name", "surname");
        grid.getDataProvider().refreshAll();
    }

    public void setPageSize(int pageSize) {
        grid.setPageSize(pageSize);
    }

    public void setPaginatorSize(int paginatorSize) {
        grid.setPaginatorSize(paginatorSize);
    }

    public void setListener(PagedTableComponentListener<T> listener) {
        this.listener = listener;
    }

    private void configureGridColumns(MessageSource messageSource) {
        DateFormat dateFormat = new SimpleDateFormat(
                messageSource.getMessage("churierpweb.dateFormat", null, LocaleContextHolder.getLocale()));
        Field[] fields = modelClass.getDeclaredFields();
        for(Field aField: fields) {
            if (aField.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class) != null) {
                org.bastanchu.churierp.churierpback.util.annotation.Field fieldAnnotation =
                        aField.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class);
                if (fieldAnnotation.visible()) {
                    Grid.Column gridColumn = grid.addColumn(e -> {
                        try {
                            aField.setAccessible(true);
                            Object value = aField.get(e);
                            if (value instanceof java.util.Date) {
                                Date dateValue = (Date) value;
                                String formattedValue = dateFormat.format(dateValue);
                                return formattedValue;
                            } else {
                                return value;
                            }
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }).setHeader(messageSource.getMessage(fieldAnnotation.key(), null, LocaleContextHolder.getLocale()));
                    //gridColumn.setComparator(CopyUtil.Instance.getComparator(aField.getType()));
                    gridColumn.setSortable(true);
                }
            }
        }
    }

    private void setHeadings() {
        getStyle().set("margin-left", "auto");
        getStyle().set("margin-right", "auto");
        getStyle().set("display", "block");
        getStyle().set("width", "" + DEFAULT_WIDTH_PERCENTAGE + "%");
    }

    @Override
    public void onComponentEvent(ItemClickEvent<T> itemClickEvent) {
        if (listener != null) {
            if (itemClickEvent.getClickCount() == 1) {
                listener.onClick(new PagedTableEvent<>(this, EventType.CLICK, itemClickEvent.getItem()));
            }
            if (itemClickEvent.getClickCount() > 1) {
                listener.onDoubleClick(new PagedTableEvent<>(this, EventType.DOUBLE_CLICK, itemClickEvent.getItem()));
            }
        }
    }

    private Div buildIconsBar() {
        Div iconsBar = new Div();
        iconsBar.getStyle().set("background-color", "#EFEFEF");
        /*
        StreamResource imageResourceMenu = new StreamResource("menu.png",
                () -> getClass().getResourceAsStream("/icons/menu.png"));
        Image imageMenu = new Image(imageResourceMenu, "Menu");
        */
        Button buttonMenu = new IconButton("/icons/menu.png", "menu", e -> {

        });
        iconsBar.add(buttonMenu);
        return iconsBar;
    }
}
