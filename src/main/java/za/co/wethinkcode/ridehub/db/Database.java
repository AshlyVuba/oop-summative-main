package za.co.wethinkcode.ridehub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Database() {
    }

    public static Connection connect(String path) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        // SQLite does not enforce foreign keys unless this is switched on for each connection.
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}
