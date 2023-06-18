package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class BigDecimalFormMapper<T> extends AbstractFormMapper<T> {

    public BigDecimalFormMapper(Class<T> beanClass, BeanValidationBinder<T> binderValidator, Binder<T> binderReader, Validator validator, Map<String, Component> formComponentsMap, boolean forceReadOnly) {
        super(beanClass, binderValidator, binderReader, validator, formComponentsMap, forceReadOnly);
    }

    @Override
    public FormLayout.FormItem mapFormEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        BigDecimalField formComponent = new BigDecimalField();
        formComponent.setLocale(LocaleContextHolder.getLocale());
        if (fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        if ((field.getAnnotation(NotEmpty.class) != null) || (field.getAnnotation(NotNull.class) != null)) {
            formComponent.getStyle().set("border-left-style","solid");
            formComponent.getStyle().set("border-left-width","thick");
            formComponent.getStyle().set("border-left-color","#FF0000");
        }
        if (forceReadOnly || fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        FormLayout.FormItem formComponentContainer = form.addFormItem(formComponent, fieldEntry.getFieldLabel());
        binderReader.forField(formComponent)
                .bind(e -> {
                    return (BigDecimal) binderGetter(field, e);
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
            return (BigDecimal) binderGetter(field, e);
        }, (e , v) -> {
            binderSetter(field, e, v);
        });
        formComponentsMap.put(fieldEntry.getField().getName(), formComponent);
        return formComponentContainer;
    }

    @Override
    public Component mapFieldEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        BigDecimalField formComponent = new BigDecimalField(fieldEntry.getFieldLabel());
        formComponent.setLocale(LocaleContextHolder.getLocale());
        if (fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        if ((field.getAnnotation(NotEmpty.class) != null) || (field.getAnnotation(NotNull.class) != null)) {
            Div prefix = new Div();
            prefix.getStyle().set("min-width","2px");
            prefix.getStyle().set("min-height","20px");
            prefix.getStyle().set("background-color","#FF0000");
            formComponent.setPrefixComponent(prefix);
        }
        if (forceReadOnly || fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        form.add(buildComponentContainer(formComponent), fieldEntry.getColSpan());
        binderReader.forField(formComponent)
                .bind(e -> {
                    return (BigDecimal) binderGetter(field, e);
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
            return (BigDecimal) binderGetter(field, e);
        }, (e , v) -> {
            binderSetter(field, e, v);
        });
        formComponentsMap.put(fieldEntry.getField().getName(), formComponent);
        return formComponent;
    }
}
