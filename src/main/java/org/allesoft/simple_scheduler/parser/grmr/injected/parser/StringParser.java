package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public class StringParser extends NamedCptParser {
    public StringParser() {
    }

    public StringParser(String name) {
        super(name);
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        return null;
    }
}
