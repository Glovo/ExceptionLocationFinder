package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.Discovery;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DiscoveryMapUtil {
    public static void addDiscovery(Discovery discovery, Map<PsiType, Set<Discovery>> discoveredExceptions) {
        PsiType psiType = discovery.getExceptionType();
        if (discoveredExceptions.containsKey(psiType)) {
            discoveredExceptions.get(psiType).add(discovery);
        } else {
            Set<Discovery> discoveredThrowStatements = new HashSet<>();
            discoveredThrowStatements.add(discovery);
            discoveredExceptions.put(psiType, discoveredThrowStatements);
        }
    }
}
