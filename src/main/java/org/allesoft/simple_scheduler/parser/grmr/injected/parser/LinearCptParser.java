package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.LinearCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

import java.util.ArrayList;
import java.util.List;

public class LinearCptParser extends NamedCptParser {
    final List<Parser> parsers = new ArrayList<>();

    public LinearCptParser() {
    }

    public LinearCptParser(String name) {
        super(name);
    }

    public LinearCptParser(String name, List<Parser> parsers) {
        super(name);
        this.parsers.addAll(parsers);
    }

    // to support injection of the recursive parsers
    public void setParsers(List<Parser> parsers) {
        this.parsers.addAll(parsers);
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        List<SyntaxTree> result = new ArrayList<>(parsers.size());
        for (Parser parser : parsers) {
            result.add(parser.parse(lexerStream));
        }
        return new LinearCptNode(this, name, result);
    }
}
