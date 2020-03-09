package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

import java.util.List;

public interface StdCptTreeExecutorService extends TreeExecutor {
    List<TreeExecutionResult> executeSubTrees(SyntaxTree node);
}
