package com.thomas.checkMate.configuration.util;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

import com.glovoapp.plugins.infrastructure.configuration.ExceptionLocationFinderSettings;
import com.glovoapp.plugins.infrastructure.configuration.Settings;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

public class FirstRunUtil {

    public static void promptSettings(final ExceptionLocationFinderSettings locationFinderSettings,
                                      final Project project) {
        final Settings settings = locationFinderSettings.getSettings();
        if (settings.isFirstRun()) {
            locationFinderSettings.set(settings.withFirstRun(false));
            int confirmed = showConfirmDialog(null,
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
