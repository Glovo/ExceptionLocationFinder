package com.thomas.checkMate.presentation;

import com.intellij.ide.util.MethodCellRenderer;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.ui.JBSplitter;
import com.thomas.checkMate.discovery.DiscoveredThrowStatement;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionForm {
    private JList<PsiType> exception_list;
    private JList<PsiMethod> method_list;
    private Map<PsiType, Set<DiscoveredThrowStatement>> exceptionMap;
    private JBSplitter splitter;

    public ExceptionForm(Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptionMap) {
        this.exceptionMap = discoveredExceptionMap;
        exception_list = createExceptionList(discoveredExceptionMap.keySet());
        method_list = createMethodList();
        LabeledComponent decoratedExceptionList = new DefaultListDecorator<PsiType>().decorate(exception_list, "Select exceptions to include");
        LabeledComponent decoratedMethodList = new DefaultListDecorator<PsiMethod>().decorate(method_list, "Inspect methods that throw this exception");
        splitter = createSplitter(decoratedExceptionList, decoratedMethodList);
    }

    private JList<PsiType> createExceptionList(Set<PsiType> exceptionTypes) {
        JList<PsiType> exceptionList = new JList<>();
        DefaultListModel<PsiType> listModel = new DefaultListModel<>();
        exceptionTypes.stream().forEach(listModel::addElement);
        exceptionList.setModel(listModel);
        exceptionList.addListSelectionListener(e -> {
            populateMethodListForSelectedExceptionWithIndex(exceptionList.getLeadSelectionIndex());
        });
        exceptionList.setCellRenderer(new PsiTypeCellRenderer());
        return exceptionList;
    }

    private JList<PsiMethod> createMethodList() {
        JList<PsiMethod> methodList = new JList<>();
        methodList.setCellRenderer(new MethodCellRenderer(true));
        return methodList;
    }

    private JBSplitter createSplitter(LabeledComponent exceptionList, LabeledComponent psiMethodList) {
        JBSplitter jbSplitter = new JBSplitter(false);
        jbSplitter.setFirstComponent(exceptionList);
        jbSplitter.setSecondComponent(psiMethodList);
        jbSplitter.setProportion(0.5f);
        return jbSplitter;
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

    public JBSplitter getSplitter() {
        return splitter;
    }

    public List<PsiType> getSelectedExceptionTypes() {
        return exception_list.getSelectedValuesList();
    }
}
