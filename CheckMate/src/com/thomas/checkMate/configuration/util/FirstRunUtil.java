package com.thomas.checkMate.configuration.util;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.thomas.checkMate.configuration.CheckMateSettings;

import javax.swing.*;

public class FirstRunUtil {
    public static void promptSettings(CheckMateSettings settings, Project project) {
        if (settings.getFirstRun()) {
            settings.setFirstRun(false);
            int confirmed = JOptionPane.showConfirmDialog(null, "You can customize the search process through the settings menu.\n Do you want to review the settings?", "CheckMate settings", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirmed == 0) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, "CheckMateConfiguration");
            }
        }
    }
}
