package com.thomas.checkMate.presentation.exception_form;


import com.intellij.ide.util.MethodCellRenderer;
import com.thomas.checkMate.discovery.general.Discovery;

import javax.swing.*;
import java.awt.*;

public class ExceptionIndicatorCellRenderer implements ListCellRenderer<Discovery> {
    public MethodCellRenderer methodCellRenderer = new MethodCellRenderer(true);

    @Override
    public Component getListCellRendererComponent(JList<? extends Discovery> list, Discovery value, int index, boolean isSelected, boolean cellHasFocus) {
        return methodCellRenderer.getListCellRendererComponent(list, value.getEncapsulatingMethod(), index, isSelected, cellHasFocus);
    }
}
