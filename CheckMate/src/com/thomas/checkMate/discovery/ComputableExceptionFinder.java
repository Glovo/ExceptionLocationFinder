package com.thomas.checkMate.discovery;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComputableExceptionFinder implements ThrowableComputable<Map<PsiType, Set<DiscoveredExceptionIndicator>>, RuntimeException> {
    private Set<PsiCallExpression> expressions;
    private List<ExceptionIndicatorDiscoverer> discovererList;
    private boolean includeJavaSrc;
    private boolean includeErrors;

    public ComputableExceptionFinder(Set<PsiCallExpression> expressions, List<ExceptionIndicatorDiscoverer> discovererList, boolean includeJavaSrc, boolean includeErrors) {
        this.expressions = expressions;
        this.discovererList = discovererList;
        this.includeJavaSrc = includeJavaSrc;
        this.includeErrors = includeErrors;
    }

    @Override
    public Map<PsiType, Set<DiscoveredExceptionIndicator>> compute() {
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        indicator.setIndeterminate(true);
        return ApplicationManager.getApplication().runReadAction((Computable<Map<PsiType, Set<DiscoveredExceptionIndicator>>>) () -> ExceptionFinder.find(expressions, discovererList, includeJavaSrc, includeErrors));
    }
}
