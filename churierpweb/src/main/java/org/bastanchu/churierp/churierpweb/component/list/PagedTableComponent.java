package org.bastanchu.churierp.churierpweb.component.list;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bastanchu.churierp.churierpback.util.annotation.ListField;
import org.bastanchu.churierp.churierpweb.component.button.IconButton;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.vaadin.klaudeta.PaginatedGrid;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final double DEFAULT_WIDTH_PERCENTAGE = 80.0;

    private Class<T> modelClass;
    private MessageSource messageSource;
    private Div gridContainer = new Div();
    private PaginatedGrid<T, Object> grid;
    private Double widthPercentage = DEFAULT_WIDTH_PERCENTAGE;
    private PagedTableComponentListener<T> listener;
    private List<Field> displayableFields;
    private List<Field> availableFields;
    //Model
    private List<T> items;

    public PagedTableComponent(Class<T> modelClass, MessageSource messageSource) {
        this.modelClass = modelClass;
        this.messageSource = messageSource;
        //this.grid = new PaginatedGrid<T>();
        setHeadings();
        //grid.setPageSize(DEFAULT_PAGE_SIZE);
        //grid.setPaginatorSize(DEFAULT_PAGINATOR_SIZE);
        //configureGridColumns();
        //grid.addItemClickListener(this);
        add(buildIconsBar());
        //add(grid);
        add(gridContainer);
        Field[] fields = modelClass.getDeclaredFields();
        initDisplayableAvailableFields(fields);
        buildGrid();
    }

    public void buildGrid() {
        if (this.grid != null) {
            gridContainer.remove(grid);
            gridContainer.removeAll();
        }
        this.grid = new PaginatedGrid<T, Object>();
        grid.setPageSize(DEFAULT_PAGE_SIZE);
        grid.setPaginatorSize(DEFAULT_PAGINATOR_SIZE);
        configureGridColumns();
        grid.addItemClickListener(this);
        gridContainer.add(grid);
    }

    public void setItems(List<T> items) {
        this.items = items;
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

    private void initDisplayableAvailableFields(Field[] fields) {
        displayableFields = new ArrayList<>();
        availableFields = new ArrayList<>();
        for (Field field: fields) {
            ListField listFieldAnnotation = field.getAnnotation(ListField.class);
            org.bastanchu.churierp.churierpback.util.annotation.Field fieldAnnotation =
                    field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class);
                    listFieldAnnotation = field.getAnnotation(ListField.class);
            if ((fieldAnnotation != null) && (listFieldAnnotation != null) &&
                 listFieldAnnotation.selected()) {
                displayableFields.add(field);
            } else if((fieldAnnotation != null) && (listFieldAnnotation != null)) {
                availableFields.add(field);
            }
        }
    }

    private void configureGridColumns() {
        DateFormat dateFormat = new SimpleDateFormat(
                messageSource.getMessage("churierpweb.dateFormat", null, LocaleContextHolder.getLocale()));
        NumberFormat numberFormat = NumberFormat.getInstance(LocaleContextHolder.getLocale());
        //Field[] fields = modelClass.getDeclaredFields();
        //initDisplayableAvailableFields(fields);
        for(Field aField: displayableFields) {
            String columnTitleKey = getFieldTitleKey(aField);
            Grid.Column gridColumn = grid.addColumn(e -> {
                try {
                    aField.setAccessible(true);
                    Object value = aField.get(e);
                    if (value instanceof java.util.Date) {
                        Date dateValue = (Date) value;
                        String formattedValue = dateFormat.format(dateValue);
                        return formattedValue;
                    } if (value instanceof java.math.BigDecimal) {
                        BigDecimal numberValue = (BigDecimal) value;
                        String formattedValue = numberFormat.format(numberValue);
                        return formattedValue;
                    } if (value instanceof java.lang.Boolean) {
                        Boolean booleanValue = (Boolean) value;
                        if (booleanValue != null) {
                            if (booleanValue) {
                                return messageSource.getMessage("churierpweb.booleanValue.true",
                                        null, LocaleContextHolder.getLocale());
                            } else {
                                return messageSource.getMessage("churierpweb.booleanValue.false",
                                        null, LocaleContextHolder.getLocale());
                            }
                        } else {
                            return "";
                        }
                    } else {
                        return value;
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }).setHeader(messageSource.getMessage(columnTitleKey, null,
                    LocaleContextHolder.getLocale()));
            //gridColumn.setComparator(CopyUtil.Instance.getComparator(aField.getType()));
            gridColumn.setSortable(true);
        }
    }

    protected Double getWidthPercentage() {
        return widthPercentage;
    }

    public void setWidthPercentage(Double widthPercentage) {
        this.widthPercentage = widthPercentage;
        getStyle().set("width", "" + widthPercentage + "%");
    }

    private void setHeadings() {
        getStyle().set("margin-left", "auto");
        getStyle().set("margin-right", "auto");
        getStyle().set("display", "block");
        getStyle().set("width", "" + widthPercentage + "%");
    }

    private String getFieldTitleKey(Field field) {
        org.bastanchu.churierp.churierpback.util.annotation.Field fieldAnnotation =
                field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class);
        if (fieldAnnotation != null) {
            return fieldAnnotation.key();
        } else {
            return "";
        }
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
        VerticalLayout thisPagedTableComponent = this;
        Button buttonMenu = new IconButton("/icons/menu.png", "menu", e -> {
            PagedTableComponentMainDialog dialog =
                    new PagedTableComponentMainDialog(displayableFields, availableFields, messageSource);
            dialog.open();
            dialog.setPagedTableMainDialogListener(event -> {
                displayableFields = event.getSelectedItems();
                availableFields = event.getAvailableItems();
                buildGrid();
                setItems(items);
            });
            /*
            dialog.addDialogCloseActionListener(event -> {
                displayableFields = dialog.getSelectedItems();
                availableFields = dialog.getAvailableItems();
                buildGrid();
            });
             */
        });
        iconsBar.add(buttonMenu);
        return iconsBar;
    }
}
