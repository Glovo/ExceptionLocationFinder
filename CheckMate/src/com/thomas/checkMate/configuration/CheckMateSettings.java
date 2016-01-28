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
    public String version;
    public boolean firstRun;
    public boolean includeJavaDocs;
    public boolean includeErrors;
    public boolean estimateInheritors;
    public List<String> classBlackList;
    public List<String> overrideBlackList;
    public List<String> excBlackList;

    public CheckMateSettings() {
        reset();
    }

    public void reset() {
        firstRun = true;
        includeJavaDocs = true;
        includeErrors = false;
        estimateInheritors = false;
        classBlackList = new ArrayList<>();
        excBlackList = new ArrayList<>();
        overrideBlackList = new ArrayList<>();
        classBlackList.add("java.lang.*");
        classBlackList.add("java.util.*");
        overrideBlackList.add("java.*");
        overrideBlackList.add("org.xml.*");
        overrideBlackList.add("org.omg.*");
        overrideBlackList.add("sun.*");
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public List<String> getClassBlackList() {
        return classBlackList;
    }

    public void setClassBlackList(List<String> classBlackList) {
        this.classBlackList = classBlackList;
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
