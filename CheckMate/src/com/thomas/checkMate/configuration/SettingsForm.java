package com.thomas.checkMate.configuration;

import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;

public class SettingsForm extends JPanel {
    private JCheckBox javaSourceCBox;
    private static final String INCLUDE_JAVA_SOURCES = "Search through java.* sources for unchecked exceptions (Not Recommended)";
    private JCheckBox javaDocsCBox;
    private static final String INCLUDE_JAVA_DOCS = "Include unchecked exceptions found in JavaDocs (Recommended)";

    public SettingsForm(CheckMateSettings settings) {
        this.setLayout(new VerticalLayout());
        javaSourceCBox = new JCheckBox(INCLUDE_JAVA_SOURCES);
        this.add("IncludeJavaSources", javaSourceCBox);
        javaDocsCBox = new JCheckBox(INCLUDE_JAVA_DOCS);
        this.add("IncludeJavaDocs", javaDocsCBox);
        reset(settings);
    }

    public boolean getIncludeJavaSrc() {
        return javaSourceCBox.isSelected();
    }

    public boolean getIncludeJavaDocs() {
        return javaDocsCBox.isSelected();
    }

    public void reset(CheckMateSettings settings) {
        javaSourceCBox.setSelected(settings.getIncludeJavaSrc());
        javaDocsCBox.setSelected(settings.getIncludeJavaDocs());
    }
}
