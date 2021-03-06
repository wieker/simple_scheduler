package org.allesoft.simple_scheduler.parser.grmr.injected.parser;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.LexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public interface Parser {
    SyntaxTree parse(LexerStream lexerStream) throws ParseException;
}
