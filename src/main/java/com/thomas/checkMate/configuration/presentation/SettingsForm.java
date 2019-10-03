package com.thomas.checkMate.configuration.presentation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.glovoapp.plugins.infrastructure.configuration.ExceptionLocationFinderSettings;
import com.glovoapp.plugins.infrastructure.configuration.Settings;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import org.jdesktop.swingx.VerticalLayout;

public class SettingsForm extends JPanel {

    private static final String ESTIMATE_INHERITORS = "Estimate plausible overrides of an encountered method (default: search through method and all overrides)";
    private static final String EXACT_SEARCH = "Search for exactly the exception class specified (ignore derived exceptions)";
    private static final String ADD_TITLE = "Add to ";

    private static final String CLASS_BLACKLIST = "Class Blacklist";
    private static final String CLASS_BLACKLIST_DETAIL = "don't search through a class's methods when the FQN matches the following regexes";
    private static final String OVERRIDE_BLACKLIST = "Override Blacklist";
    private static final String OVERRIDE_BLACKLIST_DETAIL = "don't search for overrides of a class's methods when the FQN matches the following regexes";

    private static final String CLASS_WHITELIST = "Class Whitelist (ignored when empty)";
    private static final String CLASS_WHITELIST_DETAIL = "search only through a class's methods when the FQN matches the following regexes";
    private static final String OVERRIDE_WHITELIST = "Override Whitelist (ignored when empty)";
    private static final String OVERRIDE_WHITELIST_DETAIL = "search only for overrides of a class's methods when the FQN matches the following regexes";

    private final JCheckBox estimateCheckBox;
    private final JCheckBox exactSearchCheckBox;
    private final DefaultListModel<String> classBlackListModel;
    private final DefaultListModel<String> overrideBlackListModel;

    private final DefaultListModel<String> classWhiteListModel;
    private final DefaultListModel<String> overrideWhiteListModel;


    public SettingsForm(final ExceptionLocationFinderSettings settings) {
        setLayout(new VerticalLayout());

        estimateCheckBox = new JCheckBox(ESTIMATE_INHERITORS);
        add(estimateCheckBox);

        exactSearchCheckBox = new JCheckBox(EXACT_SEARCH);
        add(exactSearchCheckBox);

        classBlackListModel = new DefaultListModel<>();
        addListModel(classBlackListModel, CLASS_BLACKLIST, CLASS_BLACKLIST_DETAIL,
            ADD_TITLE + CLASS_BLACKLIST);
        overrideBlackListModel = new DefaultListModel<>();
        addListModel(overrideBlackListModel, OVERRIDE_BLACKLIST, OVERRIDE_BLACKLIST_DETAIL,
            ADD_TITLE + OVERRIDE_BLACKLIST);

        classWhiteListModel = new DefaultListModel<>();
        addListModel(classWhiteListModel, CLASS_WHITELIST, CLASS_WHITELIST_DETAIL,
            ADD_TITLE + CLASS_WHITELIST);
        overrideWhiteListModel = new DefaultListModel<>();
        addListModel(overrideWhiteListModel, OVERRIDE_WHITELIST, OVERRIDE_WHITELIST_DETAIL,
            ADD_TITLE + OVERRIDE_WHITELIST);

        reset(settings);
    }

    public boolean getEstimateInheritors() {
        return estimateCheckBox.isSelected();
    }

    public boolean getExactSearch() {
        return exactSearchCheckBox.isSelected();
    }

    public List<String> getClassBlackList() {
        return modelToList(classBlackListModel);
    }

    public List<String> getOverrideBlackList() {
        return modelToList(overrideBlackListModel);
    }

    public List<String> getClassWhiteList() {
        return modelToList(classWhiteListModel);
    }

    public List<String> getOverrideWhiteList() {
        return modelToList(overrideWhiteListModel);
    }

    private void addListModel(final DefaultListModel<String> listModel,
                              final String label,
                              final String detail,
                              final String addTitle) {
        this.add(new SettingsListDecorator().decorate(
            new JList<>(listModel), listModel, label, detail, addTitle
        ));
    }

    public void reset(final ExceptionLocationFinderSettings exceptionLocationFinderSettings) {
        final Settings settings = exceptionLocationFinderSettings.getSettings();
        estimateCheckBox.setSelected(settings.estimateInheritors());
        exactSearchCheckBox.setSelected(settings.isExactSearch());
        resetListModel(classBlackListModel, settings.getClassBlackList());
        resetListModel(overrideBlackListModel, settings.getOverrideBlackList());
        resetListModel(classWhiteListModel, settings.getClassWhiteList());
        resetListModel(overrideWhiteListModel, settings.getOverrideWhiteList());
    }

    private void resetListModel(final DefaultListModel<String> modelToReset,
                                final List<String> settingsList) {
        modelToReset.removeAllElements();
        settingsList.forEach(modelToReset::addElement);
    }

    private List<String> modelToList(final DefaultListModel<String> listModel) {
        return range(0, listModel.size()).mapToObj(listModel::getElementAt)
                                         .collect(toList());
    }
}
