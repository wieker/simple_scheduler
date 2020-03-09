package org.allesoft.simple_scheduler.parser.grmr.injected;

import java.util.Arrays;

public class CptRun {
    public static void main(String[] args) throws Exception {
        StringLexerStream stringLexerStream = new StringLexerStream("5+6");
        IntParser intParser = new IntParser();
        intParser.setName("intParser");
        PlusParser plusParser = new PlusParser();
        plusParser.setName("plusParser");
        LinearCptParser plusOp = new LinearCptParser();
        plusOp.setName("plusOp");
        plusOp.setParsers(Arrays.asList(intParser, plusParser, intParser));

        SyntaxTree parse = plusOp.parse(stringLexerStream);

        System.out.println(parse.toString());
    }
}
