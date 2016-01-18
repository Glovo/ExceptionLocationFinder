package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodTracker {
    private Map<PsiMethod, Set<Discovery>> openMethods = new HashMap<>();
    private Map<PsiMethod, Set<Discovery>> visitedMethods = new HashMap<>();

    public void openMethod(PsiMethod method) {
        if (alreadyOpened(method)) {
            throw new IllegalArgumentException(method + " has already been opened");
        }
        openMethods.put(method, new HashSet<>());
    }

    public void closeMethod(PsiMethod method) {
        if (!alreadyOpened(method)) {
            throw new IllegalArgumentException(method + " needs to be opened first");
        }
        if (alreadyVisited(method)) {
            throw new IllegalArgumentException(method + " has been processed earlier");
        }
        visitedMethods.put(method, openMethods.get(method));
        openMethods.remove(method);
    }

    public boolean alreadyOpened(PsiMethod method) {
        return openMethods.containsKey(method);
    }

    public boolean alreadyVisited(PsiMethod method) {
        return visitedMethods.containsKey(method);
    }

    public Set<Discovery> getResult(PsiMethod method) {
        if (!alreadyVisited(method)) {
            throw new IllegalArgumentException(method + " has not been visited yet");
        }
        return visitedMethods.get(method);
    }

    public void newDiscovery(Discovery discovery) {
        openMethods.entrySet().stream().forEach(e -> e.getValue().add(discovery));
    }
}
