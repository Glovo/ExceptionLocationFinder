package com.thomas.checkMate.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "CheckMateSettings", storages = {@Storage(id = "CheckMateSettings", file = StoragePathMacros.APP_CONFIG + "/CheckMate.xml")})
public class CheckMateSettings implements ApplicationComponent, PersistentStateComponent<CheckMateSettings> {
    //Has to be public for inclusion in Persistent State.
    public boolean includeJavaSrc = false;

    public boolean getIncludeJavaSrc() {
        return includeJavaSrc;
    }

    public void setIncludeJavaSrc(boolean isJavaSource) {
        this.includeJavaSrc = isJavaSource;
    }

    public static CheckMateSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(CheckMateSettings.class);
    }

    @Override
    public void initComponent() {
        //Do nothing
    }

    @Override
    public void disposeComponent() {
        //Do nothing
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "CheckMateSettings";
    }

    @Nullable
    @Override
    public CheckMateSettings getState() {
        return this;
    }

    @Override
    public void loadState(CheckMateSettings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }
}
