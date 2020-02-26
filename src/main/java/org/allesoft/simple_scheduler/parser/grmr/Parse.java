package org.allesoft.simple_scheduler.parser.grmr;

public class Parse implements Parser {
    Tokenizer tokenizer;

    STree parse() {
        Token token = tokenizer.getToken();
        if (token.is(NumberToken.class)) {

        }
        return null;
    }

    public static void main(String[] args) {
        NumberToken numberToken = new NumberToken();
        System.out.println(numberToken.is(NumberToken.class));
    }
}
