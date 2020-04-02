package org.allesoft.embedded.configurable.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InterceptedExperimentJdbcDaoImpl implements InterceptedExperimentDao {
    private DataSource dataSource;

    public InterceptedExperimentJdbcDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(String value) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            // injection. for test purposes only
            statement.execute("insert into values_tbl (str) values ('zzz')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getValues() {
        return new ArrayList<>();
    }
}
