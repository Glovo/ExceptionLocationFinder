package com.thomas.checkMate.presentation.exception_form;

import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.ui.JBSplitter;
import com.thomas.checkMate.discovery.general.Discovery;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionForm {
    private JList<PsiType> exception_list;
    private JList<Discovery> method_list;
    private Map<PsiType, Set<Discovery>> exceptionMap;
    private JBSplitter splitter;
    private PsiFile currentFile;
    private PsiElement currentActive;

    public ExceptionForm(Map<PsiType, Set<Discovery>> discoveredExceptionMap, PsiFile currentFile) {
        this.exceptionMap = discoveredExceptionMap;
        this.currentFile = currentFile;
        currentActive = currentFile;
        exception_list = createExceptionList(discoveredExceptionMap.keySet());
        method_list = createMethodList();
        LabeledComponent decoratedExceptionList = new DefaultListDecorator<PsiType>().decorate(exception_list, "Select exceptions to include");
        LabeledComponent decoratedMethodList = new DefaultListDecorator<Discovery>().decorate(method_list, "Inspect methods that throw this exception");
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
            activateIfNecessary(currentFile);
        });
        exceptionList.setCellRenderer(new PsiTypeCellRenderer());
        return exceptionList;
    }

    private JList<Discovery> createMethodList() {
        JList<Discovery> methodList = new JList<>();
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
            DefaultListModel<Discovery> methodListModel = new DefaultListModel<>();
            exceptionMap.get(psiType).forEach(methodListModel::addElement);
            method_list.setModel(methodListModel);
            method_list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Discovery indicator = method_list.getSelectedValue();
                    if (indicator != null) {
                        if (!e.isPopupTrigger()) {
                            if (!(e.getModifiersEx() == InputEvent.SHIFT_DOWN_MASK)) {
                                activateIfNecessary(indicator.getIndicator());
                            }
                        }
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

    private void activateIfNecessary(PsiElement element) {
        if (!currentActive.equals(element)) {
            NavigationUtil.activateFileWithPsiElement(element);
            currentActive = element;
        }
    }
}
