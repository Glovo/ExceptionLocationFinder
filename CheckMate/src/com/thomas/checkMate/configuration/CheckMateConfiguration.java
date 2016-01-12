package com.thomas.checkMate.configuration;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

//@State(name = "CheckMateConfiguration", storages = {@Storage(id = "CheckMateConfiguration", file = StoragePathMacros.APP_CONFIG + "/CheckMate.xml")})
public class CheckMateConfiguration implements ApplicationComponent, SearchableConfigurable {
    private CheckMateSettings checkMateSettings;
    private SettingsForm settingsForm;

    public CheckMateConfiguration(CheckMateSettings checkMateSettings) {
        this.checkMateSettings = checkMateSettings;
    }

    @NotNull
    @Override
    public String getId() {
        return "CheckMateConfiguration";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "CheckMate";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsForm == null) {
            if (checkMateSettings == null) {
                checkMateSettings = CheckMateSettings.getInstance();
            }
            settingsForm = new SettingsForm(checkMateSettings);
        }
        return settingsForm;
    }

    @Override
    public boolean isModified() {
        boolean modified = settingsForm.getIncludeJavaSrc() != checkMateSettings.getIncludeJavaSrc();
        modified = modified || settingsForm.getIncludeJavaDocs() != checkMateSettings.getIncludeJavaDocs();
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        checkMateSettings.setIncludeJavaSrc(settingsForm.getIncludeJavaSrc());
        checkMateSettings.setIncludeJavaDocs(settingsForm.getIncludeJavaDocs());
    }

    @Override
    public void reset() {
        settingsForm.reset(checkMateSettings);
    }

    public void initComponent() {
        //Do nothing
    }

    public void disposeComponent() {
        //Do nothing
    }

    @NotNull
    public String getComponentName() {
        return "CheckMateConfiguration";
    }

    @Override
    public void disposeUIResources() {
        //Do nothing
    }
}
