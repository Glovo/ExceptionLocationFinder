package com.thomas.checkMate.presentation.exception_form;

import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;

public class DefaultListDecorator<T> implements ListDecorator<T> {
    @Override
    public LabeledComponent decorate(JList<T> listToDecorate, String label) {
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(listToDecorate);
        toolbarDecorator.disableAddAction();
        toolbarDecorator.disableRemoveAction();
        toolbarDecorator.disableUpDownActions();
        JPanel decoratedPanel = toolbarDecorator.createPanel();
        return LabeledComponent.create(decoratedPanel, label);
    }
}
