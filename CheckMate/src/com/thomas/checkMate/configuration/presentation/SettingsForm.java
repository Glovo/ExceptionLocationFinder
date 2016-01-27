package com.thomas.checkMate.configuration.presentation;

import com.thomas.checkMate.configuration.CheckMateSettings;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsForm extends JPanel {
    private static final String INCLUDE_JAVA_DOCS = "Include exceptions found in @throws JavaDoc clauses";
    private static final String INCLUDE_ERRORS = "Include errors in search results";
    private static final String ESTIMATE_INHERITORS = "Estimate plausible overrides of an encountered method (default: search through method and all overrides)";
    private static final String CLASS_BLACKLIST = "Class Blacklist (don't search through a class's methods when the FQN matches the following regexes)";
    private static final String ADD_TITLE = "Add to ";
    private static final String REGEX = "Regex: ";
    private static final String OVERRIDE_BLACKLIST = "Override Blacklist (don't search for overrides of a class's methods when the FQN matches the following regexes)";
    private static final String EXC_BLACKLIST = "Exception Blacklist (exclude the following exceptions from search results)";
    private JCheckBox javaDocsCBox;
    private JCheckBox errorsCBox;
    private JCheckBox estimateCBox;
    private DefaultListModel<String> classBlackListModel;
    private DefaultListModel<String> overrideBlackListModel;
    private DefaultListModel<String> excBlackListModel;


    public SettingsForm(CheckMateSettings settings) {
        this.setLayout(new VerticalLayout());
        javaDocsCBox = new JCheckBox(INCLUDE_JAVA_DOCS);
        this.add(javaDocsCBox);
        errorsCBox = new JCheckBox(INCLUDE_ERRORS);
        this.add(errorsCBox);
        estimateCBox = new JCheckBox(ESTIMATE_INHERITORS);
        this.add(estimateCBox);
        classBlackListModel = new DefaultListModel<>();
        this.addListModel(classBlackListModel, CLASS_BLACKLIST, ADD_TITLE + "class blacklist");
        overrideBlackListModel = new DefaultListModel<>();
        this.addListModel(overrideBlackListModel, OVERRIDE_BLACKLIST, ADD_TITLE + "override blacklist");
        excBlackListModel = new DefaultListModel<>();
        this.addListModel(excBlackListModel, EXC_BLACKLIST, ADD_TITLE + "exception blacklist");
        reset(settings);
    }

    public boolean getIncludeJavaDocs() {
        return javaDocsCBox.isSelected();
    }

    public boolean getIncludeErrors() {
        return errorsCBox.isSelected();
    }

    public boolean getEstimateInheritors() {
        return estimateCBox.isSelected();
    }

    public List<String> getClassBlackList() {
        return modelToList(classBlackListModel);
    }

    public List<String> getOverrideBlackList() {
        return modelToList(overrideBlackListModel);
    }

    public List<String> getExcBlackList() {
        return modelToList(excBlackListModel);
    }

    private void addListModel(DefaultListModel<String> listModel, String label, String addTitle) {
        this.add(new SettingsListDecorator().decorate(new JList<>(listModel), listModel, label, addTitle, REGEX));
    }

    public void reset(CheckMateSettings settings) {
        javaDocsCBox.setSelected(settings.getIncludeJavaDocs());
        errorsCBox.setSelected(settings.getIncludeErrors());
        estimateCBox.setSelected(settings.getEstimateInheritors());
        resetListModel(classBlackListModel, settings.getClassBlackList());
        resetListModel(overrideBlackListModel, settings.getOverrideBlackList());
        resetListModel(excBlackListModel, settings.getExcBlackList());
    }

    private void resetListModel(DefaultListModel<String> modelToReset, List<String> settingsList) {
        modelToReset.removeAllElements();
        settingsList.forEach(modelToReset::addElement);
    }

    private List<String> modelToList(DefaultListModel<String> listModel) {
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            resultList.add(i, listModel.getElementAt(i));
        }
        return resultList;
    }
}
