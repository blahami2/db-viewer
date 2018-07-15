package cz.blahami2.dbviewer.data;

import lombok.RequiredArgsConstructor;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RequiredArgsConstructor
public class DatabaseConnection {

    private final String driver;
    private final String hostWithPort;
    private final String username;
    private final String password;
    private final String dbName;

    /**
     * @param selectSqlCommand SQL SELECT clause
     * @param valueFactory     extract single object from {@link ResultSet} (valid cursor)
     * @param <T>              return type
     * @return list of T objects
     * @throws SQLException thrown when an error occurs
     */
    public <T> List<T> getList(String selectSqlCommand, SqlFunction<ResultSet, T> valueFactory) throws SQLException {
        return getListFromStatement(statement -> {
            try (ResultSet resultSet = statement.executeQuery(selectSqlCommand)) {
                List<T> schemas = new ArrayList<>();
                while (resultSet.next()) {
                    schemas.add(valueFactory.apply(resultSet));
                }
                return schemas;
            }
        });
    }

    private <T> List<T> getListFromStatement(SqlFunction<Statement, List<T>> dataExtractor) throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", username);
        connectionProps.put("password", password);
        String connectionString = "jdbc:" + driver + "://" + hostWithPort + "/" + dbName;
        try (java.sql.Connection con = DriverManager.getConnection(connectionString, connectionProps)) {
            try (Statement statement = con.createStatement()) {
                return dataExtractor.apply(statement);
            }
        }
    }

    public interface SqlFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
