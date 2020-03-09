package org.allesoft.simple_scheduler.parser.grmr.injected;

import java.util.ArrayList;
import java.util.List;

public class RepeatCptParser extends NamedCptParser {
    Parser child;

    public void setChild(Parser child) {
        this.child = child;
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        List<SyntaxTree> result = new ArrayList<>();
        while (true) {
            LexerPoint lexerPoint = lexerStream.begin();
            try {
                SyntaxTree syntaxTree = child.parse(lexerStream);
                result.add(syntaxTree);
            } catch (ParseException e) {
                lexerPoint.rollback();
                break;
            }
        }
        if (result.isEmpty()) {
            throw new ParseException();
        }
        return new LinearCptNode(this, name, result);
    }
}
