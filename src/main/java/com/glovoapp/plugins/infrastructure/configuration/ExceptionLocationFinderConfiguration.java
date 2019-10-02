package com.glovoapp.plugins.infrastructure.configuration;

import com.glovoapp.plugins.infrastructure.configuration.ExceptionLocationFinderSettings.State;
import com.intellij.openapi.options.SearchableConfigurable;
import com.thomas.checkMate.configuration.presentation.SettingsForm;
import java.util.Collection;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExceptionLocationFinderConfiguration implements SearchableConfigurable {

    private final ExceptionLocationFinderSettings settings = ExceptionLocationFinderSettings.getInstance();
    private final SettingsForm settingsForm = new SettingsForm(settings);

    @NotNull
    @Override
    public String getId() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Exception Location Finder";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return settingsForm;
    }

    @Override
    public boolean isModified() {
        return settingsForm.getEstimateInheritors() != settings.estimateInheritors()
            || settingsForm.getExactSearch() != settings.exactSearch()
            || collectionsDiffer(settingsForm.getOverrideBlackList(),
            settings.getOverrideBlackList())
            || collectionsDiffer(settingsForm.getClassBlackList(),
            settings.getClassBlackList())
            || collectionsDiffer(settingsForm.getClassWhiteList(),
            settings.getClassWhiteList())
            || collectionsDiffer(settingsForm.getOverrideWhiteList(),
            settings.getOverrideWhiteList());
    }

    private static <T> boolean collectionsDiffer(Collection<T> first, Collection<T> second) {
        return !(first.containsAll(second) && second.containsAll(first));
    }

    @Override
    public void apply() {
        settings.setState(new State(
            settings.isFirstRun(),
            settingsForm.getEstimateInheritors(),
            settingsForm.getExactSearch(),
            settingsForm.getClassBlackList(),
            settingsForm.getOverrideBlackList(),
            settingsForm.getClassWhiteList(),
            settingsForm.getOverrideWhiteList()
        ));
    }

    @Override
    public void reset() {
        settingsForm.reset(settings);
    }

    @Override
    public void disposeUIResources() {
    }

}
