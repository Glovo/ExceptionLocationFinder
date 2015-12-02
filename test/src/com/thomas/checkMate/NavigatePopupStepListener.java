package com.thomas.checkMate;

import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.psi.PsiThrowStatement;

public class NavigatePopupStepListener implements PopupStepListener<PsiThrowStatement> {

    @Override
    public void onFinalChoice(PsiThrowStatement object) {
        NavigationUtil.activateFileWithPsiElement(object);
    }
}
