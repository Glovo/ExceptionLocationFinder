package com.thomas.checkMate.presentation;

import com.intellij.openapi.ui.LabeledComponent;

import javax.swing.*;

public interface ListDecorator<T> {
    LabeledComponent decorate(JList<T> listToDecorate, String label);
}
