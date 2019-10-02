package com.thomas.checkMate.configuration.util;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

import com.glovoapp.plugins.infrastructure.configuration.ExceptionLocationFinderSettings;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import javax.swing.JOptionPane;

public class FirstRunUtil {

    public static void promptSettings(ExceptionLocationFinderSettings settings, Project project) {
        if (settings.isFirstRun()) {
            settings.setState(settings.getState()
                                      .withFirstRun(false));
            int confirmed = JOptionPane.showConfirmDialog(null,
                "You can customize the search process through the settings menu.\n"
                    + "Would you like to review the settings?",
                "ExceptionLocationFinder settings", YES_NO_OPTION, QUESTION_MESSAGE);
            if (confirmed == 0) {
                ShowSettingsUtil.getInstance()
                                .showSettingsDialog(project, ExceptionLocationFinderSettings.NAME);
            }
        }
    }
}
