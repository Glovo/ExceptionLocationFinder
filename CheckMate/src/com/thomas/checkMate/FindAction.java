package com.thomas.checkMate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiStatement;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import com.thomas.checkMate.writing.TryCatchStatementWriter;

import java.util.List;
import java.util.Set;


public class FindAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getProject();
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        PsiStatementExtractor statementExtractor = new PsiStatementExtractor(psiFile, currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
        PsiMethodCallExpressionExtractor methodCallExpressionExtractor = new PsiMethodCallExpressionExtractor(statementExtractor);
        Set<PsiMethodCallExpression> psiMethodCalls = methodCallExpressionExtractor.extract();
        GenerateDialog generateDialog = new GenerateDialog(psiMethodCalls, project);
        generateDialog.show();
        List<PsiStatement> statements = statementExtractor.extract();
        if (generateDialog.isOK()) {
            TryCatchStatementWriter tryCatchStatementWriter = new TryCatchStatementWriter(project, statements, generateDialog.getSelectedExceptionTypes());
            tryCatchStatementWriter.write();
        }
    }
    //TODO: check if compare works
}
