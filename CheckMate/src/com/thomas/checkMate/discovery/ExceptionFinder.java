package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionDiscoveringVisitor;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionFinder {
    public static Map<PsiType, Set<DiscoveredExceptionIndicator>> find(Set<PsiCallExpression> expressions, List<ExceptionIndicatorDiscoverer> discovererList, boolean includeJavaSrc) {
        Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
        ExceptionDiscoveringVisitor exceptionDiscoveringVisitor;
        for (PsiExpression expression : expressions) {
            exceptionDiscoveringVisitor = new ExceptionDiscoveringVisitor(expression, discovererList, includeJavaSrc);
            expression.accept(exceptionDiscoveringVisitor);
            mergeResult(discoveredExceptions, exceptionDiscoveringVisitor.getDiscoveredExceptions());
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
