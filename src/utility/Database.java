package utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Database {

    private Connection conn = null;

    public void connect() {
        String path = "jdbc:sqlite:db/ebau.sqlite";

        try {
            conn = DriverManager.getConnection(path);
            verifyTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verifyTables() {
        String carrier = "CREATE TABLE IF NOT EXISTS carrier (\n" +
                "  id   integer PRIMARY KEY,\n" +
                "  name text NOT NULL\n" +
                ");\n";
        String form = "CREATE TABLE IF NOT EXISTS form (\n" +
                "  id             integer PRIMARY KEY,\n" +
                "  name           text NOT NULL,\n" +
                "  form_bytes     blob NOT NULL,\n" +
                "  form_extension text NOT NULL,\n" +
                "  carrier_id     integer,\n" +
                "  FOREIGN KEY (carrier_id) REFERENCES carrier(id)\n" +
                ");\n";
        String formProperties = "CREATE TABLE IF NOT EXISTS form_properties (\n" +
                "  id       integer PRIMARY KEY,\n" +
                "  property text NOT NULL,\n" +
                "  value    text NOT NULL,\n" +
                "  form_id  integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form(id)\n" +
                ");\n";
        String censusHeaders = "CREATE TABLE IF NOT EXISTS census_headers (\n" +
                "  id      integer PRIMARY KEY,\n" +
                "  header  text NOT NULL,\n" +
                "  form_id integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form(id)\n" +
                ");\n";
        String formFields = "CREATE TABLE IF NOT EXISTS form_fields (\n" +
                "  id         integer PRIMARY KEY,\n" +
                "  field_name text NOT NULL,\n" +
                "  form_id    integer,\n" +
                "  FOREIGN KEY (form_id) REFERENCES form (id)\n" +
                ");\n";
        String mapping = "CREATE TABLE IF NOT EXISTS mapping (\n" +
                "  id                integer PRIMARY KEY,\n" +
                "  census_headers_id integer,\n" +
                "  form_fields_id    integer,\n" +
                "  form_id           integer,\n" +
                "  FOREIGN KEY (census_headers_id) REFERENCES census_headers (id),\n" +
                "  FOREIGN KEY (form_fields_id) REFERENCES form_fields (id),\n" +
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
        }
    }

    public ResultSet getById(String table, long id) {
        //language=SQLite
        String sql = "SELECT * FROM " + table + " WHERE id = " + String.valueOf(id);

        return getResultSet(sql);
    }

    public ResultSet list(String table) {
        //language=SQLite
        String sql = "SELECT * FROM " + table;

        return getResultSet(sql);
    }

    public void insert(String table, HashMap<String, String> parameterMap) {
        //language=SQLite
        String sql = "INSERT INTO " + table + " ("/*id, name) VALUES(?,?)"*/;
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

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            for (int i = 0; i < keySet.size(); i++) {
                preparedStatement.setString(i+1, parameterMap.get(keySet.get(i)));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(sql);
    }

    private ResultSet getResultSet(String sql) {
        ResultSet resultSet = null;
        try {
            if (!conn.isValid(0)) {
                connect();
            }
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
