package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Map;

class StringFormMapper<T> extends AbstractFormMapper<T> {

    public StringFormMapper(Class<T> beanClass, BeanValidationBinder<T> binderValidator, Binder<T> binderReader, Validator validator, Map<String, Component> formComponentsMap, boolean forceReadOnly) {
        super(beanClass, binderValidator, binderReader, validator, formComponentsMap, forceReadOnly);
    }

    @Override
    public Component mapFieldEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        GeneratedVaadinTextField<?, String> formComponent = null;
        if (fieldEntry.getFormField().password()) {
            formComponent = new PasswordField(fieldEntry.getFieldLabel());
        } else {
            formComponent = new TextField(fieldEntry.getFieldLabel());
        }
        if (forceReadOnly || fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        if (field.getAnnotation(NotEmpty.class) != null) {
            if (formComponent instanceof HasPrefixAndSuffix) {
                Div prefix = new Div();
                prefix.getStyle().set("min-width","2px");
                prefix.getStyle().set("min-height","20px");
                prefix.getStyle().set("background-color","#FF0000");
                ((HasPrefixAndSuffix) formComponent).setPrefixComponent(prefix);
            }
        }
        formComponent.getStyle().set("width","100%");
        formComponent.getStyle().set("margin-left", "0px");
        form.add( buildComponentContainer(formComponent, fieldEntry), fieldEntry.getColSpan());
        binderReader.forField(formComponent)
                .bind(e -> {
                    return (String) binderGetter(field, e);
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
            return (String) binderGetter(field, e);
        }, (e , v) -> {
            binderSetter(field, e, v);
        });
        formComponentsMap.put(fieldEntry.getField().getName(), formComponent);
        return formComponent;
    }


}
