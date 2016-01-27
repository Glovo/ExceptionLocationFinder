package com.thomas.checkMate.settings;

import com.thomas.checkMate.CheckMateTest;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class SettingsTest extends CheckMateTest {
    private static final String BLACK_IMPL = "blacklist/BlackImpl.java";
    private static final String[] OVERRIDE_FILES = new String[]{BLACK_IMPL, "blacklist/BlackSuper.java", "OverrideIgnored.java"};
    private static final String OVERRIDE_PREFIX = "settings.blacklist";
    private static final String BLACK = OVERRIDE_PREFIX + ".BlackException";
    private static final String[] EXC_FILES = new String[]{BLACK_IMPL, "blacklist/BlackException.java", "ExceptionIgnored.java"};

    @Override
    protected String getTestDir() {
        return "settings/";
    }

    public void testEstimationFound() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(true);
        CheckMateSettings.getInstance().getOverrideBlackList().remove(OVERRIDE_PREFIX);
        expect(OTHER_UNCHECKED);
    }

    public void testEstimationIgnoredWhenBlackListed() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(true);
        CheckMateSettings.getInstance().getOverrideBlackList().add(OVERRIDE_PREFIX);
        expect(CUSTOM_UNCHECKED);
    }

    public void testOverrideFoundWhenEstimationOff() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(false);
        CheckMateSettings.getInstance().getOverrideBlackList().remove(OVERRIDE_PREFIX);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testOverrideIgnoredWhenEstimationOffAndBlacklisted() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(false);
        CheckMateSettings.getInstance().getOverrideBlackList().add("settings.blacklist");
        expect(CUSTOM_UNCHECKED);
    }

    public void testExceptionFoundWhenNotBlackListed() {
        configure(EXC_FILES);
        CheckMateSettings.getInstance().getExcBlackList().remove(BLACK);
        expect(BLACK);
    }

    public void testExceptionIgnoredWhenBlackListed() {
        configure(EXC_FILES);
        CheckMateSettings.getInstance().getExcBlackList().add(BLACK);
        expectNone();
    }
}
