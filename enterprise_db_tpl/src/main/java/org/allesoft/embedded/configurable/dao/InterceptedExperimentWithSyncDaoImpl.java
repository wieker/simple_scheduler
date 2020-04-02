package org.allesoft.embedded.configurable.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

public class InterceptedExperimentWithSyncDaoImpl implements InterceptedExperimentDao {
    private JdbcTemplate jdbcTemplate;

    public InterceptedExperimentWithSyncDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(String value) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                System.out.println("tx commited from sync");
            }
        });
        jdbcTemplate.update("insert into values_tbl (str) values (?)", new Object[] { value });
    }

    @Override
    public List<String> getValues() {
        return jdbcTemplate.queryForList(" select str from values_tbl", String.class);
    }
}
