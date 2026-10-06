package za.co.wethinkcode.ridehub.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These tests build their own copy of the schema and some sample data, so they do
 * not depend on your DatabaseSchema. Only your SQL is being tested here.
 */
class RentalQueriesTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = Database.connect(":memory:");
        try (Statement s = connection.createStatement()) {
            s.execute("CREATE TABLE riders (id INTEGER PRIMARY KEY, full_name TEXT NOT NULL, email TEXT NOT NULL UNIQUE)");
            s.execute("CREATE TABLE bikes (id INTEGER PRIMARY KEY, serial_no TEXT NOT NULL UNIQUE, kind TEXT NOT NULL)");
            s.execute("""
                    CREATE TABLE rentals (
                        id INTEGER PRIMARY KEY,
                        rider_id INTEGER NOT NULL REFERENCES riders(id),
                        bike_id INTEGER NOT NULL REFERENCES bikes(id),
                        started_at TEXT NOT NULL,
                        minutes_used INTEGER)
                    """);

            s.execute("INSERT INTO riders VALUES (1, 'Thandiwe Nkosi', 'thandiwe@example.com')");
            s.execute("INSERT INTO riders VALUES (2, 'Sipho Dlamini', 'sipho@example.com')");
            s.execute("INSERT INTO riders VALUES (3, 'Naledi Mokoena', 'naledi@example.com')");
            s.execute("INSERT INTO riders VALUES (4, 'Kagiso Molefe', 'kagiso@example.com')");

            s.execute("INSERT INTO bikes VALUES (1, 'SB-001', 'STANDARD')");
            s.execute("INSERT INTO bikes VALUES (2, 'SB-002', 'STANDARD')");
            s.execute("INSERT INTO bikes VALUES (3, 'EB-001', 'ELECTRIC')");
            s.execute("INSERT INTO bikes VALUES (4, 'CB-001', 'CARGO')");

            s.execute("INSERT INTO rentals VALUES (1, 1, 1, '2026-01-05 08:00', 12)");
            s.execute("INSERT INTO rentals VALUES (2, 1, 3, '2026-01-06 09:30', 25)");
            s.execute("INSERT INTO rentals VALUES (3, 1, 1, '2026-01-07 17:45', NULL)");
            s.execute("INSERT INTO rentals VALUES (4, 2, 3, '2026-01-06 10:00', 40)");
            s.execute("INSERT INTO rentals VALUES (5, 3, 1, '2026-01-08 07:15', 5)");
            s.execute("INSERT INTO rentals VALUES (6, 3, 3, '2026-01-09 12:00', 30)");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void ridersWithMultipleRentals_listsRidersWithTwoOrMoreRentals_mostRentalsFirst() throws SQLException {
        assertThat(RentalQueries.RIDERS_WITH_MULTIPLE_RENTALS)
                .as("RIDERS_WITH_MULTIPLE_RENTALS has not been written yet")
                .isNotBlank();

        List<String> rows = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(RentalQueries.RIDERS_WITH_MULTIPLE_RENTALS)) {
            while (rs.next()) {
                rows.add(rs.getString("full_name") + ":" + rs.getInt("rental_count"));
            }
        }

        assertThat(rows).containsExactly("Thandiwe Nkosi:3", "Naledi Mokoena:2");
    }

    @Test
    void bikesNeverRented_listsSerialNumbersOfBikesWithNoRentals_inAlphabeticalOrder() throws SQLException {
        assertThat(RentalQueries.BIKES_NEVER_RENTED)
                .as("BIKES_NEVER_RENTED has not been written yet")
                .isNotBlank();

        List<String> serialNumbers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(RentalQueries.BIKES_NEVER_RENTED)) {
            while (rs.next()) {
                serialNumbers.add(rs.getString("serial_no"));
            }
        }

        assertThat(serialNumbers).containsExactly("CB-001", "SB-002");
    }
}
