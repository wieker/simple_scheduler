package org.allesoft.embedded.configurable.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class InterceptedExperimentDaoImpl implements InterceptedExperimentDao {
    private JdbcTemplate jdbcTemplate;

    public InterceptedExperimentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(String value) {
        jdbcTemplate.update("insert into values_tbl (str) values (?)", new Object[] { value });
    }

    @Override
    public List<String> getValues() {
        return jdbcTemplate.queryForList(" select str from values_tbl", String.class);
    }
}
