package org.allesoft.embedded.configurable.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InterceptedExperimentDaoTest {
    private InterceptedExperimentDao interceptedExperimentDao;
    private InterceptedExperimentDao txDao;
    private InterceptedExperimentDao proxyDao;
    private InterceptedExperimentDao customTxDao;

    @Before
    public void setup() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("test-context.xml");
        applicationContext.refresh();
        interceptedExperimentDao = (InterceptedExperimentDao) applicationContext.getBean("dao");
        txDao = (InterceptedExperimentDao) applicationContext.getBean("txDao");
        proxyDao = (InterceptedExperimentDao) applicationContext.getBean("proxyDao");
        customTxDao = (InterceptedExperimentDao) applicationContext.getBean("customTxDao");
    }

    @Test
    public void check() {
        checkDao(interceptedExperimentDao);
        checkDao(txDao);
        checkDao(proxyDao);
        checkDao(customTxDao);
    }

    private void checkDao(InterceptedExperimentDao dao) {
        dao.insert("aaa");
        dao.insert("bbb");
        dao.getValues().forEach(System.out::println);
    }

}
