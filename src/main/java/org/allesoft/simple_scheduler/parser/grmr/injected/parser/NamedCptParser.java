package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.Parser;

public abstract class NamedCptParser implements Parser {
    protected String name;

    public NamedCptParser() {
    }

    public NamedCptParser(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
