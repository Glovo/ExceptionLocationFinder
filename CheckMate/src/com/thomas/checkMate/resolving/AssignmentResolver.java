package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AssignmentResolver {
    public static List<PsiExpression> resolve(PsiVariable variable) {
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
