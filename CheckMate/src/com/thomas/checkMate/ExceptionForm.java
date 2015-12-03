package com.thomas.checkMate;

import com.intellij.psi.PsiType;
import com.thomas.checkMate.ui.PsiTypeCellRenderer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.Set;

public class ExceptionForm {
    private JList<PsiType> exception_list;
    private JPanel main_panel;
    private JList<String> method_list;
    private Map<PsiType, Set<DiscoveredThrowStatement>> exceptionMap;

    public ExceptionForm(Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptionMap) {
        this.exceptionMap = discoveredExceptionMap;
        DefaultListModel<PsiType> listModel = new DefaultListModel<>();
        for (Map.Entry<PsiType, Set<DiscoveredThrowStatement>> entry : discoveredExceptionMap.entrySet()) {
            listModel.addElement(entry.getKey());
        }
        exception_list.setModel(listModel);
        exception_list.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int index = exception_list.locationToIndex(e.getPoint());
                PsiType psiType = exception_list.getModel().getElementAt(index);
                DefaultListModel<String> methodListModel = new DefaultListModel<>();
                for (DiscoveredThrowStatement discoveredThrowStatement : exceptionMap.get(psiType)) {
                    methodListModel.addElement(discoveredThrowStatement.getEncapsulatingMethod().getName());
                }
                method_list.setModel(methodListModel);
            }
        });
        exception_list.setCellRenderer(new PsiTypeCellRenderer());
    }

    public JPanel getMailPanel() {
        return main_panel;
    }
}
