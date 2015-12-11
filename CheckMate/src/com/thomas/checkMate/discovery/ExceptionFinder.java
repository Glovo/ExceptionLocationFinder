package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ThrowStatementVisitor;

import java.util.*;

public class ExceptionFinder {

    public static Map<PsiType, Set<DiscoveredExceptionIndicator>> find(Collection<PsiMethodCallExpression> expressions, List<ExceptionIndicatorDiscoverer> discovererList) {
        Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
        ThrowStatementVisitor throwStatementVisitor;
        for (PsiMethodCallExpression expression : expressions) {
            throwStatementVisitor = new ThrowStatementVisitor(expression, discovererList);
            mergeResult(discoveredExceptions, throwStatementVisitor.getDiscoveredExceptions());
        }
        return discoveredExceptions;
    }

    private static void mergeResult(Map<PsiType, Set<DiscoveredExceptionIndicator>> firstMap, Map<PsiType, Set<DiscoveredExceptionIndicator>> secondMap) {
        for (Map.Entry<PsiType, Set<DiscoveredExceptionIndicator>> entry : secondMap.entrySet()) {
            PsiType type = entry.getKey();
            if (firstMap.containsKey(type)) {
                firstMap.get(type).addAll(entry.getValue());
            } else {
                firstMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
