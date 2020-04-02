package org.allesoft.embedded.configurable.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class InterceptedExperimentTransactionDaoImpl implements InterceptedExperimentDao {
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate txTemplate;

    public InterceptedExperimentTransactionDaoImpl(JdbcTemplate jdbcTemplate, TransactionTemplate txTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.txTemplate = txTemplate;
    }

    @Override
    public void insert(String value) {
        txTemplate.executeWithoutResult(transactionStatus ->
                jdbcTemplate.update("insert into values_tbl (str) values (?)", new Object[] { value }));
    }

    @Override
    public List<String> getValues() {
        return jdbcTemplate.queryForList(" select str from values_tbl", String.class);
    }
}
