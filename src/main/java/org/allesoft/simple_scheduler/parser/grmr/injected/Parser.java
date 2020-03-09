package org.allesoft.simple_scheduler.parser.grmr.injected;

public interface Parser {
    SyntaxTree parse(LexerStream lexerStream) throws ParseException;
}
