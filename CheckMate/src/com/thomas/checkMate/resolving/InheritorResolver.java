package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InheritorResolver {
    public static List<PsiClass> resolveInheritors(PsiExpression expression) {
        List<PsiClass> inheritors = new ArrayList<>();
        PsiVariable variable = VariableResolver.resolveVariable(expression);
        if (variable != null) {
            List<PsiExpression> assignmentExpressions = new ArrayList<>();
            PsiExpression initializer = variable.getInitializer();
            if (initializer != null) {
                assignmentExpressions.add(initializer);
            }
            assignmentExpressions.addAll(findAssignments(variable));
            assignmentExpressions.forEach(ae -> {
                List<PsiClass> subInheritors = resolveInheritors(ae);
                if (subInheritors.size() > 0) {
                    inheritors.addAll(subInheritors);
                } else {
                    PsiType type = ae.getType();
                    if (type != null) {
                        PsiClass psiClass = JavaPsiFacade.getInstance(ae.getProject()).findClass(type.getCanonicalText(), GlobalSearchScope.allScope(ae.getProject()));
                        if (psiClass != null) {
                            inheritors.add(psiClass);
                        }
                    }
                }
            });
        }
        return inheritors;
    }


    private static List<PsiExpression> findAssignments(PsiVariable variable) {
        SearchScope useScope = variable.getUseScope();
        List<PsiExpression> filteredAssignments = new ArrayList<>();
        if (useScope instanceof LocalSearchScope) {
            PsiElement[] scope = ((LocalSearchScope) useScope).getScope();
            for (PsiElement element : scope) {
                filteredAssignments = new ArrayList<>();
                Collection<PsiAssignmentExpression> assignments = PsiTreeUtil.findChildrenOfType(element, PsiAssignmentExpression.class);
                for (PsiAssignmentExpression expression : assignments) {
                    PsiExpression lExpression = expression.getLExpression();
                    PsiVariable assignmentVariable = VariableResolver.resolveVariable(lExpression);
                    if (assignmentVariable.equals(variable)) {
                        filteredAssignments.add(expression.getRExpression());
                    }
                }
            }
        }
        return filteredAssignments;
    }
}
