package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.NamedCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleTreeExecutorProvider implements TreeExecutorProvider {
    Map<String, TreeExecutor> executorsArray = new HashMap<>();

    @Override
    public TreeExecutor provide(String cptName) {
        return Optional.ofNullable(executorsArray.get(cptName)).orElse((tree, treeExecutorProvider, executionContextHolder) -> new TreeExecutionResult() {});
    }

    @Override
    public TreeExecutor provide(SyntaxTree node) {
        NamedCptNode namedCptNode = (NamedCptNode) node;
        return provide(namedCptNode.getName());
    }

    public void registerExecutor(String cptName, TreeExecutor executor) {
        executorsArray.put(cptName, executor);
    }
}
