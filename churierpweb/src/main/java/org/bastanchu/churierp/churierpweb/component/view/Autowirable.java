package org.bastanchu.churierp.churierpweb.component.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface Autowirable {

    default void autowireComponents(ApplicationContext applicationContext) {
        try {
            Field[] fields = getFields(this.getClass());
            for (Field aField:fields) {
                Annotation annotation = aField.getAnnotation(Autowired.class);
                if (annotation != null) {
                    aField.setAccessible(true);
                    aField.set(this, applicationContext.getBean(aField.getType()));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field[] getFields(Class<?> currentClass) {
        if (currentClass.equals(Object.class)) {
            return Object.class.getDeclaredFields();
        } else {
            Field[] parentFields = getFields(currentClass.getSuperclass());
            Field[] thisFields = currentClass.getDeclaredFields();
            List<Field> resultList = new ArrayList<>();
            for (Field field:parentFields) {
                resultList.add(field);
            }
            for (Field field:thisFields) {
                resultList.add(field);
            }
            Field[] resultFields = resultList.toArray(new Field[0]);
            return resultFields;
        }
    }
}
