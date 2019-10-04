package com.thomas.checkMate.discovery;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComputableExceptionFinder implements ThrowableComputable<Map<PsiType, Set<Discovery>>, RuntimeException> {
    private Set<PsiCallExpression> expressions;
    private List<ExceptionIndicatorDiscoverer> discovererList;

    public ComputableExceptionFinder(Set<PsiCallExpression> expressions,
                                     List<ExceptionIndicatorDiscoverer> discovererList) {
        this.expressions = expressions;
        this.discovererList = discovererList;
    }

    @Override
    public Map<PsiType, Set<Discovery>> compute() {
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        indicator.setIndeterminate(true);
        return ApplicationManager.getApplication().runReadAction((Computable<Map<PsiType, Set<Discovery>>>) () -> ExceptionFinder.find(expressions, discovererList));
    }
}
