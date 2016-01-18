package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;
import com.thomas.checkMate.utilities.DiscoveryMapUtil;
import com.thomas.checkMate.utilities.WhiteListUtil;

import java.util.*;

public class ExceptionDiscoveringVisitor extends JavaRecursiveElementVisitor {
    private Map<PsiType, Set<Discovery>> discoveredExceptions = new HashMap<>();
    private Map<PsiClass, Collection<PsiClass>> inheritorCache = new HashMap<>();
    private MethodTracker methodTracker = new MethodTracker();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discovererList;
    private final CheckMateSettings checkMateSettings;

    public ExceptionDiscoveringVisitor(PsiElement elementToVisit, List<ExceptionIndicatorDiscoverer> discovererList) {
        this.checkMateSettings = CheckMateSettings.getInstance();
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        this.discovererList = discovererList;
        UncheckedValidator uncheckedValidator = new UncheckedValidator(elementToVisit.getManager(), elementToVisit.getResolveScope(), checkMateSettings.getIncludeErrors());
        this.discovererList.forEach(d -> {
            d.setUncheckedValidator(uncheckedValidator);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visitElement(PsiElement element) {
        discovererList.forEach(d -> {
            Discovery discovery = d.discover(element);
            if (discovery != null) {
                methodTracker.newDiscovery(discovery);
                addDiscovery(discovery);
            }
        });
        super.visitElement(element);
    }

    private void addDiscovery(Discovery discovery) {
        if (tryStatementTracker.isNotCaughtByEnclosingCatchSections(discovery.getExceptionType())) {
            DiscoveryMapUtil.addDiscovery(discovery, discoveredExceptions);
        }
    }

    private void addDiscoveries(Set<Discovery> discoveries) {
        discoveries.forEach(this::addDiscovery);
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
        if (methodTracker.alreadyOpened(method)) {
            return;
        }
        if (methodTracker.alreadyVisited(method)) {
            addDiscoveries(methodTracker.getResult(method));
            return;
        }
        if (WhiteListUtil.isAllowed(method)) {
            methodTracker.openMethod(method);
            PsiMethod mirror = getMirror(method);
            if (mirror != null) {
                visitMethod(mirror);
            } else {
                super.visitMethod(method);
                if (checkMateSettings.getIncludeInheritors()) {
                    visitInheritors(method);
                }
            }
            methodTracker.closeMethod(method);
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
                    this.visitMethod(psiMethod);
                }
            });
        }
    }

    public PsiMethod getMirror(PsiMethod method) {
        if (method instanceof ClsMethodImpl) {
            PsiMethod sourceMirror = ((ClsMethodImpl) method).getSourceMirrorMethod();
            if (sourceMirror != null) {
                return sourceMirror;
            } else {
                PsiMethod mirror = (PsiMethod) ((ClsMethodImpl) method).getMirror();
                if (mirror != null) {
                    return mirror;
                }
            }
        }
        return null;
    }

    public Map<PsiType, Set<Discovery>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
