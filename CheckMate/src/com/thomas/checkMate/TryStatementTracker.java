package com.thomas.checkMate;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiDisjunctionType;
import com.intellij.psi.PsiTryStatement;
import com.intellij.psi.PsiType;

import java.util.*;

public class TryStatementTracker {
    private Stack<Set<PsiType>> exceptionTypesStack = new Stack<>();

    public void onTryStatementOpened(PsiTryStatement psiTryStatement) {
        PsiCatchSection[] catchSections = psiTryStatement.getCatchSections();
        exceptionTypesStack.push(getExceptionTypesFromCatchSections(catchSections));
    }

    public void onTryStatementClosed() {
        exceptionTypesStack.pop();
    }

    private Set<PsiType> getExceptionTypesFromCatchSections(PsiCatchSection[] psiCatchSections) {
        Set<PsiType> exceptionTypes = new HashSet<>();
        for (PsiCatchSection psiCatchSection : psiCatchSections) {
            PsiType type = psiCatchSection.getCatchType();
            if (type instanceof PsiDisjunctionType) {
                exceptionTypes.addAll(((PsiDisjunctionType) type).getDisjunctions());
            } else {
                exceptionTypes.add(type);
            }
        }
        Set<PsiType> parentTypes = new HashSet<>();
        for (PsiType psiType : exceptionTypes) {
            parentTypes.addAll(Arrays.asList(psiType.getSuperTypes()));
        }
        exceptionTypes.addAll(parentTypes);
        return exceptionTypes;
    }

    public boolean isNotCaughtByEnclosingCatchSections(PsiType psiType) {
        List<PsiType> typeAndSupers = new ArrayList<>();
        typeAndSupers.add(psiType);
        typeAndSupers.addAll(Arrays.asList(psiType.getSuperTypes()));
        for (Set<PsiType> psiTypes : exceptionTypesStack) {
            for (PsiType type : typeAndSupers) {
                if (psiTypes.contains(type)) {
                    return false;
                }
            }
        }
        return true;
    }
}