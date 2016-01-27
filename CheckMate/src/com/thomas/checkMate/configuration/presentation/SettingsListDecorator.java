package com.thomas.checkMate.configuration.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.ToolbarDecorator;

import javax.swing.*;

public class SettingsListDecorator {

    public LabeledComponent decorate(JList<String> listToDecorate, DefaultListModel<String> settingsListModel, String label, String inputTitle, String inputDetail) {
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(listToDecorate);
        toolbarDecorator.setAddAction(anActionButton -> {
            String whitelistItem = (String) JOptionPane.showInputDialog(listToDecorate, inputDetail, inputTitle,
                    JOptionPane.INFORMATION_MESSAGE, AllIcons.General.Add, null, null);
            settingsListModel.addElement(whitelistItem);
        });
        toolbarDecorator.disableUpDownActions();
        toolbarDecorator.setVisibleRowCount(3);
        JPanel decoratedPanel = toolbarDecorator.createPanel();
        return LabeledComponent.create(decoratedPanel, label);
    }
}
