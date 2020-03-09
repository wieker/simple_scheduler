package org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.Parser;

public class NamedCptNode implements SyntaxTree {
    final String name;
    final Parser parser;

    public NamedCptNode(Parser parser, String name) {
        this.name = name;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return name;
    }
}
