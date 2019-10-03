package com.glovoapp.plugins.infrastructure.configuration;

import static com.glovoapp.plugins.infrastructure.configuration.Settings.currentSettings;

import com.intellij.openapi.options.SearchableConfigurable;
import java.util.Collection;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ExceptionLocationFinderConfiguration implements SearchableConfigurable {

    private final SettingsForm settingsForm = new SettingsForm();

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

    @Override
    public JComponent createComponent() {
        return settingsForm;
    }

    @Override
    public boolean isModified() {
        final Settings settings = currentSettings();
        return settingsForm.getEstimateInheritors() != settings.estimateInheritors()
            || settingsForm.getExactSearch() != settings.isExactSearch()
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
        Settings.set(new Settings(
            currentSettings().isFirstRun(),
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
        settingsForm.reset();
    }

    @Override
    public void disposeUIResources() {
    }

}
