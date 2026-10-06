package za.co.wethinkcode.ridehub.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseSchemaTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = Database.connect(":memory:");
        DatabaseSchema.createSchema(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    // ---- riders ---------------------------------------------------------

    @Test
    void ridersTable_hasTheColumnsAndTypesInTheErd() throws SQLException {
        assertThat(columnTypes("riders"))
                .containsEntry("id", "INTEGER")
                .containsEntry("full_name", "TEXT")
                .containsEntry("email", "TEXT")
                .hasSize(3);
    }

    @Test
    void ridersTable_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("riders", "pk")).containsEntry("id", true);
    }

    @Test
    void ridersTable_fullNameAndEmailAreNotNull() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("riders", "notnull");
        assertThat(notNull).containsEntry("full_name", true);
        assertThat(notNull).containsEntry("email", true);
    }

    @Test
    void ridersTable_emailMustBeUnique() throws SQLException {
        insertRider(1, "Thandiwe Nkosi", "thandiwe@example.com");
        assertThrows(SQLException.class, () -> insertRider(2, "Sipho Dlamini", "thandiwe@example.com"));
    }

    // ---- bikes ----------------------------------------------------------

    @Test
    void bikesTable_hasTheColumnsAndTypesInTheErd() throws SQLException {
        assertThat(columnTypes("bikes"))
                .containsEntry("id", "INTEGER")
                .containsEntry("serial_no", "TEXT")
                .containsEntry("kind", "TEXT")
                .hasSize(3);
    }

    @Test
    void bikesTable_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("bikes", "pk")).containsEntry("id", true);
    }

    @Test
    void bikesTable_serialNoAndKindAreNotNull() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("bikes", "notnull");
        assertThat(notNull).containsEntry("serial_no", true);
        assertThat(notNull).containsEntry("kind", true);
    }

    @Test
    void bikesTable_serialNoMustBeUnique() throws SQLException {
        insertBike(1, "SB-001", "STANDARD");
        assertThrows(SQLException.class, () -> insertBike(2, "SB-001", "ELECTRIC"));
    }

    // ---- rentals --------------------------------------------------------

    @Test
    void rentalsTable_hasTheColumnsAndTypesInTheErd() throws SQLException {
        assertThat(columnTypes("rentals"))
                .containsEntry("id", "INTEGER")
                .containsEntry("rider_id", "INTEGER")
                .containsEntry("bike_id", "INTEGER")
                .containsEntry("started_at", "TEXT")
                .containsEntry("minutes_used", "INTEGER")
                .hasSize(5);
    }

    @Test
    void rentalsTable_hasIdAsPrimaryKey() throws SQLException {
        assertThat(columnFlags("rentals", "pk")).containsEntry("id", true);
    }

    @Test
    void rentalsTable_riderIdBikeIdAndStartedAtAreNotNull() throws SQLException {
        Map<String, Boolean> notNull = columnFlags("rentals", "notnull");
        assertThat(notNull).containsEntry("rider_id", true);
        assertThat(notNull).containsEntry("bike_id", true);
        assertThat(notNull).containsEntry("started_at", true);
    }

    @Test
    void rentalsTable_minutesUsedIsOptional_becauseARideMayStillBeInProgress() throws SQLException {
        assertThat(columnFlags("rentals", "notnull")).containsEntry("minutes_used", false);
    }

    @Test
    void rentalsTable_declaresBothForeignKeys() throws SQLException {
        Map<String, String> references = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA foreign_key_list(rentals)")) {
            while (resultSet.next()) {
                references.put(resultSet.getString("from"),
                        resultSet.getString("table") + "." + resultSet.getString("to"));
            }
        }
        assertThat(references)
                .containsEntry("rider_id", "riders.id")
                .containsEntry("bike_id", "bikes.id")
                .hasSize(2);
    }

    @Test
    void database_rejectsARentalForARiderThatDoesNotExist() throws SQLException {
        insertBike(1, "SB-001", "STANDARD");
        assertThrows(SQLException.class, () -> insertRental(1, 999, 1));
    }

    @Test
    void database_rejectsARentalForABikeThatDoesNotExist() throws SQLException {
        insertRider(1, "Thandiwe Nkosi", "thandiwe@example.com");
        assertThrows(SQLException.class, () -> insertRental(1, 1, 999));
    }

    @Test
    void database_acceptsAValidRental() throws SQLException {
        insertRider(1, "Thandiwe Nkosi", "thandiwe@example.com");
        insertBike(1, "SB-001", "STANDARD");
        insertRental(1, 1, 1);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM rentals")) {
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getInt(1)).isEqualTo(1);
        }
    }

    // ---- helpers --------------------------------------------------------

    private void insertRider(int id, String fullName, String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO riders (id, full_name, email) VALUES (?, ?, ?)")) {
            statement.setInt(1, id);
            statement.setString(2, fullName);
            statement.setString(3, email);
            statement.executeUpdate();
        }
    }

    private void insertBike(int id, String serialNo, String kind) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO bikes (id, serial_no, kind) VALUES (?, ?, ?)")) {
            statement.setInt(1, id);
            statement.setString(2, serialNo);
            statement.setString(3, kind);
            statement.executeUpdate();
        }
    }

    private void insertRental(int id, int riderId, int bikeId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO rentals (id, rider_id, bike_id, started_at) VALUES (?, ?, ?, '2026-01-05 08:00')")) {
            statement.setInt(1, id);
            statement.setInt(2, riderId);
            statement.setInt(3, bikeId);
            statement.executeUpdate();
        }
    }

    private Map<String, String> columnTypes(String table) throws SQLException {
        Map<String, String> types = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (resultSet.next()) {
                types.put(resultSet.getString("name"), resultSet.getString("type").toUpperCase());
            }
        }
        return types;
    }

    private Map<String, Boolean> columnFlags(String table, String flagColumn) throws SQLException {
        Map<String, Boolean> flags = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (resultSet.next()) {
                flags.put(resultSet.getString("name"), resultSet.getInt(flagColumn) > 0);
            }
        }
        return flags;
    }
}
