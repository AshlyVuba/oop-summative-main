package za.co.wethinkcode.ridehub.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchema {

    private DatabaseSchema() {
    }

    public static void createSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // TODO: create the `riders`, `bikes` and `rentals` tables described in
            // resources/erd.png, including every primary key, NOT NULL and UNIQUE
            // constraint, and both foreign keys on `rentals`.
            //
            // Execute one CREATE TABLE statement per table, like this:
            //
            // statement.execute("""
            //         CREATE TABLE example (id INTEGER PRIMARY KEY, name TEXT NOT NULL)
            //         """);
        }
    }
}
