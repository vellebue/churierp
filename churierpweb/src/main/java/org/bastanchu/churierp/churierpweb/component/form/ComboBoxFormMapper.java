package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComboBoxFormMapper<T> extends AbstractFormMapper<T> {

    @Data
    @AllArgsConstructor
    private class Item {

        private String key;
        private String value;

    }

    public ComboBoxFormMapper(Class<T> beanClass, BeanValidationBinder<T> binderValidator, Binder<T> binderReader, Validator validator, Map<String, Component> formComponentsMap, boolean forceReadOnly) {
        super(beanClass, binderValidator, binderReader, validator, formComponentsMap, forceReadOnly);
    }

    @Override
    public FormLayout.FormItem mapFormEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        ComboBox<String> formComponent = generateComboBox(fieldEntry);
        if (fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        if (field.getAnnotation(NotEmpty.class) != null) {
            formComponent.getStyle().set("border-left-style","solid");
            formComponent.getStyle().set("border-left-width","thick");
            formComponent.getStyle().set("border-left-color","#FF0000");
        }
        formComponent.getStyle().set("width","100%");
        if (forceReadOnly || fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        FormLayout.FormItem formComponentContainer = form.addFormItem(formComponent, fieldEntry.getFieldLabel());
        binderReader.forField(formComponent)
                .bind(e -> {
                    String item = (String) binderGetter(field, e);
                    return item;
                }, (e , v) -> {
                    binderSetter(field, e, v);
                });
        binderValidator.forField(formComponent).withValidator((e, valueContext) -> {
            String validation = binderValidator(field);
            if (validation.equals("")) {
                return ValidationResult.ok();
            } else {
                return ValidationResult.error(validation);
            }
        }).bind(e -> {
            String item = (String) binderGetter(field, e);
            return item;
        }, (e , v) -> {
            binderSetter(field, e, v);
        });
        formComponentsMap.put(fieldEntry.getField().getName(), formComponent);
        return formComponentContainer;
    }

    private ComboBox<String> generateComboBox(CustomForm.FieldEntry fieldEntry) {
        String conditionFieldName = fieldEntry.getComboBoxConfiguration().conditionFieldName();
        if (conditionFieldName.equals("")) {
            Map<String, String> itemsMap = getComboItemsMap(fieldEntry);
            List<String> itemKeys = itemsMap.entrySet().stream().sorted((e1, e2) -> {
                if (e1.getValue().equals(e2.getValue())) {
                    return e1.getKey().compareTo(e2.getKey());
                } else {
                    return e1.getValue().compareTo(e2.getValue());
                }
            }).map(e -> e.getKey()).collect(Collectors.toList());
            ComboBox<String> formComponent = new ComboBox<>();
            formComponent.setItems(itemKeys);
            formComponent.setItemLabelGenerator(e -> e + " - " + itemsMap.get(e));
            return formComponent;
        } else {
            ComboBox<String> masterComboBox = null;
            try {
                masterComboBox = (ComboBox<String>) formComponentsMap.get(conditionFieldName);
            } catch (ClassCastException e) {
                throw new RuntimeException("Component for field " + conditionFieldName + " for DTO class " + beanClass.getName() + " is not defined as ComboBox");
            }
            Map<String, Map<String, String>> surrogateMap = getSurrogateComboItemsMap(fieldEntry);
            SurrogateComboBox formComponent = new SurrogateComboBox(surrogateMap, masterComboBox);
            return formComponent;
        }
    }

    private Map<String,String> getComboItemsMap(CustomForm.FieldEntry fieldEntry) {
        String mapFieldName = fieldEntry.getComboBoxConfiguration().mapFieldName();
        String conditionFieldName = fieldEntry.getComboBoxConfiguration().conditionFieldName();
        Field mapField = null;
        try {
            mapField = beanClass.getDeclaredField(mapFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Map field " + mapFieldName + " not found in DTO class " + beanClass.getName());
        }
        Map<String,String> mapFieldValue = null;
        // Single map of values Map<String, String>
        try {
            mapField.setAccessible(true);
            mapFieldValue = (Map<String, String>) mapField.get(fieldEntry.getObjectBean());
        } catch (ClassCastException e) {
            throw new RuntimeException("Map field " + mapFieldName + " in DTO class " + beanClass.getName() + " should be a Map<String, String> field");
        } catch (IllegalAccessException e) {
            // TODO this may not happen if private accessor is unlocked
        }
        return mapFieldValue;
    }

    private Map<String,Map<String, String>> getSurrogateComboItemsMap(CustomForm.FieldEntry fieldEntry) {
        String mapFieldName = fieldEntry.getComboBoxConfiguration().mapFieldName();
        String conditionFieldName = fieldEntry.getComboBoxConfiguration().conditionFieldName();
        Field mapField = null;
        try {
            mapField = beanClass.getDeclaredField(mapFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Map field " + mapFieldName + " not found in DTO class " + beanClass.getName());
        }
        // Conditioned map of values Map<String, Map<String, String>>
        Map<String, Map<String, String>> conditionedMapFieldValue = null;
        try {
            mapField.setAccessible(true);
            conditionedMapFieldValue = (Map<String, Map<String,String>>) mapField.get(fieldEntry.getObjectBean());
        } catch (ClassCastException e) {
            throw new RuntimeException("Map field " + mapFieldName + " in DTO class " + beanClass.getName() + " should be a Map<String, String> field");
        } catch (IllegalAccessException e) {
            // TODO this may not happen if private accessor is unlocked
        }
        Field conditionedField = null;
        try {
            conditionedField = beanClass.getDeclaredField(conditionFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Conditioning field " + conditionFieldName + " not found in DTO class " + beanClass.getName());
        }
        String conditionedFieldValue = null;
        try {
            conditionedField.setAccessible(true);
            conditionedFieldValue = (String) conditionedField.get(fieldEntry.getObjectBean());
        } catch(ClassCastException e) {
            throw new RuntimeException("Conditioning field " + conditionFieldName + " in DTO class " + beanClass.getName() + " must be typed String");
        } catch (IllegalAccessException e) {
            // TODO this may not happen if private accessor is unlocked
        }
        return conditionedMapFieldValue;
    }

}
