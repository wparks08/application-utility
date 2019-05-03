package utility.db;

import utility.Database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DBObject<T> {

    public T get(long id) {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        T t = null;
        try {
            t = (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String tableName = null;
        try {
            tableName = (String)t.getClass().getDeclaredField("tableName").get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = db.getById(tableName, id);
        Field fields[] = t.getClass().getDeclaredFields();

        try {
            setObjectFields((T) t, resultSet, fields);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return t;
    }

    public List<T> list() throws SQLException {
        Database db = new Database();
        db.connect();

        Class clazz = this.getClass();

        ResultSet resultSet = null;
        try {
            resultSet = db.list((String) clazz.getDeclaredField("tableName").get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        List<T> objectArrayList = new ArrayList<>();

        if(resultSet != null) {
            while(resultSet.next()) {
                T t = null;
                try {
                    t = (T) clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                Field fields[] = t.getClass().getDeclaredFields();

                setObjectFields((T) t, resultSet, fields);
                objectArrayList.add(t);
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
        Field idField = null;
        try {
            idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            long id = idField.getLong(this);
            String tableName = (String) clazz.getDeclaredField("tableName").get(this);

            ResultSet resultSet = db.getById(tableName, id);
            if (!resultSet.next()) {
                System.out.println("No result. Saving new " + tableName);

                db.close();
                db.connect();

                HashMap<String, String> parameterMap = new HashMap<>();
                Field fields[] = this.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (field.getName().equals("id") || field.getName().equals("tableName")) {  //ignore tableName and id fields
                        continue;
                    }
                    field.setAccessible(true); //Get around private access
                    if (field.get(this) == null) {  //ignore null fields
                        continue;
                    }
                    String key = getSqlNameFromJavaName(field.getName());
                    String value = String.valueOf(field.get(this));
                    parameterMap.put(key, value);
                }
                db.insert(tableName, parameterMap);
            } else {
                System.out.println("Result found. Updating " + tableName + ":" + id);

                db.close();
                db.connect();

                HashMap<String, String> parameterMap = new HashMap<>();
                Field fields[] = this.getClass().getDeclaredFields();
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
                    String value = String.valueOf(field.get(this));
                    parameterMap.put(key, value);
                }
                db.update(tableName, parameterMap);
            }
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSqlNameFromJavaName(String javaName) {
        String[] splitString = javaName.split("[A-Z]");
        String sqlName = "";
        for (int i = 0; i < splitString.length; i++) {
            if (i != splitString.length - 1) {
                sqlName = sqlName + splitString[i] + "_";
                sqlName = sqlName + javaName.charAt(sqlName.length() - (1 + i));
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
            } else if (i == 0) {
                javaName = splitString[i];
            }
        }
        return javaName;
    }

    private void setObjectFields(T t, ResultSet resultSet, Field[] fields) throws SQLException {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("tableName")) {
                continue;
            }
            try {
                switch (field.getType().toString()) {
                    case "long": field.set(t, resultSet.getLong(getSqlNameFromJavaName(field.getName())));
                        break;
                    case "class java.lang.String": field.set(t, resultSet.getString(getSqlNameFromJavaName(field.getName())));
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
