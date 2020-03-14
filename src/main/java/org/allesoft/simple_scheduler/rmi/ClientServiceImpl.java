package org.allesoft.simple_scheduler.rmi;

public class ClientServiceImpl {
    private RMIService rmiService;

    public void setRmiService(RMIService rmiService) {
        this.rmiService = rmiService;
    }

    public void message() {
        rmiService.message();
    }
}
