package org.allesoft.simple_scheduler.parser.grmr.injected;

public abstract class NamedCptParser implements Parser {
    protected String name;

    public void setName(String name) {
        this.name = name;
    }
}
