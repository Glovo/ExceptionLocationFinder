package com.thomas.checkMate.writing;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CatchSectionWriter {
    private final PsiElementFactory elementFactory;

    public CatchSectionWriter(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public List<PsiCatchSection> write(Collection<PsiType> psiTypes) {
        List<PsiCatchSection> psiCatchSections = new ArrayList<>();
        for (PsiType psiType : psiTypes) {
            PsiCatchSection catchSection = elementFactory.createCatchSection(psiType, "e", null);
            psiCatchSections.add(catchSection);
        }
        return psiCatchSections;
    }
}
