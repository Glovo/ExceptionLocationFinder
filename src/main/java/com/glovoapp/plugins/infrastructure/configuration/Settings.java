package com.glovoapp.plugins.infrastructure.configuration;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public final class Settings {

    public static final String NAME = "ExceptionLocationFinderSettings";

    private final boolean firstRun;
    private final boolean estimateInheritors;
    private final boolean exactSearch;
    private final List<String> classBlackList;
    private final List<String> overrideBlackList;
    private final List<String> classWhiteList;
    private final List<String> overrideWhiteList;

    Settings(final boolean firstRun,
             final boolean estimateInheritors,
             final boolean exactSearch,
             final List<String> classBlackList,
             final List<String> overrideBlackList,
             final List<String> classWhiteList,
             final List<String> overrideWhiteList) {
        this.firstRun = firstRun;
        this.estimateInheritors = estimateInheritors;
        this.exactSearch = exactSearch;
        this.classBlackList = unmodifiableList(classBlackList);
        this.overrideBlackList = unmodifiableList(overrideBlackList);
        this.classWhiteList = unmodifiableList(classWhiteList);
        this.overrideWhiteList = unmodifiableList(overrideWhiteList);
    }

    static Settings clean() {
        return new Settings(
            true,
            false,
            false,
            asList(
                "java.lang.*",
                "java.util.*"
            ),
            asList(
                "java.*",
                "org.xml.*",
                "org.omg.*",
                "sun.*"
            ),
            emptyList(),
            emptyList()
        );
    }

    final MutableSettings makeMutable() {
        MutableSettings mutableSettings = new MutableSettings();
        mutableSettings.firstRun = firstRun;
        mutableSettings.estimateInheritors = estimateInheritors;
        mutableSettings.exactSearch = exactSearch;
        mutableSettings.classBlackList = new ArrayList<>(classBlackList);
        mutableSettings.overrideBlackList = new ArrayList<>(overrideBlackList);
        mutableSettings.classWhiteList = new ArrayList<>(classWhiteList);
        mutableSettings.overrideWhiteList = new ArrayList<>(overrideWhiteList);
        return mutableSettings;
    }

    public final Settings withFirstRun(final boolean firstRun) {
        return new Settings(
            firstRun,
            estimateInheritors,
            exactSearch,
            classBlackList,
            overrideBlackList,
            classWhiteList,
            overrideWhiteList
        );
    }

    public static Settings currentSettings() {
        return ExceptionLocationFinderSettings.getInstance()
                                              .getSettings();
    }

    public static void set(final Settings settings) {
        ExceptionLocationFinderSettings.getInstance()
                                       .setSettings(settings);
    }

    public final boolean isFirstRun() {
        return firstRun;
    }

    public final boolean estimateInheritors() {
        return estimateInheritors;
    }

    public final boolean isExactSearch() {
        return exactSearch;
    }

    public final List<String> getClassBlackList() {
        return unmodifiableList(classBlackList);
    }

    public final List<String> getOverrideBlackList() {
        return unmodifiableList(overrideBlackList);
    }

    public final List<String> getClassWhiteList() {
        return unmodifiableList(classWhiteList);
    }

    public final List<String> getOverrideWhiteList() {
        return unmodifiableList(overrideWhiteList);
    }

}
