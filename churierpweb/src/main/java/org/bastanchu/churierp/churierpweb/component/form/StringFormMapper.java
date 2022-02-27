package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.textfield.GeneratedVaadinTextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Validator;
import java.lang.reflect.Field;

class StringFormMapper<T> extends AbstractFormMapper<T> {

    public StringFormMapper(Class<T> beanClass, BeanValidationBinder<T> binderValidator, Binder<T> binderReader, Validator validator) {
        super(beanClass, binderValidator, binderReader, validator);
    }

    @Override
    public void mapFormEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        GeneratedVaadinTextField<?, String> formComponent = null;
        if (fieldEntry.getFormField().password()) {
            formComponent = new PasswordField();
        } else {
            formComponent = new TextField();
        }
        if (fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        if (field.getAnnotation(NotEmpty.class) != null) {
            formComponent.getStyle().set("border-left-style","solid");
            formComponent.getStyle().set("border-left-width","thick");
            formComponent.getStyle().set("border-left-color","#FF0000");
        }
        form.addFormItem(formComponent, fieldEntry.getFieldLabel());
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
    }
}
