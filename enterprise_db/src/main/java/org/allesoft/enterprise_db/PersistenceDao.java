package org.allesoft.enterprise_db;

import java.util.List;

public interface PersistenceDao {
    void save(List<String> list, int size);
}
