package com.uqtr.ca.orm;

import com.uqtr.ca.orm.*;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class allows to store the bean information
 */
public class BeanInfo {
    private List<FieldInfo> fields = new ArrayList<>();
    private boolean isEntity;
    private Field fieldId;

    /**
     * This constructor is gathering the bean information and allow to store the
     * fields of the bean in this class so that we don't repeat information.
     *
     * @param bean the bean
     */
    public BeanInfo(Class bean) {
        isEntity = bean.isAnnotationPresent(Entity.class);
        if (!isEntity) throw new Error("Entity should be annotated by @Entity for the following class: " + bean);

        try {
            fieldId = bean.getDeclaredField("id");
            fieldId.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Error("Bean:: " + bean + " must have a field id");
        }

        for (Field f : bean.getDeclaredFields()) {
            if (f == fieldId) continue;

            if (!f.isAnnotationPresent(Ignore.class)) fields.add(new FieldInfo(f));
        }
    }

    public static boolean isString(Class klass) {
        return klass == String.class || klass == Date.class;
    }

    public static boolean isNumber(Class bean) {
        return Number.class.isAssignableFrom(bean) || bean == int.class;
    }

    public List<FieldInfo> getFields() {
        return this.fields;
    }

    public Field getFieldId() {
        return this.fieldId;
    }
}
