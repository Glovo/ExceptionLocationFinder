package com.thomas.checkMate.util;

import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

public class ConfigurationUtil {
    public static final String[] DEFAULT_FILES = new String[]{
            "other_package/OtherCustomUncheckedException.java",
            "BridgeSuper.java",
            "CustomCheckedException.java",
            "CustomUncheckedException.java",
            "IgnoredSuperInterface.java",
            "OtherThrower.java",
            "SuperInterface.java",
            "SuperThrower.java",
            "TestBase.java",
            "Thrower.java"
    };
    private static final String BASE_FILE_DIR = "base/";

    public static void configure(CodeInsightTestFixture fixture, String testFileDir, String... testFiles) {
        int totalLength = DEFAULT_FILES.length + testFiles.length;
        String[] allFiles = new String[totalLength];
        for (String defaultFile : DEFAULT_FILES) {
            allFiles[--totalLength] = BASE_FILE_DIR + defaultFile;
        }
        for (String testFile : testFiles) {
            allFiles[--totalLength] = testFileDir + testFile;
        }
        fixture.configureByFiles(allFiles);
    }
}
