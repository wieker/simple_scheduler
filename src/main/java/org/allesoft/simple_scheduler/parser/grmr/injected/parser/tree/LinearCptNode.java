package org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class LinearCptNode extends NamedCptNode {
    final List<SyntaxTree> childs = new ArrayList<>();

    public LinearCptNode(Parser parser, String name, List<SyntaxTree> childs) {
        super(parser, name);
        this.childs.addAll(childs);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(name + " { ");
        childs.forEach(c -> str.append(" ").append(c.toString()));
        str.append(" } ");
        return str.toString();
    }
}
