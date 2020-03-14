package org.allesoft.simple_scheduler.rmi;

public class RMIServiceImpl implements RMIService {
    @Override
    public void message() {
        System.out.println("message is received");
    }
}
