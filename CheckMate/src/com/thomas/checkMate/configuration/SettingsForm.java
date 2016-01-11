package com.thomas.checkMate.configuration;

import javax.swing.*;

public class SettingsForm extends JPanel {
    private JCheckBox javaSourceCBox;
    private static final String INCLUDE_JAVA_SOURCES = "Include java.* sources while searching for unchecked exceptions";

    public SettingsForm(CheckMateSettings settings) {
        javaSourceCBox = new JCheckBox(INCLUDE_JAVA_SOURCES);
        this.add("IncludeJavaSources", javaSourceCBox);
        reset(settings);
    }

    public boolean getIncludeJavaSrc() {
        return javaSourceCBox.isSelected();
    }

    public void reset(CheckMateSettings settings) {
        javaSourceCBox.setSelected(settings.getIncludeJavaSrc());
    }
}
