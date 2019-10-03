package com.thomas.checkMate.configuration.util;

import static com.glovoapp.plugins.infrastructure.configuration.Settings.currentSettings;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

import com.glovoapp.plugins.infrastructure.configuration.Settings;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

public class FirstRunUtil {

    public static void promptSettings(final Project project) {
        final Settings settings = currentSettings();
        if (settings.isFirstRun()) {
            Settings.set(settings.withFirstRun(false));
            int confirmed = showConfirmDialog(null,
                "You can customize the search process through the settings menu.\n"
                    + "Would you like to review the settings?",
                "ExceptionLocationFinder settings", YES_NO_OPTION, QUESTION_MESSAGE);
            if (confirmed == 0) {
                ShowSettingsUtil.getInstance()
                                .showSettingsDialog(project, Settings.NAME);
            }
        }
    }
}
