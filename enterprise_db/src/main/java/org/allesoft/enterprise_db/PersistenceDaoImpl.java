package org.allesoft.enterprise_db;

import java.util.List;

public class PersistenceDaoImpl implements PersistenceDao {
    @Override
    public void save(List<String> list, int size) {
        System.out.println("size: " + size);
        list.forEach(System.out::println);
    }
}
