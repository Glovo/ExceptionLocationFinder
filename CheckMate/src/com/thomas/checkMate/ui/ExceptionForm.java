package com.thomas.checkMate.ui;

import com.intellij.ide.util.MethodCellRenderer;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ToolbarDecorator;
import com.thomas.checkMate.DiscoveredThrowStatement;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

public class ExceptionForm {
    private JList<PsiType> exception_list;
    private JPanel main_panel;
    private JList<PsiMethod> method_list;
    private Map<PsiType, Set<DiscoveredThrowStatement>> exceptionMap;
    private LabeledComponent decoratedExceptionList;
    private LabeledComponent decoratedMethodList;
    private JBSplitter splitter;

    public ExceptionForm(Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptionMap) {
        this.exceptionMap = discoveredExceptionMap;
        DefaultListModel<PsiType> listModel = new DefaultListModel<>();
        for (Map.Entry<PsiType, Set<DiscoveredThrowStatement>> entry : discoveredExceptionMap.entrySet()) {
            listModel.addElement(entry.getKey());
        }
        exception_list.setModel(listModel);
        exception_list.addListSelectionListener(e -> {
            populateMethodListForSelectedExceptionWithIndex(exception_list.getLeadSelectionIndex());
        });
        exception_list.setCellRenderer(new PsiTypeCellRenderer());
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(exception_list);
        toolbarDecorator.disableAddAction();
        toolbarDecorator.disableRemoveAction();
        toolbarDecorator.disableUpDownActions();
        JPanel decoratedPanel = toolbarDecorator.createPanel();
        decoratedExceptionList = LabeledComponent.create(decoratedPanel, "Exceptions to include in try catch statement");

        method_list.setCellRenderer(new MethodCellRenderer(true));
        toolbarDecorator = ToolbarDecorator.createDecorator(method_list);
        toolbarDecorator.disableAddAction();
        toolbarDecorator.disableRemoveAction();
        toolbarDecorator.disableUpDownActions();
        decoratedPanel = toolbarDecorator.createPanel();
        decoratedMethodList = LabeledComponent.create(decoratedPanel, "Methods that throw the selected exception");

        JBSplitter jbSplitter = new JBSplitter(false);
        jbSplitter.setFirstComponent(decoratedExceptionList);
        jbSplitter.setSecondComponent(decoratedMethodList);
        jbSplitter.setProportion(0.5f);
        splitter = jbSplitter;
    }

    private void populateMethodListForSelectedExceptionWithIndex(int index) {
        if (index >= 0) {
            PsiType psiType = exception_list.getModel().getElementAt(index);
            DefaultListModel<PsiMethod> methodListModel = new DefaultListModel<>();
            for (DiscoveredThrowStatement discoveredThrowStatement : exceptionMap.get(psiType)) {
                methodListModel.addElement(discoveredThrowStatement.getEncapsulatingMethod());
            }
            method_list.setModel(methodListModel);
        }
    }

    public JPanel getMailPanel() {
        return main_panel;
    }

    public LabeledComponent getDecoratedExceptionList() {
        return decoratedExceptionList;
    }

    public JBSplitter getSplitter() {
        return splitter;
    }
}
