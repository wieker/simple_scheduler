package org.allesoft.simple_scheduler.parser.grmr.injected;

public class OptionalCptParser extends NamedCptParser implements Parser {
    Parser next;

    public void setNext(Parser next) {
        this.next = next;
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) {
        LexerPoint lexerPoint = lexerStream.begin();
        SyntaxTree child;
        try {
            child = next.parse(lexerStream);
        } catch (ParseException e) {
            lexerPoint.rollback();
            return new OptionalCptNode(this, name, null);
        }
        return new OptionalCptNode(this, name, child);
    }
}
