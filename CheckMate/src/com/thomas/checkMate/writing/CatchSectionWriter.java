package com.thomas.checkMate.writing;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CatchSectionWriter {
    private final PsiElementFactory elementFactory;

    public CatchSectionWriter(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public List<PsiCatchSection> write(Collection<PsiType> psiTypes) {
        return psiTypes.stream().filter(Objects::nonNull).
                map(psiType -> elementFactory.createCatchSection(psiType, "e", null))
                .collect(Collectors.toList());
    }
}
