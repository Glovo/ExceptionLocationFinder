package com.thomas.checkMate;

import com.intellij.codeInsight.hint.HintManager;
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
import com.thomas.checkMate.discovery.ComputableExceptionFinder;
import com.thomas.checkMate.discovery.factories.DiscovererFactory;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.MultipleMethodException;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import com.thomas.checkMate.presentation.dialog.GenerateDialog;
import com.thomas.checkMate.writing.TryCatchStatementWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class FindAction extends AnAction {
    private final ProgressManager progressManager;
    private final HintManager hintManager;

    public FindAction() {
        progressManager = ProgressManager.getInstance();
        hintManager = HintManager.getInstance();
    }

    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getProject();
        e.getPlace();
        if (psiFile == null || editor == null) {
            return;
        }

        //Extract all selected method call expressions
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        PsiStatementExtractor statementExtractor = new PsiStatementExtractor(psiFile, currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
        PsiMethodCallExpressionExtractor methodCallExpressionExtractor = new PsiMethodCallExpressionExtractor(statementExtractor);
        Set<PsiCallExpression> psiMethodCalls;
        try {
            psiMethodCalls = methodCallExpressionExtractor.extract();
        } catch (MultipleMethodException mme) {
            showInformationHint(editor, "Please keep your selection within one method");
            return;
        }
        if (psiMethodCalls.size() < 1) {
            showInformationHint(editor, "No expressions found in current selection");
            return;
        }

        //Get selected discoverers
        List<ExceptionIndicatorDiscoverer> discovererList = DiscovererFactory.createSelectedDiscovers(project);
        //Find all uncaught unchecked exceptions in extracted method call expressions with the selected discoverers
        ComputableExceptionFinder exceptionFinder = new ComputableExceptionFinder(psiMethodCalls, discovererList);
        Map<PsiType, Set<Discovery>> discoveredExceptions;
        try {
            discoveredExceptions = progressManager
                    .runProcessWithProgressSynchronously(exceptionFinder, "Searching For Unchecked Exceptions", true, project);
        } catch (StackOverflowError | OutOfMemoryError er) {
            showInformationHint(editor, "Too many statements too process, consider disabling the overriding method search option " +
                    "and/or removing some java sources from the whitelist");
            return;
        }
        if (discoveredExceptions != null && discoveredExceptions.keySet().size() < 1) {
            showInformationHint(editor, "No uncaught unchecked exceptions found in the selected statements");
            return;
        }

        //Generate dialog with all discovered uncaught unchecked exceptions
        GenerateDialog generateDialog = new GenerateDialog(discoveredExceptions, psiFile);
        generateDialog.show();
        if (generateDialog.isOK()) {
            //If ok is pressed, start writing the try catch statement for the selected exceptions
            generateTryCatch(statementExtractor, generateDialog, project, editor);
            currentCaret.removeSelection();
        }
        psiFile.navigate(true);
    }

    private void generateTryCatch(PsiStatementExtractor statementExtractor, GenerateDialog generateDialog, Project project, Editor editor) {
        //Extract the statements that need to be wrapped by the try catch statement
        List<PsiStatement> statements = statementExtractor.extract();
        //Activate the file that needs to be edited
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
        hintManager.showInformationHint(editor, hint);
    }
}
