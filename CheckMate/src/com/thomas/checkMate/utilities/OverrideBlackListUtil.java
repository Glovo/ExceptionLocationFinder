package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class OverrideBlackListUtil {
    private static final CheckMateSettings checkMateSettings = CheckMateSettings.getInstance();

    public static boolean isAllowed(PsiMethod method) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        return isAllowed(psiClass);
    }

    public static boolean isAllowed(PsiClass psiClass) {
        if (psiClass != null) {
            String qName = psiClass.getQualifiedName();
            if (qName != null) {
                if (!checkMateSettings.getOverrideBlackList().stream().anyMatch(qName::startsWith))
                    return true;
            }
        }
        return false;
    }
}
