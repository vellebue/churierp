package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class AbstractFormMapper<T> {

    protected Class<T> beanClass;
    protected BeanValidationBinder<T> binderValidator;
    protected Binder<T> binderReader;
    protected Validator validator;
    protected Map<String, Component> formComponentsMap;

    public AbstractFormMapper(Class<T> beanClass,
                              BeanValidationBinder<T> binderValidator,
                              Binder<T>binderReader,
                              Validator validator,
                              Map<String, Component> formComponentsMap) {
        this.beanClass = beanClass;
        this.binderValidator = binderValidator;
        this.binderReader = binderReader;
        this.validator = validator;
        this.formComponentsMap = formComponentsMap;
    }

    public abstract FormLayout.FormItem mapFormEntry(CustomForm form, CustomForm.FieldEntry fieldEntry);

    protected Object binderGetter(Field field, Object e) {
        try {
            field.setAccessible(true);
            Object value = field.get(e);
            if (value != null) {
                if (Date.class.isAssignableFrom(value.getClass())) {
                    Date dateValue = (Date) value;
                    value = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
            }
            return value;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void binderSetter(Field field, Object e, Object value) {
        try {
            field.setAccessible(true);
            if (value != null) {
                if (LocalDate.class.isAssignableFrom(value.getClass())) {
                    LocalDate localDateValue = (LocalDate) value;
                    value = java.util.Date.from(localDateValue.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                }
            }
            field.set(e, value);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String binderValidator(Field field) {
        String validationResult = "";
        try {
            T bean = beanClass.getDeclaredConstructor().newInstance();
            binderReader.writeBean(bean);
            Set<ConstraintViolation<T>> validations = validator.validate(bean);
            for (ConstraintViolation<T> constraintViolation: validations) {
                if (constraintViolation.getPropertyPath().toString().equals(field.getName())) {
                    validationResult = constraintViolation.getMessage();
                }
            }
        } catch (ValidationException e) {
            // TODO add logger trace
        } catch (Exception e) {
            // TODO add logger trace if constructor fails newInstance
        }
        return validationResult;
    }
}
