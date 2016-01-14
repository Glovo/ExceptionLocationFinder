package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class WhiteListUtil {
    private static final CheckMateSettings checkMateSettings = CheckMateSettings.getInstance();

    public static boolean isAllowed(PsiMethod method) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        return isAllowed(psiClass);
    }

    public static boolean isAllowed(PsiClass psiClass) {
        if (psiClass != null) {
            String qName = psiClass.getQualifiedName();
            if (qName != null) {
                if (!(qName.startsWith("java") || qName.startsWith("org.xml") || qName.startsWith("org.omg")))
                    return true;
                if (checkMateSettings.getSrcWhiteList().stream().anyMatch(qName::startsWith))
                    return true;
            }
        }
        return false;
    }
}
