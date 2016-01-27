package com.thomas.checkMate.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "CheckMateSettings", storages = {@Storage(id = "CheckMateSettings", file = StoragePathMacros.APP_CONFIG + "/CheckMate.xml")})
public class CheckMateSettings implements ApplicationComponent, PersistentStateComponent<CheckMateSettings> {
    private static CheckMateSettings instance;
    //These fields have to be public for inclusion in Persistent State.
    public boolean includeJavaDocs = true;
    public boolean includeErrors = false;
    public boolean estimateInheritors = false;
    public List<String> overrideBlackList = new ArrayList<>();
    public List<String> excBlackList = new ArrayList<>();

    public CheckMateSettings() {
        overrideBlackList.add("java");
        overrideBlackList.add("org.xml");
        overrideBlackList.add("org.omg");
        overrideBlackList.add("sun");
        excBlackList.add("java.lang.NullPointerException");
        excBlackList.add("java.lang.IllegalArgumentException");
        excBlackList.add("java.lang.IndexOutOfBoundsException");
    }

    public static CheckMateSettings getInstance() {
        if (instance == null) {
            synchronized (CheckMateSettings.class) {
                if (instance == null) {
                    instance = ApplicationManager.getApplication().getComponent(CheckMateSettings.class);
                }
            }
        }
        return instance;
    }

    public boolean getIncludeJavaDocs() {
        return includeJavaDocs;
    }

    public void setIncludeJavaDocs(boolean includeJavaDocs) {
        this.includeJavaDocs = includeJavaDocs;
    }

    public boolean getIncludeErrors() {
        return includeErrors;
    }

    public void setIncludeErrors(boolean includeErrors) {
        this.includeErrors = includeErrors;
    }

    public boolean getEstimateInheritors() {
        return estimateInheritors;
    }

    public void setEstimateInheritors(boolean estimateInheritors) {
        this.estimateInheritors = estimateInheritors;
    }

    public List<String> getOverrideBlackList() {
        return overrideBlackList;
    }

    public void setOverrideBlackList(List<String> overrideBlackList) {
        this.overrideBlackList = overrideBlackList;
    }

    public List<String> getExcBlackList() {
        return excBlackList;
    }

    public void setExcBlackList(List<String> excBlackList) {
        this.excBlackList = excBlackList;
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
