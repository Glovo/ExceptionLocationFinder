package com.thomas.checkMate.configuration.util;

import com.thomas.checkMate.configuration.CheckMateSettings;

public class VersionCheckUtil {
    public static final String currentVersion = "1.1";

    public static void reset(CheckMateSettings checkMateSettings) {
        if (!currentVersion.equals(checkMateSettings.getVersion())) {
            checkMateSettings.reset();
            checkMateSettings.setVersion(currentVersion);
        }
    }
}
