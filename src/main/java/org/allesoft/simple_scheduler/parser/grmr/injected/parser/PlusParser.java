package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.NamedCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.StringLexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public class PlusParser extends NamedCptParser {
    public PlusParser() {
    }

    public PlusParser(String name) {
        super(name);
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        StringLexerStream stringLexerStream = (StringLexerStream) lexerStream;
        String piece = stringLexerStream.getPiece(1);
        if ('+' != piece.charAt(0)) {
            throw new ParseException();
        }
        return new NamedCptNode(this, name);
    }
}
