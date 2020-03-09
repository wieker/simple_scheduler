package org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.Parser;

public class OptionalCptNode extends NamedCptNode {
    SyntaxTree child;

    public OptionalCptNode(Parser parser, String name, SyntaxTree child) {
        super(parser, name);
        this.child = child;
    }
}
