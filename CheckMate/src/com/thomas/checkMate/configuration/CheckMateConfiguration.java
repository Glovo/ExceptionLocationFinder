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

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "CheckMateConfiguration";
    }

//    @Nullable
//    @Override
//    public CheckMateSettings getState() {
//        return checkMateSettings;
//    }
//
//    @Override
//    public void loadState(CheckMateSettings checkMateSettings) {
//        this.checkMateSettings = checkMateSettings;
//        if (settingsForm == null) {
//            settingsForm = new SettingsForm(checkMateSettings);
//        } else {
//            settingsForm.reset(checkMateSettings);
//        }
//    }

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
        return settingsForm.getIncludeJavaSrc() != checkMateSettings.getIncludeJavaSrc();
    }

    @Override
    public void apply() throws ConfigurationException {
        checkMateSettings.setIncludeJavaSrc(settingsForm.getIncludeJavaSrc());
    }

    @Override
    public void reset() {
        settingsForm.reset(checkMateSettings);
    }

    @Override
    public void disposeUIResources() {
    }
}
