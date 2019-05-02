package utility.db;

import utility.Database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DBObject<T> {

    public T get(long id) throws SQLException {
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

        setObjectFields((T) t, resultSet, fields);

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
                System.out.println("No result");
                //get sql names from java names
                //pass to method in Database for insert
                //with table name, and array of sql column names
                //ignore tableName field
                HashMap<String, String> tester = new HashMap<>();
                tester.put("name","New Carrier");
                db.insert(tableName, tester);
            } else {
                System.out.println("Result found");
                //Implement update method in Database
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
