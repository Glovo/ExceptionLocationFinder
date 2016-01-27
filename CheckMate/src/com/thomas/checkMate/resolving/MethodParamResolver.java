package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.search.searches.SuperMethodsSearch;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodParamResolver {
    public static List<PsiExpression> resolve(PsiVariable variable) {
        PsiMethod surroundingMethod = PsiTreeUtil.getParentOfType(variable, PsiMethod.class);
        List<PsiExpression> resolvedExpression = new ArrayList<>();
        if (surroundingMethod != null) {
            PsiParameterList parameterList = surroundingMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            for (PsiParameter parameter : parameters) {
                PsiIdentifier nameIdentifier = parameter.getNameIdentifier();
                if (nameIdentifier != null && nameIdentifier.equals(variable.getNameIdentifier())) {
                    resolvedExpression.addAll(processMethod(surroundingMethod, parameterList.getParameterIndex(parameter)));
                }
            }
        }
        return resolvedExpression;
    }


    private static List<PsiExpression> processMethod(PsiMethod surroundingMethod, int argumentIndex) {
        List<PsiExpression> discoveredExpressions = new ArrayList<>();
        PsiType methodType = getMethodType(surroundingMethod);
        expressionsOf(referencesTo(surroundingMethod)).forEach(addArgumentsTo(discoveredExpressions, argumentIndex));
        if (methodType != null) {
            expressionsOf(superReferencesTo(surroundingMethod))
                    .filter(qualifierIsSubOf(methodType))
                    .forEach(addArgumentsTo(discoveredExpressions, argumentIndex));
        }
        return discoveredExpressions;
    }

    private static Stream<PsiReference> referencesTo(PsiMethod method) {
        return MethodReferencesSearch.search(method).findAll().stream();
    }

    private static Stream<PsiReference> superReferencesTo(PsiMethod method) {
        Collection<PsiReference> references = new ArrayList<>();
        Query<MethodSignatureBackedByPsiMethod> superSearch = SuperMethodsSearch.search(method, null, true, true);
        Collection<MethodSignatureBackedByPsiMethod> superMethods = superSearch.findAll();
        superMethods.forEach(sm -> references.addAll(referencesTo(sm.getMethod()).collect(Collectors.toList())));
        return references.stream();
    }


    private static Stream<PsiMethodCallExpression> expressionsOf(Stream<PsiReference> methodReferences) {
        return methodReferences
                .filter(mr -> mr instanceof PsiReferenceExpression)
                .map(pre -> ((PsiReferenceExpression) pre).getParent())
                .filter(p -> p != null && p instanceof PsiMethodCallExpression)
                .map(pme -> (PsiMethodCallExpression) pme);
    }

    private static Consumer<PsiMethodCallExpression> addArgumentsTo(List<PsiExpression> discoveredExpressions, int argumentIndex) {
        return (pme -> {
            PsiExpressionList argumentList = pme.getArgumentList();
            PsiExpression[] expressions = argumentList.getExpressions();
            if (expressions.length > argumentIndex) {
                discoveredExpressions.add(expressions[argumentIndex]);
            }
        });
    }

    private static PsiType getMethodType(PsiMethod psiMethod) {
        PsiClass subClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
        if (subClass != null) {
            return PsiTypesUtil.getClassType(subClass);
        }
        return null;
    }

    private static Predicate<PsiMethodCallExpression> qualifierIsSubOf(PsiType type) {
        return (pme -> {
            List<PsiType> types = TypeEstimator.estimate(pme.getMethodExpression().getQualifierExpression());
            return types.contains(type);
        });
    }
}
