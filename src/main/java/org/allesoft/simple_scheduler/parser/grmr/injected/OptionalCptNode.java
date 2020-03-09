package org.allesoft.simple_scheduler.parser.grmr.injected;

public class OptionalCptNode extends NamedCptNode {
    SyntaxTree child;

    public OptionalCptNode(Parser parser, String name, SyntaxTree child) {
        super(parser, name);
        this.child = child;
    }
}
