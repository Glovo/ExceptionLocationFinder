//package com.thomas.checkMate;
//
//import com.intellij.openapi.ui.popup.JBPopup;
//import com.intellij.openapi.ui.popup.JBPopupFactory;
//import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
//import com.intellij.psi.PsiMethod;
//import com.intellij.psi.PsiType;
//
//import java.util.Map;
//import java.util.Set;
//
//public class GeneratePopup {
//    private final JBPopup popup;
//
//    public GeneratePopup(PsiMethod psiMethod) {
//        ThrowStatementVisitor throwStatementVisitor = new ThrowStatementVisitor(psiMethod);
//        Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions = throwStatementVisitor.getDiscoveredExceptions();
//        BaseListPopupStep step = new ThrowsStatementPopupStep("Throws statements", null new NavigatePopupStepListener());
//        popup = JBPopupFactory.getInstance().createListPopup(step);
//    }
//
//    public JBPopup getPopup() {
//        return popup;
//    }
//}
