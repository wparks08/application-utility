package AppUtility.db;

import AppUtility.Database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Deprecated
public abstract class DBObject<T> {

    public T get(long id) {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        T t = null;
        try {
            t = (T) clazz.getDeclaredConstructor(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        String tableName = getSqlNameFromJavaName(t.getClass().getSimpleName());

        ResultSet resultSet = db.getById(tableName, id);
        Field[] fields = t.getClass().getDeclaredFields();

        try {
            setObjectFields((T) t, resultSet, fields);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return t;
    }

    public T getBy(String column, String value) {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        T t = null;
        try {
            t = (T) clazz.getDeclaredConstructor(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        String tableName = getSqlNameFromJavaName(t.getClass().getSimpleName());

        ResultSet resultSet = db.getByColumnValue(tableName, column, value);
        Field[] fields = t.getClass().getDeclaredFields();

        try {
            if (!resultSet.isClosed()) {
                setObjectFields((T) t, resultSet, fields);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return t;
    }

    public List<T> list() {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        ResultSet resultSet;
        resultSet = db.list(clazz.getSimpleName());
        List<T> objectArrayList = new ArrayList<>();

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    T t = null;
                    try {
                        t = (T) clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    assert t != null;
                    Field[] fields = t.getClass().getDeclaredFields();

                    setObjectFields((T) t, resultSet, fields);
                    objectArrayList.add(t);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objectArrayList;
    }

    public void save() {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        //try get by id
        //if no result, persist new
        Field idField;
        try {
            idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            long id = idField.getLong(this);
            String tableName = getSqlNameFromJavaName(clazz.getSimpleName());

            ResultSet resultSet = db.getById(tableName, id);
            if (!resultSet.next()) {
                System.out.println("No result. Saving new " + tableName);

                db.close();
                db.connect();

                HashMap<String, String> parameterMap = new HashMap<>();
                Field[] fields = this.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (field.getName().equals("id") || field.getName().equals("tableName")) {  //ignore tableName and id fields
                        continue;
                    }
                    field.setAccessible(true); //Get around private access
                    if (field.get(this) == null) {  //ignore null fields
                        continue;
                    }
                    String key = getSqlNameFromJavaName(field.getName());

                    if (field.getType().toString().equals("class [B")) {
                        String value = Base64.getEncoder().encodeToString((byte[]) field.get(this));
                        parameterMap.put(key, value);
                    } else {
                        String value = String.valueOf(field.get(this));
                        parameterMap.put(key, value);
                    }
                }
                long generatedId = db.insert(tableName, parameterMap);
                idField.setLong(this, generatedId);
            } else {
                System.out.println("Result found. Updating " + tableName + ":" + id);

                db.close();
                db.connect();

                HashMap<String, String> parameterMap = new HashMap<>();
                Field[] fields = this.getClass().getDeclaredFields();
                //Implement update method in Database
                for (Field field : fields) {
                    if (field.getName().equals("tableName")) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.get(this) == null) {
                        continue;
                    }
                    String key = getSqlNameFromJavaName(field.getName());
                    if (field.getType().toString().equals("class [B")) {
                        String value = Base64.getEncoder().encodeToString((byte[]) field.get(this));
                        parameterMap.put(key, value);
                    } else {
                        String value = String.valueOf(field.get(this));
                        parameterMap.put(key, value);
                    }
                }
                db.update(tableName, parameterMap);
            }
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        Database db = new Database();
        db.connect();
        Class clazz = this.getClass();

        try {
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);

            long id = idField.getLong(this);

            if (id == 0) {
                return; //no op if it doesn't exist in database
            }

            String tableName = getSqlNameFromJavaName(clazz.getSimpleName());

            System.out.println("Deleting " + tableName + ":" + id);

            db.delete(tableName, id);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<?> getChildren(Class childClass) {
        String childTable = getSqlNameFromJavaName(childClass.getSimpleName());

        Database db = new Database();
        db.connect();
        Class clazz = this.getClass();

        Field idField;

        long id = 0;
        try {
            idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);

            id = idField.getLong(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String parentClassName = clazz.getSimpleName();

        List<Object> toReturn = new ArrayList<>();

        ResultSet resultSet = db.getByParentId(childTable, parentClassName, id);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Object child = childClass.newInstance();
                    Field[] fields = childClass.getDeclaredFields();

                    setObjectFields(child, resultSet, fields);
                    toReturn.add(child);
                }
            } catch (SQLException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return toReturn;
    }

    private String getSqlNameFromJavaName(String javaName) {
        String[] splitString = javaName.split("[A-Z]");
        String sqlName = "";
        for (int i = 0; i < splitString.length; i++) {
            if (i == 0 && Character.isUpperCase(javaName.charAt(0))) {
                sqlName = javaName.charAt(0) + splitString[0];
            } else if (i != splitString.length - 1) {
                sqlName = sqlName + splitString[i] + "_";
                if (i > 0) {
                    sqlName = sqlName + javaName.charAt(javaName.indexOf(splitString[i]) + splitString[i].length());
                } else {
                    sqlName = sqlName + javaName.charAt(sqlName.length() - 1);
                }
            } else {
                sqlName = sqlName + splitString[i];
            }
        }
        return sqlName.toLowerCase();
    }

    private String getJavaNameFromSqlName(String sqlName) {
        String[] splitString = sqlName.split("_");
        String javaName = "";
        for (int i = 0; i < splitString.length; i++) {
            if (i > 0) {
                splitString[i] = Character.toUpperCase(splitString[i].charAt(0)) + splitString[i].substring(1);
                javaName = javaName + splitString[i];
            } else {
                javaName = splitString[i];
            }
        }
        return javaName;
    }

    private void setObjectFields(Object object, ResultSet resultSet, Field[] fields) throws SQLException {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("tableName")) {
                continue;
            }
            try {
                switch (field.getType().toString()) {
                    case "long":
                        field.set(object, resultSet.getLong(getSqlNameFromJavaName(field.getName())));
                        break;
                    case "class java.lang.String":
                        field.set(object, resultSet.getString(getSqlNameFromJavaName(field.getName())));
                        break;
                    case "class [B":
                        field.set(object, Base64.getDecoder().decode(resultSet.getString(getSqlNameFromJavaName(field.getName()))));
                        break;
                    default:
                        System.out.println(field.getName());
                        System.out.println(field.getType().toString());
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
