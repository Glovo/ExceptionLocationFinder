package com.thomas.checkMate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiStatement;

import javax.swing.*;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FindAction extends AnAction {
    private JComponent jComponent;
    private List<Statement> selectedStatements = new ArrayList<>();

    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        Set<PsiMethodCallExpression> psiMethodCalls = getPsiMethodCallExpressionFromContext(currentCaret.getSelectionStart(), currentCaret.getSelectionEnd(), psiFile);
        GenerateDialog generateDialog = new GenerateDialog(psiMethodCalls, e.getProject());
        generateDialog.show();
        if (generateDialog.isOK()) {
//            TryCatchStatementWriter tryCatchStatementWriter = new TryCatchStatementWriter(editor.getDocument(), e.getProject(), )
        }
    }

    private Set<PsiMethodCallExpression> getPsiMethodCallExpressionFromContext(int startOffset, int endOffset, PsiFile psiFile) {
        Set<PsiMethodCallExpression> selectedStatements = new PsiMethodCallExpressionExtractor(psiFile, startOffset, endOffset).extract();
        selectedStatements.stream().forEach(s -> System.out.println(s.getText()));
        return selectedStatements;
    }

    private Set<PsiStatement> getPsiStatementsFromContext(int startOffset, int endOffset, PsiFile psiFile) {
        return null;
    }
}
