package com.thomas.checkMate;

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;

public class ExceptionFinderTestUtil {
    private static final String BASE_FILE_DIR = "base/";
    private static final String TEST_FILE_DIR = "exception_finder/";
    public static final String[] COMMON_TEST_FILES = new String[]{
            "CustomCheckedException.java",
            "CustomUncheckedException.java",
            "SuperInterface.java",
            "SuperInterface.java",
            "SuperThrower.java",
            "TestBase.java",
            "Thrower.java",
            "other_package/OtherCustomUncheckedException.java"};

    public static void configure(JavaCodeInsightTestFixture fixture, String... testFiles) {
        int totalLength = COMMON_TEST_FILES.length + testFiles.length;
        String[] allFiles = new String[COMMON_TEST_FILES.length + testFiles.length];
        for (String commonFile : COMMON_TEST_FILES) {
            allFiles[--totalLength] = BASE_FILE_DIR + commonFile;
        }
        for (String testFile : testFiles) {
            allFiles[--totalLength] = TEST_FILE_DIR + testFile;
        }
        fixture.configureByFiles(allFiles);
    }
}
