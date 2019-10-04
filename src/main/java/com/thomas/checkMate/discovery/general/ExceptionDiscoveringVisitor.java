package com.thomas.checkMate.discovery.general;

import static com.glovoapp.plugins.infrastructure.configuration.Settings.currentSettings;
import static com.thomas.checkMate.configuration.util.ListFilterUtil.isAllowedInBlackList;
import static com.thomas.checkMate.configuration.util.ListFilterUtil.isAllowedInWhiteList;

import com.glovoapp.plugins.infrastructure.configuration.Settings;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiTryStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.thomas.checkMate.resolving.OverridingMethodEstimator;
import com.thomas.checkMate.resolving.OverridingMethodResolver;
import com.thomas.checkMate.utilities.DiscoveryMapUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ExceptionDiscoveringVisitor extends JavaRecursiveElementVisitor {

    private Map<PsiType, Set<Discovery>> discoveredExceptions = new HashMap<>();
    private MethodTracker methodTracker = new MethodTracker();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discovererList;

    public ExceptionDiscoveringVisitor(PsiElement elementToVisit,
                                       List<ExceptionIndicatorDiscoverer> discovererList) {
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        this.discovererList = discovererList;
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
        final PsiMethod psiMethod = callExpression.resolveMethod();
        final Settings settings = currentSettings();
        if (psiMethod != null) {
            if (isAllowedInBlackList(psiMethod, settings.getClassBlackList())
                && isAllowedInWhiteList(psiMethod, settings.getClassWhiteList())) {
                if (isAllowedInBlackList(psiMethod, settings.getOverrideBlackList())
                    && isAllowedInWhiteList(psiMethod, settings.getOverrideWhiteList())) {
                    if (settings.estimateInheritors()) {
                        if (callExpression instanceof PsiMethodCallExpression) {
                            List<PsiMethod> overrides = OverridingMethodEstimator.estimate(
                                (PsiMethodCallExpression) callExpression, psiMethod);
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

    private void visitMethod(PsiMethod method, List<PsiMethod> overridingMethods) {
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

        final Settings settings = currentSettings();
        if (isAllowedInBlackList(method, settings.getClassBlackList())
            && isAllowedInWhiteList(method, settings.getClassWhiteList())) {
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

    private PsiMethod getMirror(PsiMethod method) {
        if (method instanceof ClsMethodImpl) {
            return Optional.of(((ClsMethodImpl) method))
                           .map(ClsMethodImpl::getSourceMirrorMethod)
                           .orElseGet(() -> (PsiMethod) ((ClsMethodImpl) method).getMirror());
        }
        return null;
    }

    public Map<PsiType, Set<Discovery>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
