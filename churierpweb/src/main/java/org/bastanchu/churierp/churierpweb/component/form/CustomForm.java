package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.formlayout.FormLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration;
import org.bastanchu.churierp.churierpback.util.annotation.FormField;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class CustomForm<T> extends FormLayout {

    private Logger logger = LoggerFactory.getLogger(CustomForm.class);
    private Validator validator;
    private Class<T> beanClass;
    private BeanValidationBinder<T> binderValidator;
    private Binder<T> binderReader;
    /**
     * This map links each field (by its property name) with its corresponding displaying
     * component in this form.
     */
    private Map<String, Component> formComponentsMap = new HashMap<>();
    /**
     * This map links each hidden field (by its property name) with its value in this form
     */
    private Map<String, Object> formHiddenValuesMap = new HashMap<>();
    private MessageSource messageSource;
    private VerticalLayout errorsBox = new VerticalLayout();
    /**
     * <code>true</code> if key fields annotated with @ListField are read only fields
     * <code>false</code> otherwise.
     */
    private boolean readOnlyKeys;

    @Data
    @AllArgsConstructor
    static class FieldEntry implements Comparable<CustomForm.FieldEntry>{

        private Object objectBean;
        private FormField formField;
        private String fieldLabel;
        private Class<?> fieldType;
        private Integer colSpan;
        private ComboBoxConfiguration comboBoxConfiguration;
        private Double widthPercentage;
        private Field field;
        private boolean listKeyField;

        public boolean equals(Object o) {
            if (o instanceof CustomForm.FieldEntry) {
                CustomForm.FieldEntry entry = (CustomForm.FieldEntry) o;
                return formField.indexInGroup() == entry.getFormField().indexInGroup();
            } else {
                return false;
            }
        }

        public int hashCode() {
            return formField.indexInGroup();
        }

        @Override
        public int compareTo(@NotNull CustomForm.FieldEntry o) {
            return this.formField.indexInGroup() - o.formField.indexInGroup();
        }
    }

    public CustomForm(T bean, MessageSource messageSource) {
        this(bean, messageSource, false);
    }

    public CustomForm(T bean, MessageSource messageSource, boolean readOnlyKeys) {
        this.readOnlyKeys = readOnlyKeys;
        this.getStyle().set("display","block");
        this.getStyle().set("margin-left","auto");
        this.getStyle().set("margin-right","auto");
        beanClass = (Class<T>) bean.getClass();
        binderValidator = new BeanValidationBinder<T>(beanClass);
        binderReader = new Binder<T>();
        this.messageSource = messageSource;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Map<Integer, Set<CustomForm.FieldEntry>> formGroupsMap = buildFormGroups(bean);
        errorsBox.getStyle().set("width", "100%");
        buildForm(formGroupsMap);
    }

    public void setWidthPercentage(double width) {
        this.getStyle().set("width", width + "%");
    }

    private Map<Integer, Set<CustomForm.FieldEntry>> buildFormGroups(T bean) {
        Map<Integer, Set<CustomForm.FieldEntry>> map = new TreeMap<>();
        Class<?> dtoClass = bean.getClass();
        Field[] fields = dtoClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.FormField.class) != null) {
                org.bastanchu.churierp.churierpback.util.annotation.FormField formFieldAnnotation =
                        field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.FormField.class);
                org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration comboBoxConfiguration =
                        formFieldAnnotation.comboBoxConfiguration();
                org.bastanchu.churierp.churierpback.util.annotation.ListField listFieldAnnotation =
                        field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.ListField.class);
                boolean keyListField = false;
                if ((listFieldAnnotation != null) && (listFieldAnnotation.keyField())) {
                    keyListField = true;
                }
                if (field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class) != null) {
                    org.bastanchu.churierp.churierpback.util.annotation.Field fieldAnnotation =
                            field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.Field.class);
                    Integer groupId = formFieldAnnotation.groupId();
                    Integer colSpan = formFieldAnnotation.colSpan();
                    Set<CustomForm.FieldEntry> fieldEntrySet = null;
                    if ((fieldEntrySet = map.get(groupId)) == null) {
                        fieldEntrySet = new TreeSet<>();
                        map.put(formFieldAnnotation.groupId(), fieldEntrySet);
                    }
                    Integer fieldIndexIngroup = formFieldAnnotation.indexInGroup();
                    String labelText = messageSource.getMessage(fieldAnnotation.key(), null, LocaleContextHolder.getLocale());

                    Class<?> fieldClass = field.getType();
                    fieldEntrySet.add(new CustomForm.FieldEntry(bean, formFieldAnnotation, labelText, fieldClass,
                            colSpan, comboBoxConfiguration, formFieldAnnotation.widthPercentage(), field, keyListField));
                }
            } else if (field.getAnnotation(org.bastanchu.churierp.churierpback.util.annotation.HiddenFormField.class) != null) {
                // Fill initial null value for hidden field
                formHiddenValuesMap.put(field.getName(), null);
            }
        }
        return map;
    }

    private Integer getBiggestFormGroupsize(Map<Integer, Set<CustomForm.FieldEntry>> formGroupsMap) {
        return formGroupsMap.values().stream().map(e -> {
            return e.size();
        }).reduce((a , b) -> {
            if ((a != null) && (b != null) ) {
                if ( a >= b) {
                    return a;
                } else {
                    return b;
                }
            }
            else if (a != null) {
                return a;
            }
            else if (b != null) {
                return b;
            }
            else {
                return null;
            }
        }).get();
    }

    private void buildForm(Map<Integer, Set<CustomForm.FieldEntry>> formGroupsMap) {
        Integer biggestGroupSize = getBiggestFormGroupsize(formGroupsMap);
        setColspan(new Div(), biggestGroupSize * 2);
        for (Set<CustomForm.FieldEntry> groupSet :formGroupsMap.values()) {
           for (CustomForm.FieldEntry fieldEntry : groupSet) {
                addFieldToForm(fieldEntry);
           }
        }
        // Finally add lower errors box
        add(errorsBox, biggestGroupSize * 2);
    }

    private Component addFieldToForm(CustomForm.FieldEntry fieldEntry) {
        Class<?> fieldType = fieldEntry.getFieldType();
        Component component = null;
        boolean forceReadOnly = fieldEntry.listKeyField && readOnlyKeys;
        if (!fieldEntry.comboBoxConfiguration.mapFieldName().equals("")) {
            //Combo Box
            ComboBoxFormMapper<T> comboBoxFormMapper =
                    new ComboBoxFormMapper<>(beanClass, binderValidator, binderReader,
                            validator, formComponentsMap, forceReadOnly);
            component = comboBoxFormMapper.mapFieldEntry(this, fieldEntry);
        }
        else if (Date.class.isAssignableFrom(fieldType)) {
            // Date Picker
            DateFormMapper<T> dateFormMapper =
                    new DateFormMapper<>(beanClass, binderValidator, binderReader,
                            validator, formComponentsMap, forceReadOnly);
            component = dateFormMapper.mapFieldEntry(this, fieldEntry);
        } else if (Integer.class.isAssignableFrom(fieldType)) {
            // Number Text Field
            IntegerFormMapper<T> integerFormMapper =
                    new IntegerFormMapper<>(beanClass, binderValidator, binderReader,
                            validator, formComponentsMap, forceReadOnly);
            component = integerFormMapper.mapFieldEntry(this, fieldEntry);
        } else if(BigDecimal.class.isAssignableFrom(fieldType)) {
            // BigDecimal field
            BigDecimalFormMapper<T> bigDecimalFormMapper =
                    new BigDecimalFormMapper<>(beanClass, binderValidator, binderReader,
                            validator, formComponentsMap, forceReadOnly);
            component = bigDecimalFormMapper.mapFieldEntry(this, fieldEntry);
        } else {
            // Generic Text Field
            StringFormMapper<T> stringFormMapper =
                    new StringFormMapper<>(beanClass, binderValidator, binderReader,
                            validator, formComponentsMap, forceReadOnly);
            component = stringFormMapper.mapFieldEntry(this, fieldEntry);
        }
        return component;
    }

    public void addErrorMessageKey(String errorMessageKey, Object[] parameters) {
        String errorMessage = messageSource.getMessage(errorMessageKey, parameters, LocaleContextHolder.getLocale());
        Label label = new Label(errorMessage);
        label.getStyle().set("color", "red");
        label.getStyle().set("font-size", "10pt");
        label.getStyle().set("width", "100%");
        label.getStyle().set("margin-top", "0px");
        errorsBox.add(label);
    }

    public void addErrorMessageText(String errorMessageText) {
        Label label = new Label(errorMessageText);
        label.getStyle().set("color", "red");
        label.getStyle().set("font-size", "10pt");
        label.getStyle().set("width", "100%");
        label.getStyle().set("margin-top", "0px");
        errorsBox.add(label);
    }

    public void removeErrorMessages() {
        errorsBox.removeAll();
    }

    public boolean writeBean(T targetBean) {
        removeErrorMessages();
        try {
            binderValidator.writeBean(targetBean);
            writeHiddenValues(targetBean);
            if (targetBean instanceof org.bastanchu.churierp.churierpback.dto.Validator) {
                org.bastanchu.churierp.churierpback.dto.Validator validable =
                        (org.bastanchu.churierp.churierpback.dto.Validator) targetBean;
                List<String> errors = validable.validate();
                if ((errors != null) && (errors.size() > 0)) {
                    errors.stream().forEach(it -> {
                        addErrorMessageKey(it, null);
                    });
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (ValidationException e) {
            logger.info("Validation error in form: " + e.getMessage());
            return false;
        }
    }

    public void readBean(T sourceBean) {
        binderValidator.readBean(sourceBean);
        readHiddenValues(sourceBean);
    }

    private void writeHiddenValues(T bean) {
        for (String fieldName : formHiddenValuesMap.keySet()) {
            Field field = null;
            try {
                field = bean.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                //This may not happen
                throw new RuntimeException(e);
            }
            Object value = formHiddenValuesMap.get(fieldName);
            try {
                field.setAccessible(true);
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                // This may not happen
                throw new RuntimeException(e);
            }
        }
    }

    private void readHiddenValues(T bean) {
        for (String fieldName : formHiddenValuesMap.keySet()) {
            Field field = null;
            try {
                field = bean.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                //This may not happen
                throw new RuntimeException(e);
            }
            Object value = null;
            try {
                field.setAccessible(true);
                value = field.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            formHiddenValuesMap.put(fieldName, value);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
