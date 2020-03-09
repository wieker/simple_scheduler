package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.IntNode;

class IntegerExecutionResult implements TreeExecutionResult {
    int result;

    public int getResult() {
        return result;
    }

    public IntegerExecutionResult(int value) {
        result = value;
    }
}
