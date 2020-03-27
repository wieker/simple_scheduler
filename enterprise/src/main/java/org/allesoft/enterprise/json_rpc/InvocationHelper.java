package org.allesoft.enterprise.json_rpc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class InvocationHelper implements MethodInterceptor, InitializingBean, FactoryBean<Object>, ApplicationContextAware {
    private Hello helloProxy;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Object: " + invocation.getStaticPart());
        System.out.println("Method: " + invocation.getMethod().getName());
        return "test";
    }

    @Override
    public Object getObject() throws Exception {
        return helloProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return Hello.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        helloProxy = ProxyFactory.getProxy(Hello.class, this);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
