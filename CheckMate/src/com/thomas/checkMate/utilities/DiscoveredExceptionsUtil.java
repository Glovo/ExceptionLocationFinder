package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiThrowStatement;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.DiscoveredThrowStatement;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DiscoveredExceptionsUtil {
    public static Optional<PsiThrowStatement> findThrowStatementOf(PsiMethod psiMethod, Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions) {
        Collection<Set<DiscoveredThrowStatement>> values = discoveredExceptions.values();
        return values.stream().flatMap(Collection::stream).filter(dt -> dt.getEncapsulatingMethod().equals(psiMethod)).map(DiscoveredThrowStatement::getPsiThrowStatement).findFirst();
    }
}
