package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;
import com.thomas.checkMate.resolving.OverridingMethodEstimator;
import com.thomas.checkMate.resolving.OverridingMethodResolver;
import com.thomas.checkMate.configuration.util.BlackListUtil;
import com.thomas.checkMate.utilities.DiscoveryMapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionDiscoveringVisitor extends JavaRecursiveElementVisitor {
    private final CheckMateSettings checkMateSettings;
    private Map<PsiType, Set<Discovery>> discoveredExceptions = new HashMap<>();
    //    private Map<PsiClass, Collection<PsiClass>> inheritorCache = new HashMap<>();
    private MethodTracker methodTracker = new MethodTracker();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discovererList;

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
            if (BlackListUtil.isAllowed(psiMethod, checkMateSettings.getClassBlackList())) {
                if (BlackListUtil.isAllowed(psiMethod, checkMateSettings.getOverrideBlackList())) {
                    if (checkMateSettings.getEstimateInheritors()) {
                        if (callExpression instanceof PsiMethodCallExpression) {
                            List<PsiMethod> overrides = OverridingMethodEstimator.estimate((PsiMethodCallExpression) callExpression, psiMethod);
                            overrides.forEach(m -> visitMethod(m, null));
                        }
                    } else {
                        visitMethod(psiMethod, OverridingMethodResolver.resolve(psiMethod));
                    }
                } else {
                    visitMethod(psiMethod, null);
                }
            }
        }
        super.visitCallExpression(callExpression);
    }

    public void visitMethod(PsiMethod method, List<PsiMethod> overridingMethods) {
        if (method == null) {
            return;
        }
        if (methodTracker.alreadyOpened(method)) {
            return;
        }
        if (methodTracker.alreadyVisited(method)) {
            addDiscoveries(methodTracker.getResult(method));
            return;
        }
        if (BlackListUtil.isAllowed(method, checkMateSettings.getClassBlackList())) {
            methodTracker.openMethod(method);
            PsiMethod mirror = getMirror(method);
            if (mirror != null) {
                visitMethod(mirror);
            } else {
                super.visitMethod(method);
                if (overridingMethods != null) {
                    overridingMethods.forEach(om -> visitMethod(om, null));
                }
            }
            methodTracker.closeMethod(method);
        }
    }

//    public void visitInheritors(PsiMethod method) {
//        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
//        if (psiClass != null) {
//            Collection<PsiClass> inheritors = inheritorCache.get(psiClass);
//            if (inheritors == null) {
//                inheritors = ClassInheritorsSearch.search(psiClass).findAll();
//                inheritorCache.put(psiClass, inheritors);
//            }
//            inheritors.forEach(i -> {
//                PsiMethod[] overridingMethods = i.findMethodsBySignature(method, false);
//                for (PsiMethod psiMethod : overridingMethods) {
//                    this.visitMethod(psiMethod);
//                }
//            });
//        }
//    }

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
