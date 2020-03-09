package org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.Parser;

public class IntNode extends NamedCptNode {
    int value;

    public IntNode(Parser parser, String name, int value) {
        super(parser, name);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
