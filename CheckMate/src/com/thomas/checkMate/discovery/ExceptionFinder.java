package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExceptionFinder {

    public static Map<PsiType, Set<DiscoveredThrowStatement>> find(Collection<PsiMethodCallExpression> expressions) {
        Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions = new HashMap<>();
        ThrowStatementVisitor throwStatementVisitor;
        for (PsiMethodCallExpression expression : expressions) {
            throwStatementVisitor = new ThrowStatementVisitor(expression);
            mergeResult(discoveredExceptions, throwStatementVisitor.getDiscoveredExceptions());
        }
        return discoveredExceptions;
    }

    private static void mergeResult(Map<PsiType, Set<DiscoveredThrowStatement>> firstMap, Map<PsiType, Set<DiscoveredThrowStatement>> secondMap) {
        for (Map.Entry<PsiType, Set<DiscoveredThrowStatement>> entry : secondMap.entrySet()) {
            PsiType type = entry.getKey();
            if (firstMap.containsKey(type)) {
                firstMap.get(type).addAll(entry.getValue());
            } else {
                firstMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
