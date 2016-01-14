package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;
import com.thomas.checkMate.utilities.WhiteListUtil;

import java.util.*;

public class ExceptionDiscoveringVisitor extends JavaRecursiveElementVisitor {
    private Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
    private Map<PsiClass, Collection<PsiClass>> inheritorCache = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discovererList;
    private Set<PsiElement> visitedMethods = new HashSet<>();
    private final CheckMateSettings checkMateSettings;

    public ExceptionDiscoveringVisitor(PsiElement elementToVisit, List<ExceptionIndicatorDiscoverer> discovererList) {
        this.checkMateSettings = CheckMateSettings.getInstance();
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        this.discovererList = discovererList;
        UncheckedValidator uncheckedValidator = new UncheckedValidator(elementToVisit.getManager(), elementToVisit.getResolveScope(), checkMateSettings.getIncludeErrors());
        this.discovererList.forEach(d -> {
            d.setTryStatementTracker(tryStatementTracker);
            d.setUncheckedValidator(uncheckedValidator);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visitElement(PsiElement element) {
        discovererList.forEach(d -> d.addDiscoveries(element, discoveredExceptions));
        super.visitElement(element);
    }

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        tryStatementTracker.onTryStatementOpened(statement);
        super.visitTryStatement(statement);
        tryStatementTracker.onTryStatementClosed();
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        PsiMethod psiMethod = callExpression.resolveMethod();
        if (psiMethod != null) {
            visitMethod(psiMethod);
        }
        super.visitCallExpression(callExpression);
    }


    public void visitMethod(PsiMethod method) {
        if (WhiteListUtil.isAllowed(method)) {
            visitSource(method);
            if (checkMateSettings.getIncludeInheritors()) {
                visitInheritors(method);
            }
        }
    }

    public void visitInheritors(PsiMethod method) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        if (psiClass != null) {
            Collection<PsiClass> inheritors = inheritorCache.get(psiClass);
            if (inheritors == null) {
                inheritors = ClassInheritorsSearch.search(psiClass).findAll();
                inheritorCache.put(psiClass, inheritors);
            }
            inheritors.forEach(i -> {
                PsiMethod[] overridingMethods = i.findMethodsBySignature(method, false);
                for (PsiMethod psiMethod : overridingMethods) {
                    if (!psiMethod.equals(method)) {
                        this.visitMethod(psiMethod);
                    }
                }
            });
        }
    }


    public void visitSource(PsiMethod method) {
        boolean srcFound = false;
        if (method instanceof ClsMethodImpl) {
            PsiMethod sourceMirror = ((ClsMethodImpl) method).getSourceMirrorMethod();
            if (sourceMirror != null) {
                srcFound = true;
                uniqueVisit(sourceMirror);
            } else {
                PsiMethod mirror = (PsiMethod) ((ClsMethodImpl) method).getMirror();
                if (mirror != null) {
                    srcFound = true;
                    uniqueVisit(mirror);
                }
            }
        }
        if (!srcFound) {
            uniqueVisit(method);
        }
    }

    public void uniqueVisit(PsiMethod method) {
        if (!visitedMethods.contains(method)) {
            visitedMethods.add(method);
            super.visitMethod(method);
        }
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
