package com.thomas.checkMate.presentation.exception_form;

import com.intellij.openapi.ui.LabeledComponent;

import javax.swing.*;

public interface ListDecorator<T> {
    LabeledComponent decorate(JList<T> listToDecorate, String label);
}
