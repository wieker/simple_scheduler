package org.allesoft.simple_scheduler.parser.grmr.injected;

import java.util.ArrayList;
import java.util.List;

public class LinearCptParser extends NamedCptParser {
    final List<Parser> parsers = new ArrayList<>();

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
