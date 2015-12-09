package com.thomas.checkMate.writing;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class TryCatchStatementWriter {
    private Document document;
    private PsiMethod psiMethod;
    private Project project;
    private WriteTryCatchRunnable writeRunnable;
    private PsiStatement firstStatement;
    private PsiStatement lastStatement;

    public TryCatchStatementWriter(Document document, Project project, List<PsiStatement> psiStatements, List<PsiType> exceptions, PsiMethod psiMethod) {
        this.document = document;
        this.project = project;
        this.writeRunnable = new WriteTryCatchRunnable(psiStatements, exceptions);
        this.psiMethod = psiMethod;
    }

    public void write() {
        WriteCommandAction.runWriteCommandAction(project, writeRunnable);
    }

    public class WriteTryCatchRunnable implements Runnable {
        private List<PsiStatement> statementsToSurround;
        private List<PsiType> exceptionTypes;
        private int startOffset;
        private int endOffset;

        public WriteTryCatchRunnable(List<PsiStatement> psiStatements, List<PsiType> exceptionTypes) {
            this.statementsToSurround = psiStatements;
            this.exceptionTypes = exceptionTypes;
            determineOffsets(psiStatements);
        }

        @Override
        public void run() {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            List<PsiCatchSection> psiCatchSections = generateCatchSections(elementFactory);
            PsiTryStatement surroundingTryStatement = PsiTreeUtil.getParentOfType(firstStatement, PsiTryStatement.class);
            if (surroundingTryStatement != null) {
                psiCatchSections.forEach(surroundingTryStatement::add);
                return;
            }
            PsiTryStatement tryStatement = generateTryStatement(elementFactory);
            psiCatchSections.forEach(tryStatement::add);
            PsiElement commonContext = firstStatement.getContext();
            if (commonContext != null) {
                commonContext.addBefore(tryStatement, firstStatement);
                statementsToSurround.stream().forEach(PsiStatement::delete);
            }
        }

        private PsiTryStatement generateTryStatement(PsiElementFactory elementFactory) {
            StringBuilder sb = new StringBuilder("try{");
            for (PsiStatement psiStatement : statementsToSurround) {
                sb.append(psiStatement.getText());
            }
            sb.append("}");
            return (PsiTryStatement) elementFactory.createStatementFromText(sb.toString(), null);
        }

        private List<PsiCatchSection> generateCatchSections(PsiElementFactory elementFactory) {
            List<PsiCatchSection> psiCatchSections = new ArrayList<>();
            for (int i = 0; i < exceptionTypes.size(); i++) {
                PsiCatchSection catchSection = elementFactory.createCatchSection(exceptionTypes.get(i), "e" + i, null);
                psiCatchSections.add(catchSection);
            }
            return psiCatchSections;
        }

        private void determineOffsets(List<PsiStatement> psiStatements) {
            startOffset = Integer.MAX_VALUE;
            endOffset = 0;
            for (PsiStatement psiStatement : psiStatements) {
                int tempStart;
                int tempEnd;
                TextRange textRange = psiStatement.getTextRange();
                tempStart = textRange.getStartOffset();
                tempEnd = textRange.getEndOffset();
                if (tempStart < startOffset) {
                    startOffset = tempStart;
                    firstStatement = psiStatement;
                }
                if (tempEnd > endOffset) {
                    endOffset = tempEnd;
                    lastStatement = psiStatement;
                }
            }
            startOffset--;
        }
    }
}
