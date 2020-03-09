package org.allesoft.simple_scheduler.parser.grmr.injected;

public class IntNode extends NamedCptNode {
    int value;

    public IntNode(Parser parser, String name, int value) {
        super(parser, name);
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
