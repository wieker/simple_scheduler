package org.allesoft.embedded.infrastructure;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class JdbcInterceptor implements MethodInterceptor {
    private List<String> autoCommitableMethods;
    private TransactionTemplate transactionTemplate;

    public void setAutoCommitableMethods(List<String> autoCommitableMethods) {
        this.autoCommitableMethods = autoCommitableMethods;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (isAutoCommitableMethod(invocation.getMethod().getName())) {
            return transactionTemplate.execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus transactionStatus) {
                    System.out.println("custom tx invoked");
                    try {
                        Object retVal = invocation.proceed();
                        System.out.println("custom tx finished");
                        return retVal;
                    } catch (Throwable t) {
                        System.out.println("custom tx failed");
                        throw new RuntimeException(t);
                    }
                }
            });
        } else {
            return invocation.proceed();
        }
    }

    private boolean isAutoCommitableMethod(String methodName) {
        boolean isAutoCommitable = false;
        for (String autoCommitableMethod : autoCommitableMethods) {
            if (autoCommitableMethod.equals(methodName)) {
                isAutoCommitable = true;
                break;
            }
        }
        return isAutoCommitable;
    }
}
