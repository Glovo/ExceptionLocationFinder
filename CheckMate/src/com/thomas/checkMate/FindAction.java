package com.thomas.checkMate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;

import javax.swing.*;


public class FindAction extends AnAction {
    private JComponent jComponent;

    public void actionPerformed(AnActionEvent e) {
//        GeneratePopup generatedPopup = new GeneratePopup(getPsiMethodFromContext(e));
//        popup = generatedPopup.getPopup;
//        popup.showInFocusCenter();
        GenerateDialog generateDialog = new GenerateDialog(getPsiMethodFromContext(e));
        generateDialog.show();
    }


    private PsiMethod getPsiMethodFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(elementAt, PsiMethod.class);
    }
}
