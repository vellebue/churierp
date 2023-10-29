package org.bastanchu.churierp.churierpweb.component.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Map;

public class BooleanCheckBoxFormMapper<T> extends AbstractFormMapper<T> {


    public BooleanCheckBoxFormMapper(Class<T> beanClass, BeanValidationBinder<T> binderValidator, Binder<T> binderReader, Validator validator, Map<String, Component> formComponentsMap, boolean forceReadOnly) {
        super(beanClass, binderValidator, binderReader, validator, formComponentsMap, forceReadOnly);
    }

    @Override
    public Component mapFieldEntry(CustomForm form, CustomForm.FieldEntry fieldEntry) {
        Field field = fieldEntry.getField();
        Checkbox formComponent = new Checkbox(fieldEntry.getFieldLabel());
        if ((field.getAnnotation(NotEmpty.class) != null) || (field.getAnnotation(NotNull.class) != null)) {
            Div prefix = new Div();
            prefix.getStyle().set("min-width","2px");
            prefix.getStyle().set("min-height","20px");
            prefix.getStyle().set("background-color","#FF0000");
            PrefixUtil.setPrefixComponent(formComponent, prefix);
        }
        if (forceReadOnly || fieldEntry.getFormField().readOnly()) {
            formComponent.setReadOnly(true);
        }
        form.add(buildComponentContainer(formComponent, fieldEntry), fieldEntry.getColSpan());
        binderReader.forField(formComponent)
                .bind(e -> {
                    return (Boolean) binderGetter(field, e);
                }, (e , v) -> {
                    binderSetter(field, e, v);
                });
        binderValidator.forField(formComponent).withValidator((e, valueContext) -> {
            return ValidationResult.ok();
        }).bind(e -> {
            return (Boolean) binderGetter(field, e);
        }, (e , v) -> {
            binderSetter(field, e, v);
        });
        formComponentsMap.put(fieldEntry.getField().getName(), formComponent);
        return formComponent;
    }
}
