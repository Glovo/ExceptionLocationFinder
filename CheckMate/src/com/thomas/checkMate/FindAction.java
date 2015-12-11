package com.thomas.checkMate;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.thomas.checkMate.discovery.ExceptionFinder;
import com.thomas.checkMate.discovery.doc_throw_tag.DocTagDiscoverer;
import com.thomas.checkMate.discovery.doc_throw_tag.DocTagTypeResolver;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementTypeResolver;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import com.thomas.checkMate.presentation.dialog.GenerateDialog;
import com.thomas.checkMate.writing.TryCatchStatementWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FindAction extends AnAction {
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
        Set<PsiMethodCallExpression> psiMethodCalls = methodCallExpressionExtractor.extract();
        if (psiMethodCalls.size() < 1) {
            showInformationHint(editor, "No expressions found in current selection");
            return;
        }

        //Get all discoverers
        //TODO: Select discoverers with settings
        List<ExceptionIndicatorDiscoverer> discovererList = new ArrayList<>();
        discovererList.add(getThrowStatementDiscoverer());
        discovererList.add(getDocTagDiscoverer(project));

        //Find all uncaught unchecked exceptions in extracted method call expressions with the discoverers
        Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = ExceptionFinder.find(psiMethodCalls, discovererList);
        if (discoveredExceptions.keySet().size() < 1) {
            showInformationHint(editor, "No uncaught unchecked exceptions found in the selected statements");
            return;
        }

        //Generate dialog with all discovered uncaught unchecked exceptions
        GenerateDialog generateDialog = new GenerateDialog(discoveredExceptions, project);
        generateDialog.show();
        if (generateDialog.isOK()) {

            //If ok is pressed, start writing the try catch statement for the selected exceptions
            generateTryCatch(statementExtractor, generateDialog, project, editor);
        }
    }

    private ThrowStatementDiscoverer getThrowStatementDiscoverer() {
        ThrowStatementTypeResolver throwStatementTypeResolver = new ThrowStatementTypeResolver();
        return new ThrowStatementDiscoverer(throwStatementTypeResolver);
    }

    private DocTagDiscoverer getDocTagDiscoverer(Project project) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        DocTagTypeResolver typeResolver = new DocTagTypeResolver(elementFactory);
        return new DocTagDiscoverer(typeResolver);
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
