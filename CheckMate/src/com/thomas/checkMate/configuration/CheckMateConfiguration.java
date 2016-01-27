package com.thomas.checkMate.configuration;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.thomas.checkMate.configuration.presentation.SettingsForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
        boolean modified = settingsForm.getIncludeJavaDocs() != checkMateSettings.getIncludeJavaDocs();
        modified = modified || settingsForm.getIncludeErrors() != checkMateSettings.getIncludeErrors();
        modified = modified || settingsForm.getEstimateInheritors() != checkMateSettings.getEstimateInheritors();
        modified = modified || !settingsForm.getOverrideBlackList().containsAll(checkMateSettings.getOverrideBlackList());
        modified = modified || !checkMateSettings.getOverrideBlackList().containsAll(settingsForm.getOverrideBlackList());
        modified = modified || !settingsForm.getExcBlackList().containsAll(checkMateSettings.getExcBlackList());
        modified = modified || !checkMateSettings.getExcBlackList().containsAll(settingsForm.getExcBlackList());
        modified = modified || !settingsForm.getClassBlackList().containsAll(checkMateSettings.getClassBlackList());
        modified = modified || !checkMateSettings.getClassBlackList().containsAll(settingsForm.getClassBlackList());
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        checkMateSettings.setIncludeJavaDocs(settingsForm.getIncludeJavaDocs());
        checkMateSettings.setIncludeErrors(settingsForm.getIncludeErrors());
        checkMateSettings.setEstimateInheritors(settingsForm.getEstimateInheritors());
        checkMateSettings.setClassBlackList(settingsForm.getClassBlackList());
        checkMateSettings.setOverrideBlackList(settingsForm.getOverrideBlackList());
        checkMateSettings.setExcBlackList(settingsForm.getExcBlackList());
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
