package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

public interface TreeExecutorProvider {
    TreeExecutor provide(String cptName);

    TreeExecutor provide(SyntaxTree node);
}
