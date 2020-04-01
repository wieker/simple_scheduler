package org.allesoft.embedded;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class CustomNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        System.out.println("handler");
        registerBeanDefinitionParser("custom", new CustomBeanDefinitionParser());
    }
}
