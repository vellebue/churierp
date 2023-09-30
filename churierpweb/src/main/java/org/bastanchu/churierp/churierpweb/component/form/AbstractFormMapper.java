package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(AbstractFormMapper.class);

    protected Class<T> beanClass;
    protected BeanValidationBinder<T> binderValidator;
    protected Binder<T> binderReader;
    protected Validator validator;
    protected Map<String, Component> formComponentsMap;
    protected boolean forceReadOnly;

    public AbstractFormMapper(Class<T> beanClass,
                              BeanValidationBinder<T> binderValidator,
                              Binder<T>binderReader,
                              Validator validator,
                              Map<String, Component> formComponentsMap,
                              boolean forceReadOnly) {
        this.beanClass = beanClass;
        this.binderValidator = binderValidator;
        this.binderReader = binderReader;
        this.validator = validator;
        this.formComponentsMap = formComponentsMap;
        this.forceReadOnly = forceReadOnly;
    }

    protected Div buildComponentContainer(Component component, CustomForm.FieldEntry fieldEntry) {
        Div divContainer = new Div();
        Div componentFiller = new Div();
        Div divFiller = new Div();
        componentFiller.getStyle().set("width", "90%");
        divFiller.getStyle().set("width","10%");
        componentFiller.add(component);
        divContainer.add(componentFiller);
        divContainer.add(divFiller);
        logger.debug("Drawing generic field max width in pixels: " +  fieldEntry.getMaxWidthInPixels());
        if (fieldEntry.getMaxWidthInPixels() != 0.0) {
            divContainer.getStyle().set("max-width", fieldEntry.getMaxWidthInPixels() + "px");
        }
        return divContainer;
    }

    @Deprecated
    //public abstract FormLayout.FormItem mapFormEntry(CustomForm form, CustomForm.FieldEntry fieldEntry);

    public abstract Component mapFieldEntry(CustomForm form, CustomForm.FieldEntry fieldEntry);

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
            logger.error("Validation Exception", e);
        } catch (Exception e) {
            // TODO add logger trace if constructor fails newInstance
            logger.error("Generic Exception", e);
        }
        return validationResult;
    }
}
