package com.thomas.checkMate.discovery;

import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

public class ExceptionFinderTestUtil {
    private static final String BASE_FILE_DIR = "base/";
    public static final String[] COMMON_TEST_FILES = new String[]{
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

    public static void configure(CodeInsightTestFixture fixture, String testFileDir, String... testFiles) {
        int totalLength = COMMON_TEST_FILES.length + testFiles.length;
        String[] allFiles = new String[COMMON_TEST_FILES.length + testFiles.length];
        for (String commonFile : COMMON_TEST_FILES) {
            allFiles[--totalLength] = BASE_FILE_DIR + commonFile;
        }
        for (String testFile : testFiles) {
            allFiles[--totalLength] = testFileDir + testFile;
        }
        fixture.configureByFiles(allFiles);
    }
}
