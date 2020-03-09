package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public interface TreeExecutor {
    TreeExecutionResult run(SyntaxTree tree, TreeExecutorProvider treeExecutorProvider,
                            ExecutionContextHolder executionContextHolder);
}
