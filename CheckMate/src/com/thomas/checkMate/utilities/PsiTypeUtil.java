package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiType;

import java.util.Arrays;
import java.util.List;

public class PsiTypeUtil {
    public static void getDeepSupers(PsiType psiType, List<PsiType> superTypes) {
        List<PsiType> parentTypes = Arrays.asList(psiType.getSuperTypes());
        superTypes.addAll(parentTypes);
        for (PsiType parentType : parentTypes) {
            getDeepSupers(parentType, superTypes);
        }
    }
}
