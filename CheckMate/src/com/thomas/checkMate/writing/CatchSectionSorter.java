package com.thomas.checkMate.writing;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: find a better way to sort
public class CatchSectionSorter {
    public static List<PsiCatchSection> sort(List<PsiCatchSection> sectionsToSort) {
        List<PsiCatchSection> sorted = new ArrayList<>();
        for (PsiCatchSection psiCatchSection : sectionsToSort) {
            int index = determineIndex(psiCatchSection, sectionsToSort);
            if (index < 0) {
                sorted.add(psiCatchSection);
            } else {
                sorted.add(index, psiCatchSection);
            }
        }
        return sorted;
    }

    private static int determineIndex(PsiCatchSection toInsert, List<PsiCatchSection> sorted) {
        PsiType catchType = toInsert.getCatchType();
        if (catchType != null) {
            List<PsiType> supers = Arrays.asList(catchType.getSuperTypes());
            for (int i = 0; i < sorted.size(); i++) {
                PsiCatchSection catchSection = sorted.get(i);
                PsiType cType = catchSection.getCatchType();
                if (cType != null) {
                    for (PsiType superType : supers) {
                        if (superType.equals(cType)) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
