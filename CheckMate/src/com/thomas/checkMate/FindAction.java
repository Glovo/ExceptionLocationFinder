package com.thomas.checkMate;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.ComputableExceptionFinder;
import com.thomas.checkMate.discovery.factories.DiscovererFactory;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import com.thomas.checkMate.presentation.dialog.GenerateDialog;
import com.thomas.checkMate.writing.TryCatchStatementWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class FindAction extends AnAction {
    private CheckMateSettings checkMateSettings;

    public FindAction() {
        checkMateSettings = CheckMateSettings.getInstance();
    }

    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getProject();
        if (psiFile == null || editor == null) {
            return;
        }

        //Extract all selected method call expressions
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        PsiStatementExtractor statementExtractor = new PsiStatementExtractor(psiFile, currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
        PsiMethodCallExpressionExtractor methodCallExpressionExtractor = new PsiMethodCallExpressionExtractor(statementExtractor);
        Set<PsiCallExpression> psiMethodCalls = methodCallExpressionExtractor.extract();
        if (psiMethodCalls.size() < 1) {
            showInformationHint(editor, "No expressions found in current selection");
            return;
        }

        //Get selected discoverers
        List<ExceptionIndicatorDiscoverer> discovererList = DiscovererFactory.createSelectedDiscovers(project, checkMateSettings.getIncludeJavaDocs());
        //Find all uncaught unchecked exceptions in extracted method call expressions with the selected discoverers
        ComputableExceptionFinder exceptionFinder = new ComputableExceptionFinder(psiMethodCalls, discovererList, checkMateSettings.getIncludeJavaSrc());
        Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = ProgressManager.getInstance()
                .runProcessWithProgressSynchronously(exceptionFinder, "Searching For Unchecked Exceptions", true, project);
        if (discoveredExceptions.keySet().size() < 1) {
            showInformationHint(editor, "No uncaught unchecked exceptions found in the selected statements");
            return;
        }

        //Generate dialog with all discovered uncaught unchecked exceptions
        GenerateDialog generateDialog = new GenerateDialog(discoveredExceptions, psiFile);
        generateDialog.show();
        if (generateDialog.isOK()) {
            //If ok is pressed, start writing the try catch statement for the selected exceptions
            generateTryCatch(statementExtractor, generateDialog, project, editor);
        } else {
            //The action was cancelled
            psiFile.navigate(true);
        }
    }

    private void generateTryCatch(PsiStatementExtractor statementExtractor, GenerateDialog generateDialog, Project project, Editor editor) {
        //Extract the statements that need to be wrapped by the try catch statement
        List<PsiStatement> statements = statementExtractor.extract();
        //Activate the file that needs to be edited
        NavigationUtil.activateFileWithPsiElement(statements.get(0));
        //Get the selected exceptions
        List<PsiType> selectedExceptionTypes = generateDialog.getSelectedExceptionTypes();
        if (selectedExceptionTypes.size() < 1) {
            showInformationHint(editor, "No exceptions selected");
            return;
        }
        //Write a try catch statement for the selected exceptions around the extracted statements
        new TryCatchStatementWriter(project, statements, selectedExceptionTypes).write();
    }

    private void showInformationHint(Editor editor, String hint) {
        HintManager.getInstance().showInformationHint(editor, hint);
    }
}
