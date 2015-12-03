package com.thomas.checkMate;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

public class GenerateDialog extends DialogWrapper {
    private JComponent mainComponent;

    public GenerateDialog(PsiMethod psiMethod) {
        super(psiMethod.getProject());
        setTitle("Uncaught Unchecked Exceptions");
        ThrowStatementVisitor throwStatementVisitor = new ThrowStatementVisitor(psiMethod);
        Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions = throwStatementVisitor.getDiscoveredExceptions();
        mainComponent = new ExceptionForm(discoveredExceptions).getMailPanel();
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainComponent;
    }
}
