package com.glovoapp.plugins.infrastructure.configuration;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@State(
    name = ExceptionLocationFinderSettings.NAME,
    storages = {@Storage(value = "ExceptionLocationFinderSettings.xml")}
)
public class ExceptionLocationFinderSettings implements
    PersistentStateComponent<ExceptionLocationFinderSettings.State> {

    public static final String NAME = "ExceptionLocationFinderSettings";

    private static ExceptionLocationFinderSettings instance;

    public static class State {

        private final boolean firstRun;
        private final boolean estimateInheritors;
        private final boolean exactSearch;
        private final List<String> classBlackList;
        private final List<String> overrideBlackList;
        private final List<String> classWhiteList;
        private final List<String> overrideWhiteList;

        State(final boolean firstRun,
              final boolean estimateInheritors,
              final boolean exactSearch,
              final List<String> classBlackList,
              final List<String> overrideBlackList,
              final List<String> classWhiteList,
              final List<String> overrideWhiteList) {
            this.firstRun = firstRun;
            this.estimateInheritors = estimateInheritors;
            this.exactSearch = exactSearch;
            this.classBlackList = classBlackList;
            this.overrideBlackList = overrideBlackList;
            this.classWhiteList = classWhiteList;
            this.overrideWhiteList = overrideWhiteList;
        }

        private static State clean() {
            return new State(
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

        public final State withFirstRun(boolean firstRun) {
            return new State(
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

    private State state;

    public ExceptionLocationFinderSettings() {
        this.state = State.clean();
    }

    public static ExceptionLocationFinderSettings getInstance() {
        if (instance == null) {
            synchronized (ExceptionLocationFinderSettings.class) {
                if (instance == null) {
                    instance = ApplicationManager.getApplication()
                                                 .getComponent(
                                                     ExceptionLocationFinderSettings.class);
                }
            }
        }
        return instance;
    }

    @NotNull
    @Override
    public ExceptionLocationFinderSettings.State getState() {
        return state;
    }

    @Override
    public void loadState(final @NotNull ExceptionLocationFinderSettings.State settings) {
        this.state = settings;
    }

    public final boolean isFirstRun() {
        return state.firstRun;
    }

    public final boolean estimateInheritors() {
        return state.estimateInheritors;
    }

    public final boolean exactSearch() {
        return state.exactSearch;
    }

    public final List<String> getClassBlackList() {
        return unmodifiableList(state.classBlackList);
    }

    public final List<String> getOverrideBlackList() {
        return unmodifiableList(state.overrideBlackList);
    }

    public final List<String> getClassWhiteList() {
        return unmodifiableList(state.classWhiteList);
    }

    public final List<String> getOverrideWhiteList() {
        return unmodifiableList(state.overrideWhiteList);
    }

    public final void setState(final @NotNull ExceptionLocationFinderSettings.State state) {
        this.state = state;
    }

}
