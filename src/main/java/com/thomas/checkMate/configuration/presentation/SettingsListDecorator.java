package com.thomas.checkMate.configuration.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.ToolbarDecorator;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class SettingsListDecorator {
    private static final String REGEX = "Regex: ";

    LabeledComponent decorate(JList<String> listToDecorate,
                              DefaultListModel<String> settingsListModel, String label,
                              String detail, String inputTitle) {
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(listToDecorate);
        toolbarDecorator.setAddAction(anActionButton -> {
            String blackListItem = (String) JOptionPane.showInputDialog(listToDecorate, REGEX, inputTitle,
                    JOptionPane.INFORMATION_MESSAGE, AllIcons.General.Add, null, null);
            try {
                Pattern.compile(blackListItem);
                settingsListModel.addElement(blackListItem);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(listToDecorate, blackListItem + " is not a valid regex", "Invalid Regex", JOptionPane.ERROR_MESSAGE);
            }
        });
        toolbarDecorator.disableUpDownActions();
        toolbarDecorator.setVisibleRowCount(3);
        JPanel decoratedPanel = toolbarDecorator.createPanel();
        decoratedPanel.setBorder(BorderFactory.createTitledBorder(detail));
        return LabeledComponent.create(decoratedPanel, label);
    }
}
