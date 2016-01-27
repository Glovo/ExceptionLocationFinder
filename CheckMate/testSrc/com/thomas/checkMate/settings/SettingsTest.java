package com.thomas.checkMate.settings;

import com.thomas.checkMate.CheckMateTest;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class SettingsTest extends CheckMateTest {
    private static final String BLACK_IMPL = "blacklist/BlackImpl.java";
    private static final String[] OVERRIDE_FILES = new String[]{BLACK_IMPL, "blacklist/BlackSuper.java", "OverrideIgnored.java"};
    private static final String BLACKLIST_PREFIX = "settings.blacklist.";
    private static final String OVERRIDE_REGEX = BLACKLIST_PREFIX + "*";
    private static final String EXCEPTION_REGEX = OVERRIDE_REGEX;
    private static final String BLACK_EXCEPTION = BLACKLIST_PREFIX + "BlackException";
    private static final String BLACK_EXCEPTION_FILE = "blacklist/BlackException.java";
    private static final String[] EXC_FILES = new String[]{BLACK_IMPL, BLACK_EXCEPTION_FILE, "ExceptionIgnored.java"};
    private static final String BLACK_CLASS = BLACKLIST_PREFIX + "BlackClass";
    private static final String BLACK_CLASS_FILE = "blacklist/BlackClass.java";
    private static final String[] CLASS_FILES = new String[]{BLACK_CLASS_FILE, BLACK_EXCEPTION_FILE, "ClassIgnored.java"};

    @Override
    protected String getTestDir() {
        return "settings/";
    }

    public void testEstimationFound() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(true);
        CheckMateSettings.getInstance().getOverrideBlackList().remove(OVERRIDE_REGEX);
        expect(OTHER_UNCHECKED);
    }

    public void testEstimationIgnoredWhenBlackListed() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(true);
        CheckMateSettings.getInstance().getOverrideBlackList().add(OVERRIDE_REGEX);
        expect(CUSTOM_UNCHECKED);
    }

    public void testOverrideFoundWhenEstimationOff() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(false);
        CheckMateSettings.getInstance().getOverrideBlackList().remove(OVERRIDE_REGEX);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testOverrideIgnoredWhenEstimationOffAndBlacklisted() {
        configure(OVERRIDE_FILES);
        CheckMateSettings.getInstance().setEstimateInheritors(false);
        CheckMateSettings.getInstance().getOverrideBlackList().add(OVERRIDE_REGEX);
        expect(CUSTOM_UNCHECKED);
    }

    public void testExceptionFoundWhenNotBlackListed() {
        configure(EXC_FILES);
        CheckMateSettings.getInstance().getExcBlackList().remove(EXCEPTION_REGEX);
        expect(BLACK_EXCEPTION);
    }

    public void testExceptionIgnoredWhenBlackListed() {
        configure(EXC_FILES);
        CheckMateSettings.getInstance().getExcBlackList().add(EXCEPTION_REGEX);
        expectNone();
    }

    public void testClassMethodIgnoredWhenBlackListed() {
        configure(CLASS_FILES);
        CheckMateSettings.getInstance().getClassBlackList().add(BLACK_CLASS);
        expectNone();
    }

    public void testClassMethodVisitedWhenNotBlackListed() {
        configure(CLASS_FILES);
        CheckMateSettings.getInstance().getClassBlackList().remove(BLACK_CLASS);
        expect(BLACK_EXCEPTION);
    }
}
