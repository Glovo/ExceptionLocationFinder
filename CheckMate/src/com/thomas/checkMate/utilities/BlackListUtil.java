package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

public class BlackListUtil {
    public static boolean isAllowed(PsiMethod method, List<String> blackList) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        return isAllowed(psiClass, blackList);
    }

    public static boolean isAllowed(PsiClass psiClass, List<String> blackList) {
        if (psiClass != null) {
            String qName = psiClass.getQualifiedName();
            return isAllowed(qName, blackList);
        }
        return false;
    }

    public static boolean isAllowed(PsiType type, List<String> blackList) {
        return isAllowed(type.getCanonicalText(), blackList);
    }

    public static boolean isAllowed(String fqn, List<String> blackList) {
        if (fqn != null) {
            if (!blackList.stream().anyMatch(fqn::startsWith))
                return true;
        }
        return false;
    }
}
