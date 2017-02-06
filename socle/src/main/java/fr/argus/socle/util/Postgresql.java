package fr.argus.socle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Postgresql {

    private static final Logger LOG = LoggerFactory.getLogger(Postgresql.class);
    private final Connection connection;

    public Postgresql(Connection connection) {
        this.connection = assertNotNull(connection, "A connection is required");
        assertValidConnection(connection);
    }

    public Postgresql dropNotNulls(String... tables) {
    	assertNotNull(tables, "tables is required");

        try {
            for (String table : tables) {
                for (String column : findNotNullColumns(table)) {
                    LOG.info("Disabling not-null ON column '{}' of table '{}'", column, table);
                    executeSql("ALTER TABLE " + table + " ALTER " + column + " DROP NOT NULL");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Postgresql dropPrimaryKeys(String... tables) {
    	assertNotNull(tables, "tables is required");

        try {
            for (String table : tables) {
                for (String primaryKey : findPrimaryKeyConstraints(table)) {
                    LOG.info("Disabling primary key '{}' of table '{}'", primaryKey, table);
                    executeSql("ALTER TABLE " + table + " DROP CONSTRAINT " + primaryKey + " CASCADE");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Postgresql dropForeignKeys(String... tables) {
    	assertNotNull(tables, "tables is required");

        try {
            for (String table : tables) {
                for (String foreignKey : findForeignKeyConstraints(table)) {
                    LOG.info("Disabling foreign key '{}' of table '{}'", foreignKey, table);
                    executeSql("ALTER TABLE " + table + " DROP CONSTRAINT " + foreignKey);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Postgresql dropAll(String... tables) {
    	assertNotNull(tables, "tables is required");

        dropForeignKeys(tables);
        dropPrimaryKeys(tables);
        dropNotNulls(tables);

        return this;
    }

    private List<String> findNotNullColumns(String table) throws SQLException {
        List<String> notNullsColumns = new ArrayList<>();

        String sql = "SELECT DISTINCT column_name FROM INFORMATION_SCHEMA.COLUMNS" +
                " WHERE column_name IS NOT NULL" +
                " AND columns.is_nullable='NO'" + 
                getCritereTableName(table);

        try {
            notNullsColumns.addAll(getFirstColumnValues(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }

        // Cannot drop-non-null from columns part of a primary key
        notNullsColumns.removeAll(findPrimaryKeyColumns(table));

        return notNullsColumns;
    }

    private List<String> findPrimaryKeyColumns(String table) throws SQLException {
        return getFirstColumnValues("SELECT a.attname" +
                " FROM pg_index i JOIN pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY (i.indkey)" +
                " WHERE i.indrelid = '" + table + "'::REGCLASS AND i.indisprimary");

    }

    private List<String> findPrimaryKeyConstraints(String table) throws SQLException {
        return getFirstColumnValues("SELECT constraint_name" +
                " FROM information_schema.table_constraints" +
                " WHERE constraint_type = 'PRIMARY KEY'" + 
                getCritereTableName(table));
    }

    private List<String> findForeignKeyConstraints(String table) throws SQLException {
        return getFirstColumnValues("SELECT constraint_name" +
                " FROM information_schema.table_constraints" +
                " WHERE constraint_type = 'FOREIGN KEY'" + 
                getCritereTableName(table));
    }

    private String getCritereTableName(String table) {
    	return table.split("\\.", 2).length > 1 
        		? " AND table_schema||'.'||table_name='" + table + "'" 
        		: " AND table_schema||'.'||table_name='public." + table + "'" ;
    }
    
    private List<String> getFirstColumnValues(String sql) throws SQLException {
        List<String> values = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery(sql);
        while (rs.next()) {
            values.add(rs.getString(1));
        }
        return values;
    }

    private void executeSql(String sql) {
        try {
            LOG.debug("Executing sql: {}", sql);
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    private void assertValidConnection(Connection connection) {
        try {
            if (connection.isClosed()) {
                throw new RuntimeException("Connection is already closed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T assertNotNull(T ref, String errorMsg) {
        if (ref == null) {
            throw new IllegalArgumentException(errorMsg);
        }

        return ref;
    }
}
