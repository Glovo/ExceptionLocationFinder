package com.thomas.checkMate.presentation.dialog;

import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiThrowStatement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ThrowsStatementPopupStep extends BaseListPopupStep<PsiThrowStatement> {

    private PopupStepListener<PsiThrowStatement> listener;

    public ThrowsStatementPopupStep(String title, List<? extends PsiThrowStatement> values, PopupStepListener<PsiThrowStatement> popupStepListener) {
        super(title, values);
        this.listener = popupStepListener;
    }

    @Override
    public PopupStep onChosen(PsiThrowStatement selectedValue, boolean finalChoice) {
        if (finalChoice) {
            listener.onFinalChoice(selectedValue);
        }
        return super.onChosen(selectedValue, finalChoice);
    }

    @NotNull
    @Override
    public String getTextFor(PsiThrowStatement value) {
        return value.getText();
    }
}
