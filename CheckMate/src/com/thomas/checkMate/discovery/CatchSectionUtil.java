package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiDisjunctionType;
import com.intellij.psi.PsiType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CatchSectionUtil {
    public static Set<PsiType> getExceptionTypes(PsiCatchSection psiCatchSection) {
        Set<PsiType> exceptionTypes = new HashSet<>();
        PsiType type = psiCatchSection.getCatchType();
        if (type instanceof PsiDisjunctionType) {
            exceptionTypes.addAll(((PsiDisjunctionType) type).getDisjunctions());
        } else {
            exceptionTypes.add(type);
        }
        return exceptionTypes;
    }

    public static Set<PsiType> getExceptionTypesWithSupers(PsiCatchSection psiCatchSection) {
        Set<PsiType> exceptionTypes = getExceptionTypes(psiCatchSection);
        Set<PsiType> parentTypes = new HashSet<>();
        for (PsiType psiType : exceptionTypes) {
            parentTypes.addAll(Arrays.asList(psiType.getSuperTypes()));
        }
        exceptionTypes.addAll(parentTypes);
        return exceptionTypes;
    }

    public static Set<PsiType> getExceptionTypes(PsiCatchSection[] psiCatchSections) {
        Set<PsiType> exceptionTypes = new HashSet<>();
        Arrays.asList(psiCatchSections).stream().forEach(e -> exceptionTypes.addAll(getExceptionTypes(e)));
        return exceptionTypes;
    }

    public static Set<PsiType> getExceptionTypesWithSupers(PsiCatchSection[] psiCatchSections) {
        Set<PsiType> exceptionTypes = new HashSet<>();
        Arrays.asList(psiCatchSections).stream().forEach(e -> exceptionTypes.addAll(getExceptionTypesWithSupers(e)));
        return exceptionTypes;
    }
}
