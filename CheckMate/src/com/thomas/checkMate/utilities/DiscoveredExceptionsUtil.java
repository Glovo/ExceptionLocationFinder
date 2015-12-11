package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DiscoveredExceptionsUtil {
    public static Optional<PsiElement> findIndicatorOf(PsiMethod psiMethod, Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions) {
        Collection<Set<DiscoveredExceptionIndicator>> values = discoveredExceptions.values();
        return values.stream().flatMap(Collection::stream).filter(dt -> dt.getEncapsulatingMethod().equals(psiMethod)).map(DiscoveredExceptionIndicator::getIndicator).findFirst();
    }
}
