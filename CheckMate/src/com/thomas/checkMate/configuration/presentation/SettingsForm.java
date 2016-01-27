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
    private static final String OVERRIDE_BLACKLIST = "Override Blacklist (don't search for overrides of a class's methods when the FQN matches the following regexes)";
    private static final String OVERRIDE_BLACKLIST_DETAIL = "Exclude all classes with an FQN that matches: ";
    private static final String OVERRIDE_BLACKLIST_TITLE = "Add to override blacklist";
    private static final String EXC_BLACKLIST = "Exception blacklist (exclude the following exceptions from search results)";
    private static final String EXC_BLACKLIST_DETAIL = "FQN of exception to ignore: ";
    private static final String EXC_BLACKLIST_TITLE = "Add to exception blacklist";
    private JCheckBox javaDocsCBox;
    private JCheckBox errorsCBox;
    private JCheckBox estimateCBox;
    private DefaultListModel<String> overrideBlackList;
    private DefaultListModel<String> excBlackListModel;


    public SettingsForm(CheckMateSettings settings) {
        this.setLayout(new VerticalLayout());
        javaDocsCBox = new JCheckBox(INCLUDE_JAVA_DOCS);
        this.add(javaDocsCBox);
        errorsCBox = new JCheckBox(INCLUDE_ERRORS);
        this.add(errorsCBox);
        estimateCBox = new JCheckBox(ESTIMATE_INHERITORS);
        this.add(estimateCBox);
        overrideBlackList = new DefaultListModel<>();
        this.add(new SettingsListDecorator().decorate(new JList<>(overrideBlackList), overrideBlackList,
                OVERRIDE_BLACKLIST, OVERRIDE_BLACKLIST_TITLE, OVERRIDE_BLACKLIST_DETAIL));
        excBlackListModel = new DefaultListModel<>();
        this.add(new SettingsListDecorator().decorate(new JList<>(excBlackListModel), excBlackListModel,
                EXC_BLACKLIST, EXC_BLACKLIST_TITLE, EXC_BLACKLIST_DETAIL));
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

    public List<String> getOverrideBlackList() {
        return modelToList(overrideBlackList);
    }

    public List<String> getExcBlackList() {
        return modelToList(excBlackListModel);
    }

    public void reset(CheckMateSettings settings) {
        javaDocsCBox.setSelected(settings.getIncludeJavaDocs());
        errorsCBox.setSelected(settings.getIncludeErrors());
        estimateCBox.setSelected(settings.getEstimateInheritors());
        resetListModel(overrideBlackList, settings.getOverrideBlackList());
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
