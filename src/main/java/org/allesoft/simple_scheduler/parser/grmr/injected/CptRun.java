package org.allesoft.simple_scheduler.parser.grmr.injected;

import org.allesoft.simple_scheduler.parser.grmr.injected.parser.IntParser;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.LinearCptParser;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.PlusParser;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.iostream.StringLexerStream;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.IntNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.LinearCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.NamedCptNode;
import org.allesoft.simple_scheduler.parser.grmr.injected.parser.tree.SyntaxTree;

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

        SimpleTreeExecutorProvider provider = new SimpleTreeExecutorProvider();
        provider.registerExecutor("intParser", (tree, treeExecutorProvider, executionContextHolder) -> new IntegerExecutionResult(((IntNode) tree).getValue()));
        provider.registerExecutor("plusOp", (tree, treeExecutorProvider, executionContextHolder) ->
                new IntegerExecutionResult(((LinearCptNode) tree).getChilds().stream()
                        .map(c -> treeExecutorProvider.provide(((NamedCptNode) c).getName()).run(c, treeExecutorProvider, executionContextHolder))
                        .filter(c -> c instanceof IntegerExecutionResult)
                        .map(c -> (IntegerExecutionResult) c)
                        .map(IntegerExecutionResult::getResult)
                        .reduce(0, Integer::sum)));

        SyntaxTree parse = plusOp.parse(stringLexerStream);

        TreeExecutionResult treeExecutionResult = provider.provide(parse).run(parse, provider, null);

        System.out.println(parse.toString());

        System.out.println(((IntegerExecutionResult) treeExecutionResult).getResult());
    }

}
