package com.thomas.checkMate.writing;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.utilities.CatchSectionUtil;

import java.util.Arrays;
import java.util.List;

public class TryCatchStatementWriter {
    private Project project;
    private WriteTryCatchRunnable writeRunnable;

    public TryCatchStatementWriter(Project project, List<PsiStatement> psiStatements, List<PsiType> exceptions) {
        this.project = project;
        this.writeRunnable = new WriteTryCatchRunnable(psiStatements, exceptions, JavaPsiFacade.getElementFactory(project));
    }

    public void write() {
        WriteCommandAction.runWriteCommandAction(project, writeRunnable);
    }

    public class WriteTryCatchRunnable implements Runnable {
        private List<PsiStatement> statementsToSurround;
        private List<PsiType> exceptionTypes;
        private PsiStatement firstStatement;
        private TryStatementWriter tryStatementWriter;
        private CatchSectionWriter catchSectionWriter;

        public WriteTryCatchRunnable(List<PsiStatement> psiStatements, List<PsiType> exceptionTypes, PsiElementFactory elementFactory) {
            this.statementsToSurround = psiStatements;
            this.exceptionTypes = exceptionTypes;
            this.firstStatement = psiStatements.get(0);
            this.tryStatementWriter = new TryStatementWriter(elementFactory);
            this.catchSectionWriter = new CatchSectionWriter(elementFactory);
        }

        @Override
        public void run() {
            List<PsiCatchSection> psiCatchSections = catchSectionWriter.write(exceptionTypes);
            PsiTryStatement surroundingTryStatement;
            if (firstStatement instanceof PsiTryStatement) {
                surroundingTryStatement = (PsiTryStatement) firstStatement;
            } else {
                surroundingTryStatement = PsiTreeUtil.getParentOfType(firstStatement, PsiTryStatement.class);
            }
            if (surroundingTryStatement != null) {
                PsiCatchSection[] existingCatchSections = surroundingTryStatement.getCatchSections();
                psiCatchSections.addAll(catchSectionWriter.write(CatchSectionUtil.getExceptionTypes(existingCatchSections)));
                Arrays.asList(existingCatchSections).forEach(PsiCatchSection::delete);
                addCatchSectionByTypeHierarchy(psiCatchSections, surroundingTryStatement);
            } else {
                PsiTryStatement tryStatement = tryStatementWriter.write(statementsToSurround);
                addCatchSectionByTypeHierarchy(psiCatchSections, tryStatement);
                PsiElement commonContext = firstStatement.getContext();
                if (commonContext != null) {
                    commonContext.addBefore(tryStatement, firstStatement);
                    statementsToSurround.stream().forEach(PsiStatement::delete);
                }
            }
        }

        private void addCatchSectionByTypeHierarchy(List<PsiCatchSection> psiCatchSections, PsiTryStatement psiTryStatement) {
            CatchSectionSorter.sort(psiCatchSections).forEach(psiTryStatement::add);
        }
    }
}
