package org.allesoft.enterprise.json_rpc;

public class HelloImpl implements Hello {
    @Override
    public void message() {
        System.out.println("called");
    }
}
