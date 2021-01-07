package com.uqtr.ca;

import com.google.inject.Inject;
import com.uqtr.ca.logging.Log;
import com.uqtr.ca.logging.RetrieveLog;
import com.uqtr.ca.orm.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class is where all the sql request will be treated
 */
public class PersistentManager {
    private static PersistentManager INSTANCE = null;
    private final Connection conn;
    private final Map<Class, BeanInfo> beans = new HashMap<>();

    /**
     * This constructor set the database properties
     *
     * @throws SQLException Arrives when it is unable to get a connection
     */
    @Inject
    public PersistentManager() throws SQLException {
        String url = "jdbc:postgresql://localhost:5440/dev";
        Properties props = new Properties();
        props.setProperty("user", "dev");
        props.setProperty("password", "dev");
        conn = DriverManager.getConnection(url, props);
    }

    /**
     * This method allows to retrieve a single data from the table in the database
     *
     * @param bean The representant of the bean class.
     * @param sql  The sql resquest.
     * @param <T>  Takes any bean class.
     * @return return the instance of the bean with his field set by retrieving the data.
     * @throws IllegalAccessException    Throws it if it is impossible to set or get a reflective field.
     * @throws InstantiationException    Throws it if creating the instance of the bean is impossible.
     * @throws NoSuchMethodException     Throws it if a certain method can't be found.s
     * @throws InvocationTargetException Throws it if there is an error getting the declared constructor.
     */
    @RetrieveLog
    public <T> T fetchOne(Class<T> bean, String sql) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        T instance = bean.getDeclaredConstructor().newInstance();

        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                instance = createInstance(bean, resultSet);
            } else
                Log.logInfo("Error:: No result has been found for this request: " + sql, ConsoleColors.RED);

        } catch (SQLException | NoSuchMethodException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }
        /* ignored */

        return instance;
    }

    /**
     * This method allow to retrieve all of the data in the database given by th sql request.
     * It allows to get a bean instance, put it in a list and return this list.
     *
     * @param bean The representant of the bean class.
     * @param sql  The sql request.
     * @param <T>  Takes any bean class.
     * @return return the list of instantiate beans object.
     * @throws IllegalAccessException Throws it if it is impossible to set or get a reflective field.
     * @throws InstantiationException Throws it if creating the instance of the bean is impossible.
     */
    @RetrieveLog
    public <T> List<T> fetchAll(Class<T> bean, String sql) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                T instance = createInstance(bean, resultSet);
                list.add(instance);
            }
        } catch (SQLException | NoSuchMethodException | InvocationTargetException throwables) {
            System.out.println("FetchAll::something went wrong");
            throwables.printStackTrace();
        }
        /* ignored */

        return list;
    }

    /**
     * This method allows to create an instance of a bean and return it.
     *
     * @param bean      The representant of the bean class.
     * @param resultSet The result of the sql request when called resultSet.next()
     * @param <T>       Takes any bean class.
     * @return the bean instance.
     * @throws IllegalAccessException    Throws it if it is impossible to set or get a reflective field.
     * @throws InstantiationException    Throws it if creating the instance of the bean is impossible.
     * @throws SQLException              Throws it if there is an error with the sql resultset.
     * @throws NoSuchMethodException     Throws it if a certain method can't be found.
     * @throws InvocationTargetException Throws it if there is an error getting the declared constructor.
     */
    private <T> T createInstance(Class<T> bean, ResultSet resultSet) throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException {
        T instance = bean.getDeclaredConstructor().newInstance();

        BeanInfo beanInfo = getBeanInfo(bean);

        Entity entity = bean.getAnnotation(Entity.class);

        // set id
        Field fieldId = beanInfo.getFieldId();
        fieldId.set(instance, resultSet.getInt(fieldId.getName().toLowerCase()));

        for (FieldInfo fieldInfo : beanInfo.getFields()) {
            Field f = fieldInfo.getField();
            String fieldName = fieldInfo.getFieldName();

            if (fieldInfo.isString()) {
                String value = resultSet.getString(fieldName);
                f.set(instance, value);
            } else if (fieldInfo.isNumber()) {
                int value = resultSet.getInt(fieldName);
                f.set(instance, value);
            } else if (fieldInfo.isHasMany()) {
                fetchHasMany(f, fieldId, instance, fieldInfo.getHasMany());
            } else if (fieldInfo.isHasOne()) {
                fetchHasOne(f, fieldId, instance, fieldInfo.getHasOne(), entity);
            }
        }

        return instance;
    }

    /**
     * This method allows to create the sql request associated with a table association of one to one.
     * After the method is created, it recalls the fetch methode to retrieve the correct data with the sql just created
     *
     * @param f        The field of the bean.
     * @param fieldId  The fieldID.
     * @param instance The instance of the object that is going to get his properties set.
     * @param hasOne   The annotation instance which allows to get the other table that we will be performing
     *                 the one-to-one association with. Allows to link the main table with the table that is contained
     *                 in the bean's field HasOne annotation.
     * @throws IllegalAccessException Throws it if it is impossible to set or get a reflective field.
     * @throws IllegalAccessException
     */
    public void fetchHasOne(Field f, Field fieldId, Object instance, HasOne hasOne, Entity entity) throws IllegalAccessException {

        Log.logInfo("---> A NEW REQUEST(TABLE JOIN) IS NEEDED ON THE FOLLOWING FIELD:: " + f.getName() +
                " ----> hasOne(one-to-one). Creating the sql request right now!\n", "");
        Entity hasOneEntity = (Entity) hasOne.klass().getAnnotation(Entity.class);

        String sqlHasOne = "SELECT " + hasOneEntity.tablename() + ".*" +
                " FROM " + entity.tablename() +
                " JOIN " + hasOneEntity.tablename() + " ON " + hasOneEntity.tablename() + ".id" + "=" + entity.tablename() + "." + hasOne.fk() +
                " WHERE " + hasOne.fk() + " = " + hasOne.fk();
        try {
            f.set(instance, this.fetchOne(hasOne.klass(), sqlHasOne));
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows to create the sql request for retrieving the join of two table with the relation
     * one to many.
     *
     * @param f        The field of the bean.
     * @param fieldId  The fieldID.
     * @param instance The instance of the object that is going to get his properties set.
     * @param hasMany  The annotation instance which allows to get the other table that will we will be performing
     *                 the sql request with(kind of like the link between the main table associated
     *                 with the bean and the other table that will need to be fetch.
     * @throws IllegalAccessException Throws it if it is impossible to set or get a reflective field.
     */
    public void fetchHasMany(Field f, Field fieldId, Object instance, HasMany hasMany) throws IllegalAccessException {
        Log.logInfo("---> A NEW REQUEST(TABLE JOIN) IS NEEDED ON THE FOLLOWING FIELD:: " + f.getName() +
                " ----> hasMany(one-to-many). Creating the sql request right now!\n", "");
        Entity hasManyEntity = (Entity) hasMany.klass().getAnnotation(Entity.class);
        String sqlHasMany = "SELECT * FROM " + hasManyEntity.tablename() + " WHERE " + hasMany.fk() + " = " + fieldId.get(instance);

        try {
            f.set(instance, this.fetchAll(hasMany.klass(), sqlHasMany));
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method puts the bean in the hashmap.
     *
     * @param bean The representant of the bean class.
     * @return The bean.
     */
    private BeanInfo getBeanInfo(Class bean) {
        if (!beans.containsKey(bean)) {
            beans.put(bean, new BeanInfo(bean));
        }

        return beans.get(bean);
    }

    /**
     * Get the instance of the peristant manager
     *
     * @return
     */
    public static PersistentManager getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new PersistentManager();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return INSTANCE;
    }

    /**
     * Close the connection to the database
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) { /* ignored */}
        }
    }
}
