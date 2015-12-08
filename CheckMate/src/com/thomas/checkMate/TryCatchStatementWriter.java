package com.thomas.checkMate;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;

import java.util.List;

public class TryCatchStatementWriter {
    private Document document;
    private Project project;
    private WriteTryCatchRunnable writeRunnable;

    public TryCatchStatementWriter(Document document, Project project, List<PsiStatement> psiStatements, List<PsiType> exceptions) {
        this.document = document;
        this.project = project;
        this.writeRunnable = new WriteTryCatchRunnable(psiStatements, exceptions);
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
            StringBuilder sb = new StringBuilder("try{");
            for (PsiStatement psiStatement : statementsToSurround) {
                sb.append(psiStatement.getText());
            }
            sb.append("} catch(");
            for (int i = 0; i < exceptionTypes.size(); i++) {
                sb.append(exceptionTypes.get(i).getPresentableText());
                if (i < exceptionTypes.size() - 1) {
                    sb.append(" | ");
                }
            }
            sb.append("{//catch logic}");
            document.replaceString(0, 1, sb.toString());
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
                }
                if (tempEnd > endOffset) {
                    endOffset = tempEnd;
                }
            }
        }
    }
}
