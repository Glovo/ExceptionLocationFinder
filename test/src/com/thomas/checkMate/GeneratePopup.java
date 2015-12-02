package com.thomas.checkMate;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiThrowStatement;

import java.util.List;

public class GeneratePopup {
//    private final JBPopup popup;

    public GeneratePopup(PsiMethod psiMethod) {
        ThrowStatementVisitor throwStatementVisitor = new ThrowStatementVisitor(psiMethod);
        throwStatementVisitor.getThrowStatements();
//        BaseListPopupStep step = new ThrowsStatementPopupStep("Throws statements", psiThrowStatements, new NavigatePopupStepListener());
//        popup = JBPopupFactory.getInstance().createListPopup(step);
    }

//    public JBPopup getPopup() {
//        return popup;
//    }
}
