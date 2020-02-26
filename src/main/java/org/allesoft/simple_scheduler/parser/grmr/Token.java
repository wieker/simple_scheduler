package org.allesoft.simple_scheduler.parser.grmr;

public interface Token {
    default <C extends Token> boolean is(Class<C> klass) {
        return klass.isAssignableFrom(this.getClass());
    }
}
