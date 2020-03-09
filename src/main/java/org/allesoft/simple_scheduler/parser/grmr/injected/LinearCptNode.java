package org.allesoft.simple_scheduler.parser.grmr.injected;

import java.util.ArrayList;
import java.util.List;

public class LinearCptNode extends NamedCptNode {
    final List<SyntaxTree> childs = new ArrayList<>();

    public LinearCptNode(Parser parser, String name, List<SyntaxTree> childs) {
        super(name, parser);
        this.childs.addAll(childs);
    }
}
