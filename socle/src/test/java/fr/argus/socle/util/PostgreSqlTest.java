package fr.argus.socle.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Test;

import fr.argus.socle.db.DataSourceManager;

public class PostgreSqlTest {

	@AfterClass
	public static void afterClass() throws SQLException {
		new PostgreSqlTest().executeSql(	"drop SCHEMA if exists myschema CASCADE",
					"drop table if exists foreignforeigntable",
					"drop table if exists foreigntable",
					"drop table if exists srctable",
					"drop table if exists mytable2",
					"drop table if exists mytable");
	}
	
    @Test
    public void drop_not_nulls_on_single_table_with_schema() throws Exception {
    	executeSql("drop SCHEMA if exists myschema CASCADE", "create SCHEMA myschema");
        executeSql("drop table if exists myschema.mytable", "create table myschema.mytable (a int not null)");

        try (Connection connection = openConnection()) {
            new Postgresql(connection).dropNotNulls("myschema.mytable");
            connection.createStatement().execute("insert into myschema.mytable(a) values (null)");
        }
    }
    
    @Test
    public void drop_not_nulls_on_single_table() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null)");

        try (Connection connection = openConnection()) {
            new Postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a) values (null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_many_not_nulls() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null, b text not null)");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (null,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_primary_key() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int primary key not null, b int not null)");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b) values (1, null)");
        }
    }

    @Test
    public void drop_not_nulls_on_single_table_with_composite_primary_key() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int not null, b int not null, c int not null, primary key (a,b))");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropNotNulls("mytable");
            connection.createStatement().execute("insert into mytable(a,b,c) values (1,1,null)");
        }
    }

    @Test
    public void drop_not_nulls_on_multiple_tables() throws Exception {
        executeSql(
                "drop table if exists mytable", "create table mytable (a int not null, b text not null)",
                "drop table if exists mytable2", "create table mytable2 (c int not null, d text not null)");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropNotNulls("mytable", "mytable2");
            connection.createStatement().execute("insert into mytable(a,b) values (null, null)");
            connection.createStatement().execute("insert into mytable2(c,d) values (null, null)");
        }
    }

    @Test
    public void drop_primary_keys_on_single_table() throws Exception {
        executeSql("drop table if exists mytable", "create table mytable (a int primary key)");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropPrimaryKeys("mytable");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
        }
    }

    @Test
    public void drop_primary_keys_on_multiple_tables() throws Exception {
    	executeSql("drop SCHEMA if exists myschema CASCADE", "create SCHEMA myschema");
        executeSql(
                "drop table if exists mytable", "create table mytable (a int primary key)",
                "drop table if exists myschema.mytable2", "create table myschema.mytable2 (b int primary key)");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropPrimaryKeys("mytable", "myschema.mytable2");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into mytable(a) values (1)");
            connection.createStatement().execute("insert into myschema.mytable2(b) values (1)");
            connection.createStatement().execute("insert into myschema.mytable2(b) values (1)");
        }
    }

    @Test
    public void drop_composed_primary_key() throws Exception {
        executeSql("drop table if exists mytable",
                "create table mytable (a int, b int, constraint pk primary key (a, b))");

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropPrimaryKeys("mytable");
            connection.createStatement().execute("insert into mytable(a, b) values (1, 1)");
            connection.createStatement().execute("insert into mytable(a, b) values (1, 1)");
        }
    }

    @Test
    public void drop_foreign_keys_on_single_table() throws Exception {
        executeSql(
                "drop table if exists foreigntable", "drop table if exists srctable",
                "create table srctable (id int primary key)",
                "create table foreigntable (fk int, foreign key (fk) REFERENCES srctable(id))"
        );

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropForeignKeys("foreigntable");
            connection.createStatement().execute("insert into foreigntable(fk) values (1)");
        }
    }

    @Test
    public void drop_foreign_keys_on_multiples_tables() throws Exception {
    	executeSql("drop SCHEMA if exists myschema CASCADE", "create SCHEMA myschema");
        executeSql(
                "drop table if exists foreigntable", "drop table if exists myschema.srctable",
                "create table myschema.srctable (id int primary key, col int not null)",
                "create table foreigntable (fk int primary key, foreign key (fk) REFERENCES myschema.srctable(id))"
        );

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropAll("foreigntable", "myschema.srctable");
            connection.createStatement().execute("insert into myschema.srctable(id, col) values (null, null)");
            connection.createStatement().execute("insert into foreigntable(fk) values (null)");
        }
    }

    @Test
    public void drop_all() throws Exception {
        executeSql(
                "drop table if exists foreignforeigntable", "drop table if exists foreigntable", "drop table if exists srctable",
                "create table srctable (id int primary key)",
                "create table foreigntable (fk int primary key, foreign key (fk) REFERENCES srctable(id))",
                "create table foreignforeigntable (fkfk int, foreign key (fkfk) REFERENCES foreigntable(fk))"
        );

        try (Connection connection = openConnection()) {
        	new Postgresql(connection).dropForeignKeys("foreignforeigntable", "foreigntable");
            connection.createStatement().execute("insert into foreignforeigntable(fkfk) values (1)");
            connection.createStatement().execute("insert into foreigntable(fk) values (1)");
        }
    }

    @Test(expected = RuntimeException.class)
    public void closed_connection() throws Exception {
        Connection connection = mock(Connection.class);
        given(connection.isClosed()).willReturn(true);

        new Postgresql(connection);
    }

    @Test
    public void no_op_given_no_table_given() throws Exception {
        Connection connection = mock(Connection.class);

        new Postgresql(connection)
                .dropNotNulls()
                .dropPrimaryKeys()
                .dropForeignKeys()
                .dropAll();

        verify(connection).isClosed();
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void drop_not_nulls_given_null_tables() throws Exception {
        try {
        	new Postgresql(mock(Connection.class)).dropNotNulls((String[]) null);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void drop_not_primary_keys_given_null_tables() throws Exception {
        try {
        	new Postgresql(mock(Connection.class)).dropPrimaryKeys((String[]) null);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void drop_not_foreign_keys_given_null_tables() throws Exception {
        try {
        	new Postgresql(mock(Connection.class)).dropForeignKeys((String[]) null);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void drop_all_given_null_tables() throws Exception {
        try {
        	new Postgresql(mock(Connection.class)).dropAll((String[]) null);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Connection openConnection() throws SQLException {
        return DataSourceManager.getInstance().getConnection();
    }

    private void executeSql(String... sqls) throws SQLException {
        try (Connection conn = openConnection()) {
            for (String sql : sqls) {
                conn.createStatement().execute(sql);
            }
        }
    }
}