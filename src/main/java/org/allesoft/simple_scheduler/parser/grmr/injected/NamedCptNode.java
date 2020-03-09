package org.allesoft.simple_scheduler.parser.grmr.injected;

public class NamedCptNode implements SyntaxTree {
    final String name;
    final Parser parser;

    public NamedCptNode(String name, Parser parser) {
        this.name = name;
        this.parser = parser;
    }
}
