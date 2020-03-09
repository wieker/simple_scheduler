package org.allesoft.simple_scheduler.parser.grmr.injected;

public class PlusParser extends NamedCptParser {
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
