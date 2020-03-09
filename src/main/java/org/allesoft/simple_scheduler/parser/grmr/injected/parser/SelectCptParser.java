package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerPoint;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.OptionalCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

import java.util.ArrayList;
import java.util.List;

public class SelectCptParser extends NamedCptParser {
    final List<Parser> parsers = new ArrayList<>();

    public SelectCptParser() {
    }

    public SelectCptParser(String name, List<Parser> parsers) {
        super(name);
        this.parsers.addAll(parsers);
    }

    // to support injection of the recursive parsers
    public void setParsers(List<Parser> parsers) {
        this.parsers.addAll(parsers);
    }

    @Override
    public SyntaxTree parse(LexerStream lexerStream) throws ParseException {
        LexerPoint lexerPoint = lexerStream.begin();
        SyntaxTree child = null;
        for (Parser parser : parsers) {
            try {
                child = parser.parse(lexerStream);
                break;
            } catch (ParseException e) {
                lexerPoint.rollback();
            }
        }
        if (child == null) {
            throw new ParseException();
        }
        return new OptionalCptNode(this, name, child);
    }
}
