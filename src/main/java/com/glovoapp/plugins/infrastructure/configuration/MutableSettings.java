package com.glovoapp.plugins.infrastructure.configuration;

import java.util.List;

/**
 * Only for serialization.
 */
final class MutableSettings {

    public boolean firstRun;
    public boolean estimateInheritors;
    public boolean exactSearch;
    public List<String> classBlackList;
    public List<String> overrideBlackList;
    public List<String> classWhiteList;
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

    public final boolean isFirstRun() {
        return firstRun;
    }

    public final void setFirstRun(final boolean firstRun) {
        this.firstRun = firstRun;
    }

    public final boolean isEstimateInheritors() {
        return estimateInheritors;
    }

    public final void setEstimateInheritors(final boolean estimateInheritors) {
        this.estimateInheritors = estimateInheritors;
    }

    public final boolean isExactSearch() {
        return exactSearch;
    }

    public final void setExactSearch(final boolean exactSearch) {
        this.exactSearch = exactSearch;
    }

    public final List<String> getClassBlackList() {
        return classBlackList;
    }

    public final void setClassBlackList(final List<String> classBlackList) {
        this.classBlackList = classBlackList;
    }

    public final List<String> getOverrideBlackList() {
        return overrideBlackList;
    }

    public final void setOverrideBlackList(final List<String> overrideBlackList) {
        this.overrideBlackList = overrideBlackList;
    }

    public final List<String> getClassWhiteList() {
        return classWhiteList;
    }

    public final void setClassWhiteList(final List<String> classWhiteList) {
        this.classWhiteList = classWhiteList;
    }

    public final List<String> getOverrideWhiteList() {
        return overrideWhiteList;
    }

    public final void setOverrideWhiteList(final List<String> overrideWhiteList) {
        this.overrideWhiteList = overrideWhiteList;
    }

}
