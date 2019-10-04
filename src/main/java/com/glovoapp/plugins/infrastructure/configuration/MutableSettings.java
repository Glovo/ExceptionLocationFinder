package com.glovoapp.plugins.infrastructure.configuration;

import java.util.List;

/**
 * Only for serialization.
 */
final class MutableSettings {

    @SuppressWarnings("WeakerAccess")
    public boolean firstRun;
    @SuppressWarnings("WeakerAccess")
    public boolean estimateInheritors;
    @SuppressWarnings("WeakerAccess")
    public boolean exactSearch;
    @SuppressWarnings("WeakerAccess")
    public List<String> classBlackList;
    @SuppressWarnings("WeakerAccess")
    public List<String> overrideBlackList;
    @SuppressWarnings("WeakerAccess")
    public List<String> classWhiteList;
    @SuppressWarnings("WeakerAccess")
    public List<String> overrideWhiteList;

    final Settings makeImmutable() {
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

}
