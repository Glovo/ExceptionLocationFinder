package com.thomas.checkMate.presentation.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.presentation.exception_form.ExceptionForm;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenerateDialog extends DialogWrapper {
    private ExceptionForm exceptionForm;

    public GenerateDialog(Map<PsiType, Set<Discovery>> discoveredExceptions, PsiFile currentFile) {
        super(currentFile.getProject());
        setTitle("Uncaught Unchecked Exceptions");
        exceptionForm = new ExceptionForm(discoveredExceptions, currentFile);
        init();
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
