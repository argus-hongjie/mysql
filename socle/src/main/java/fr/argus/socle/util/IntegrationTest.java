package fr.argus.socle.util;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import fr.argus.socle.db.DataSourceManager;
import lombok.Cleanup;

public class IntegrationTest {
	
	protected static DataSourceManager postgres = DataSourceManager.getInstance();
	protected static PlatformTransactionManager postgresTxMgr = postgres.getTxManager();
	protected TransactionStatus postgresStatus;
	
    @Before
    public void before() throws Exception {
        postgresStatus = postgresTxMgr.getTransaction(new DefaultTransactionDefinition());
        
        String[] tables = {"referentiel.ressources", "referentiel.modalite_appro", "production.logs"};
        @Cleanup Connection con = postgres.getConnection();
        new Postgresql(con).dropAll(tables);
    }
    
	@After
	public void after() {
		if (!postgresStatus.isCompleted()) postgresTxMgr.rollback(postgresStatus);
	}
}
