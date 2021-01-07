package com.uqtr.ca.orm;

import java.lang.reflect.Field;

/**
 * This class allows to store the information of the field(his deferent annotation and type)
 */
public class FieldInfo {
    private final Field field;
    private final boolean isHasMany;
    private final boolean isHasOne;
    private final HasMany hasMany;
    private final HasOne hasOne;

    private String fieldName;
    private final boolean isString;
    private final boolean isNumber;

    /**
     * This constructor adds all the necessary information of a field
     * which will be use to get the fields information later on
     *
     * @param f
     */
    public FieldInfo(Field f) {
        field = f;

        f.setAccessible(true);
        fieldName = f.getName().toLowerCase();

        isString = BeanInfo.isString(f.getType());
        isNumber = BeanInfo.isNumber(f.getType());
        boolean isEntity = f.isAnnotationPresent(Entity.class);
        boolean isField = f.isAnnotationPresent(DbField.class);

        hasOne = f.getAnnotation(HasOne.class);
        isHasOne = hasOne != null;

        hasMany = f.getAnnotation(HasMany.class);
        isHasMany = hasMany != null;

        if (isField) {
            DbField annoField = f.getAnnotation(DbField.class);
            fieldName = annoField.name();
        }

        if (isHasMany && !hasMany.klass().isAnnotationPresent(Entity.class)) {
            throw new Error("Entity should be annotated by @Entity for the following class: " + hasMany.klass());
        }

        if (isHasOne && !hasOne.klass().isAnnotationPresent(Entity.class)) {
            throw new Error("Entity should be annotated by @Entity for the following class " + hasOne.klass());
        }
    }

    public boolean isString() {
        return isString;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public HasMany getHasMany() {
        return hasMany;
    }

    public HasOne getHasOne() {
        return hasOne;
    }

    public Field getField() {
        return field;
    }

    public boolean isHasOne() {
        return isHasOne;
    }

    public boolean isHasMany() {
        return isHasMany;
    }

    public String getFieldName() {
        return fieldName;
    }
}
