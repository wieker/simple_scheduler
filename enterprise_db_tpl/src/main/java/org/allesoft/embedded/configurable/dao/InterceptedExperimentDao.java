package org.allesoft.embedded.configurable.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface InterceptedExperimentDao {
    void insert(String value);

    List<String> getValues();
}
