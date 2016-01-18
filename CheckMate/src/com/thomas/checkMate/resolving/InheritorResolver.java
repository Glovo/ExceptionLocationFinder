package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

public class InheritorResolver {
    public static List<PsiClass> resolveInheritors(PsiExpression expression) {
        List<PsiClass> inheritors = new ArrayList<>();
        PsiVariable variable = VariableResolver.resolveVariable(expression);
        if (variable != null) {
            PsiExpression initializer = variable.getInitializer();
            if (initializer != null) {
                List<PsiClass> subInheritors = resolveInheritors(initializer);
                if (subInheritors.size() > 0) {
                    inheritors.addAll(subInheritors);
                } else {
                    PsiType type = initializer.getType();
                    if (type != null) {
                        PsiClass psiClass = ClassUtil.findPsiClass(PsiManager.getInstance(initializer.getProject()), type.getCanonicalText());
                        if (psiClass != null) {
                            inheritors.add(psiClass);
                        }
                    }
                }
            }
        }
        return inheritors;
    }
}
