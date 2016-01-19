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
    public boolean includeInheritors = false;
    public List<String> srcWhiteList = new ArrayList<>();
    public List<String> excBlackList = new ArrayList<>();

    public CheckMateSettings() {
        srcWhiteList.add("javax.persistence");
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

    public boolean getIncludeInheritors() {
        return includeInheritors;
    }

    public void setIncludeInheritors(boolean includeInheritors) {
        this.includeInheritors = includeInheritors;
    }

    public List<String> getSrcWhiteList() {
        return srcWhiteList;
    }

    public void setSrcWhiteList(List<String> srcWhiteList) {
        this.srcWhiteList = srcWhiteList;
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
