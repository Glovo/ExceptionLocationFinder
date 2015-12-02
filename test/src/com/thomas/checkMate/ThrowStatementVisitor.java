package com.thomas.checkMate;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;

import java.util.*;

public class ThrowStatementVisitor extends PsiRecursiveElementVisitor {
    private Map<PsiThrowStatement, Set<PsiMethod>> throwStatements = new HashMap<>();
    private PsiType javaLangError;
    private PsiType javaLangRuntimeException;
    private Stack<PsiElement> tryBlocks = new Stack<>();

    public ThrowStatementVisitor(PsiMethod methodToVisit) {
        this.javaLangError = PsiType.getJavaLangError(methodToVisit.getManager(), methodToVisit.getResolveScope());
        this.javaLangRuntimeException = PsiType.getJavaLangRuntimeException(methodToVisit.getManager(), methodToVisit.getResolveScope());
        methodToVisit.accept(this);
    }

    @Override
    public void visitElement(PsiElement element) {
        if (element instanceof PsiTryStatement) {
            tryBlocks.push(element);
            System.out.println("Push: " + tryBlocks);
            PsiTryStatement psiTryStatement = (PsiTryStatement) element;
            super.visitElement(element);
            tryBlocks.pop();
            System.out.println("Pop: " + tryBlocks);
            return;
        }
        if (element instanceof PsiThrowStatement) {
            PsiThrowStatement psiThrowStatement = (PsiThrowStatement) element;
            if (isUncheckedOrError(psiThrowStatement)) {
                System.out.println("Hit: " + psiThrowStatement);
                this.addToMap(psiThrowStatement);
            }
        }
        if (element instanceof PsiCallExpression) {
            PsiCallExpression psiCallExpression = (PsiCallExpression) element;
            System.out.println(psiCallExpression);
            this.visitElement(psiCallExpression.resolveMethod());
        }
        super.visitElement(element);
    }

    private void addToMap(PsiThrowStatement psiThrowStatement) {
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiThrowStatement, PsiMethod.class);
        if (throwStatements.containsKey(psiThrowStatement)) {
            throwStatements.get(psiThrowStatement).add(psiMethod);
        } else {
            Set<PsiMethod> psiMethods = new HashSet<>();
            psiMethods.add(psiMethod);
            throwStatements.put(psiThrowStatement, psiMethods);
        }
    }

    private boolean isUncheckedOrError(PsiThrowStatement psiThrowStatement) {
        PsiExpression exception = psiThrowStatement.getException();
        if (exception != null) {
            PsiType psiType = exception.getType();
            if (psiType != null) {
                if (psiType.equals(javaLangRuntimeException) || psiType.equals(javaLangError)) {
                    return true;
                }
                List<PsiType> parentTypes = Arrays.asList(psiType.getSuperTypes());
                if (parentTypes.contains(javaLangRuntimeException) || parentTypes.contains(javaLangError)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleCatchSection(PsiCatchSection psiCatchSection) {
        List<PsiType> psiTypes;
        PsiType type = psiCatchSection.getCatchType();
        if (type instanceof PsiDisjunctionType) {
            psiTypes = ((PsiDisjunctionType) type).getDisjunctions();
        } else {
            psiTypes = new ArrayList<>();
            psiTypes.add(type);
        }
        for (PsiType psiType : psiTypes) {
            System.out.println("Catch: " + psiType);
        }
    }

    public Map<PsiThrowStatement, Set<PsiMethod>> getThrowStatements() {
        for (Map.Entry<PsiThrowStatement, Set<PsiMethod>> entry : throwStatements.entrySet()) {
            System.out.println(entry.getKey().getText());
            for (PsiMethod psiMethod : entry.getValue()) {
                System.out.println("\t" + psiMethod);
            }
        }
        return throwStatements;
    }
}
