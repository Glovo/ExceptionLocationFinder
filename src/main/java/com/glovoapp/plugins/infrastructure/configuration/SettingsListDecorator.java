package com.glovoapp.plugins.infrastructure.configuration;

import static com.intellij.icons.AllIcons.General.Add;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.ToolbarDecorator;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

final class SettingsListDecorator {

    private static final String REGEX = "Regex: ";

    LabeledComponent decorate(final JList<String> listToDecorate,
                              final DefaultListModel<String> settingsListModel,
                              final String label,
                              final String detail,
                              final String inputTitle) {
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(listToDecorate);
        toolbarDecorator.setAddAction(anActionButton -> {
            final String listItem = (String) showInputDialog(
                listToDecorate,
                REGEX,
                inputTitle,
                INFORMATION_MESSAGE,
                Add,
                null,
                null
            );
            try {
                Pattern.compile(listItem);
            } catch (Exception exception) {
                showMessageDialog(
                    listToDecorate,
                    '\'' + listItem + "' is not a valid regex",
                    "Invalid Regex",
                    ERROR_MESSAGE
                );
                return;
            }
            settingsListModel.addElement(listItem);
        });
        toolbarDecorator.disableUpDownActions();
        toolbarDecorator.setVisibleRowCount(5);
        JPanel decoratedPanel = toolbarDecorator.createPanel();
        decoratedPanel.setBorder(createTitledBorder(detail));
        return LabeledComponent.create(decoratedPanel, label);
    }
}
