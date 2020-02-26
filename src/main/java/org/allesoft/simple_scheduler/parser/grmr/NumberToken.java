package org.allesoft.simple_scheduler.parser.grmr;

public class NumberToken implements Token {
    @Override
    public <C extends Token> boolean is(Class<C> klass) {
        return klass.isAssignableFrom(this.getClass());
    }
}
