package utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Database {

    private Connection conn = null;
    private String path = "jdbc:sqlite:db/ebau.sqlite";

    public void connect() {
        try {
            conn = DriverManager.getConnection(path);
            verifyTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(PreparedStatement preparedStatement) {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verifyTables() {
        //language=SQLite
        String carrier = "CREATE TABLE IF NOT EXISTS carrier (\n" +
                "  id   integer PRIMARY KEY,\n" +
                "  name text NOT NULL\n" +
                ");\n";
        //language=SQLite
        String form = "CREATE TABLE IF NOT EXISTS form (\n" +
                "  id             integer PRIMARY KEY,\n" +
                "  name           text NOT NULL,\n" +
                "  form_bytes     blob NOT NULL,\n" +
                "  form_extension text NOT NULL,\n" +
                "  carrier_id     integer,\n" +
                "  FOREIGN KEY (carrier_id) REFERENCES carrier(id)\n" +
                ");\n";
        //language=SQLite
        String formProperties = "CREATE TABLE IF NOT EXISTS form_property (\n" +
                "  id       integer PRIMARY KEY,\n" +
                "  property text NOT NULL,\n" +
                "  value    text NOT NULL,\n" +
                "  form_id  integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form(id)\n" +
                ");\n";
        //language=SQLite
        String censusHeaders = "CREATE TABLE IF NOT EXISTS census_header (\n" +
                "  id      integer PRIMARY KEY,\n" +
                "  header  text NOT NULL,\n" +
                "  form_id integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form(id)\n" +
                ");\n";
        //language=SQLite
        String formFields = "CREATE TABLE IF NOT EXISTS form_field (\n" +
                "  id         integer PRIMARY KEY,\n" +
                "  field_name text NOT NULL,\n" +
                "  form_id    integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form (id)\n" +
                ");\n";
        //language=SQLite
        String mapping = "CREATE TABLE IF NOT EXISTS mapping (\n" +
                "  id                integer PRIMARY KEY,\n" +
                "  census_header_id integer,\n" +
                "  form_field_id    integer,\n" +
                "  form_id           integer,\n" +
                "  FOREIGN KEY (census_header_id) REFERENCES census_header (id),\n" +
                "  FOREIGN KEY (form_field_id) REFERENCES form_field (id),\n" +
                "  FOREIGN KEY (form_id) REFERENCES form (id)\n" +
                ");";

        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(carrier);
            statement.execute(form);
            statement.execute(formProperties);
            statement.execute(censusHeaders);
            statement.execute(formFields);
            statement.execute(mapping);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(statement);
            //DO NOT close the connection here...
        }
    }

    public ResultSet getById(String table, long id) {
        //language=SQLite
        String sql = "SELECT * FROM " + table + " WHERE id = " + String.valueOf(id);

        return getResultSet(sql);
    }

    public ResultSet getByParentId(String childTable, String parentName, long id) {
        String parentIdField = parentName + "_id";

        //language=SQLite
        String sql = "SELECT * FROM " + childTable + " WHERE " + parentIdField + " = " + String.valueOf(id);

        return getResultSet(sql);
    }

    public ResultSet list(String table) {
        //language=SQLite
        String sql = "SELECT * FROM " + table;

        return getResultSet(sql);
    }

    public long insert(String table, HashMap<String, String> parameterMap) {
        try {
            if (!conn.isValid(0)) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //language=SQLite
        String sql = "INSERT INTO " + table + " (";
        String values = "VALUES (";

        List<String> keySet = new ArrayList<>(parameterMap.keySet());

        for (int i = 0; i < keySet.size(); i++) {
            if (i == keySet.size() - 1) {
                sql = sql + keySet.get(i) + ") ";
                values = values + "?)";
            } else {
                sql = sql + keySet.get(i) + ", ";
                values = values + "?,";
            }
        }

        sql = sql + values;

        PreparedStatement preparedStatement = null;

        long generatedId = 0;

        try {
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < keySet.size(); i++) {
                preparedStatement.setString(i+1, parameterMap.get(keySet.get(i)));
            }
            preparedStatement.executeUpdate();
            generatedId = preparedStatement.getGeneratedKeys().getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
            close();
        }

        System.out.println(sql);
        return generatedId;
    }

    public void update(String tableName, HashMap<String, String> parameterMap) {
        try {
            if (!conn.isValid(0)) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //language=SQLite
        String sql = "UPDATE " + tableName + " SET ";

        List<String> keySet = new ArrayList<>(parameterMap.keySet());
        keySet.remove("id"); //we'll grab it later

        for (int i = 0; i < keySet.size(); i++) {
            if (i == keySet.size() - 1) {
                sql = sql + keySet.get(i) + " = ? WHERE id = ? ";
            } else {
                sql = sql + keySet.get(i) + " = ? , ";
            }
        }

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < keySet.size(); i++) {
                preparedStatement.setString(i+1, parameterMap.get(keySet.get(i)));
            }
            preparedStatement.setString(keySet.size()+1,parameterMap.get("id"));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
            close();
        }
    }

    public void delete(String tableName, long id) {
        try {
            if (!conn.isValid(0)) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //language=SQLite
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
            close();
        }
    }

    private ResultSet getResultSet(String sql) {
        connect();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            if (!conn.isValid(0)) {
                connect();
            }
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
