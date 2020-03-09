package org.allesoft.simple_scheduler.parser.linear;

import org.allesoft.simple_scheduler.parser.grmr.Parser;

import java.util.List;
import java.util.Map;

public class LinearParser implements Parser {
    List<Parser> parsers;

    public LinearParser(Context context) {
        parsers = context.getByType("linear");
    }

    public LinearParser(Map<String, List<Parser>> context) {
        parsers = context.get("linearParsers");
        new Injector().inject(null, parsers);
    }
}
