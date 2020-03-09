package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.IntNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerPoint;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.StringLexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public class IntParser extends NamedCptParser {
    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        LexerPoint begin = lexerStream.begin();
        StringLexerStream stringLexerStream = (StringLexerStream) lexerStream;
        String piece = stringLexerStream.getPiece(1);
        if (!Character.isDigit(piece.charAt(0))) {
            throw new ParseException();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(piece);
        while (true) {
            try {
                piece = stringLexerStream.getPiece(1);
            } catch (ParseException e) {
                piece = "e";
            }
            if (!Character.isDigit(piece.charAt(0))) {
                String result = builder.toString();
                begin.rollback();
                ((StringLexerStream) lexerStream).getPiece(result.length());
                return new IntNode(this, name, Integer.parseInt(result));
            } else {
                builder.append(piece);
            }
        }
    }
}
