package com.thomas.checkMate.ui;

import com.intellij.psi.PsiType;

import javax.swing.*;
import java.awt.*;

public class PsiTypeCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        if (value instanceof PsiType) {
            return super.getListCellRendererComponent(
                    list, ((PsiType) value).getCanonicalText(), index,
                    isSelected, hasFocus);
        }
        return super.getListCellRendererComponent(list, value, index,
                isSelected, hasFocus);
    }
}
