package com.thomas.checkMate.configuration.presentation;

import com.thomas.checkMate.configuration.CheckMateSettings;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsForm extends JPanel {
    private static final String INCLUDE_JAVA_DOCS = "Include exceptions found in @throws JavaDoc clauses";
    private static final String INCLUDE_ERRORS = "Include errors in search results";
    private static final String INCLUDE_INHERITORS = "Search through all overrides of encountered methods (may include false positives and increase search times)";
    private static final String SRC_WHITELIST = "Sources whitelist (by default all classes from java*, org.xml* and org.omg* are ignored)";
    private static final String SRC_WHITELIST_DETAIL = "Include all classes with a FQN that starts with: ";
    private static final String SRC_WHITELIST_TITLE = "Add to whitelist";
    private static final String EXC_BLACKLIST = "Exception blacklist (exclude the following exceptions from search results)";
    private static final String EXC_BLACKLIST_DETAIL = "FQN of exception to ignore: ";
    private static final String EXC_BLACKLIST_TITLE = "Add to blacklist";
    private JCheckBox javaDocsCBox;
    private JCheckBox errorsCBox;
    private JCheckBox inheritorsCBox;
    private DefaultListModel<String> srcWhiteListModel;
    private DefaultListModel<String> excBlackListModel;


    public SettingsForm(CheckMateSettings settings) {
        this.setLayout(new VerticalLayout());
        javaDocsCBox = new JCheckBox(INCLUDE_JAVA_DOCS);
        this.add(javaDocsCBox);
        errorsCBox = new JCheckBox(INCLUDE_ERRORS);
        this.add(errorsCBox);
        inheritorsCBox = new JCheckBox(INCLUDE_INHERITORS);
        this.add(inheritorsCBox);
        srcWhiteListModel = new DefaultListModel<>();
        this.add(new SettingsListDecorator().decorate(new JList<>(srcWhiteListModel), srcWhiteListModel,
                SRC_WHITELIST, SRC_WHITELIST_TITLE, SRC_WHITELIST_DETAIL));
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

    public boolean getIncludeInheritors() {
        return inheritorsCBox.isSelected();
    }

    public List<String> getSrcWhiteList() {
        return modelToList(srcWhiteListModel);
    }

    public List<String> getExcBlackList() {
        return modelToList(excBlackListModel);
    }

    public void reset(CheckMateSettings settings) {
        javaDocsCBox.setSelected(settings.getIncludeJavaDocs());
        errorsCBox.setSelected(settings.getIncludeErrors());
        inheritorsCBox.setSelected(settings.getIncludeInheritors());
        resetListModel(srcWhiteListModel, settings.getSrcWhiteList());
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
