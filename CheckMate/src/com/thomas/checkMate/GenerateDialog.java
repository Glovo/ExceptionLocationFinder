package com.thomas.checkMate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.DiscoveredThrowStatement;
import com.thomas.checkMate.discovery.ThrowStatementVisitor;
import com.thomas.checkMate.presentation.ExceptionForm;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenerateDialog extends DialogWrapper {
    private ExceptionForm exceptionForm;

    public GenerateDialog(Set<PsiMethodCallExpression> psiMethodCallExpressions, Project project) {
        super(project);
        setTitle("Uncaught Unchecked Exceptions");
        Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions = new HashMap<>();
        ThrowStatementVisitor throwStatementVisitor;
        for (PsiMethodCallExpression expression : psiMethodCallExpressions) {
            throwStatementVisitor = new ThrowStatementVisitor(expression);
            mergeResult(discoveredExceptions, throwStatementVisitor.getDiscoveredExceptions());
        }
        exceptionForm = new ExceptionForm(discoveredExceptions);
        init();
    }

    private void mergeResult(Map<PsiType, Set<DiscoveredThrowStatement>> firstMap, Map<PsiType, Set<DiscoveredThrowStatement>> secondMap) {
        for (Map.Entry<PsiType, Set<DiscoveredThrowStatement>> entry : secondMap.entrySet()) {
            PsiType type = entry.getKey();
            if (firstMap.containsKey(type)) {
                firstMap.get(type).addAll(entry.getValue());
            } else {
                firstMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return exceptionForm.getSplitter();
    }

    public List<PsiType> getSelectedExceptionTypes() {
        return exceptionForm.getSelectedExceptionTypes();
    }
}
