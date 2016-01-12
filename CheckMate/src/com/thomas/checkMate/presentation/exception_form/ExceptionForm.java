package com.thomas.checkMate.presentation.exception_form;

import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.ui.JBSplitter;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionForm {
    private JList<PsiType> exception_list;
    private JList<DiscoveredExceptionIndicator> method_list;
    private Map<PsiType, Set<DiscoveredExceptionIndicator>> exceptionMap;
    private JBSplitter splitter;
    private PsiFile currentFile;

    public ExceptionForm(Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptionMap, PsiFile currentFile) {
        this.exceptionMap = discoveredExceptionMap;
        this.currentFile = currentFile;
        exception_list = createExceptionList(discoveredExceptionMap.keySet());
        method_list = createMethodList();
        LabeledComponent decoratedExceptionList = new DefaultListDecorator<PsiType>().decorate(exception_list, "Select exceptions to include");
        LabeledComponent decoratedMethodList = new DefaultListDecorator<DiscoveredExceptionIndicator>().decorate(method_list, "Inspect methods that throw this exception");
        exception_list.setSelectedIndex(0);
        splitter = createSplitter(decoratedExceptionList, decoratedMethodList);
    }

    private JList<PsiType> createExceptionList(Set<PsiType> exceptionTypes) {
        JList<PsiType> exceptionList = new JList<>();
        DefaultListModel<PsiType> listModel = new DefaultListModel<>();
        exceptionTypes.stream().forEach(listModel::addElement);
        exceptionList.setModel(listModel);
        exceptionList.addListSelectionListener(e -> {
            populateMethodListForSelectedExceptionWithIndex(exceptionList.getLeadSelectionIndex());
            currentFile.navigate(false);
        });
        exceptionList.setCellRenderer(new PsiTypeCellRenderer());
        return exceptionList;
    }

    private JList<DiscoveredExceptionIndicator> createMethodList() {
        JList<DiscoveredExceptionIndicator> methodList = new JList<>();
        methodList.setCellRenderer(new ExceptionIndicatorCellRenderer());
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
            DefaultListModel<DiscoveredExceptionIndicator> methodListModel = new DefaultListModel<>();
            exceptionMap.get(psiType).forEach(methodListModel::addElement);
            method_list.setModel(methodListModel);
            method_list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DiscoveredExceptionIndicator indicator = method_list.getSelectedValue();
                    if (indicator != null) {
                        NavigationUtil.activateFileWithPsiElement(indicator.getIndicator());
                    }
                }
            });
        }
    }

    public JBSplitter getSplitter() {
        return splitter;
    }

    public List<PsiType> getSelectedExceptionTypes() {
        return exception_list.getSelectedValuesList();
    }
}
