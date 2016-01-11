package com.thomas.checkMate.utilities;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;

public class JavaLangUtil {
    public static boolean isJavaSource(PsiMethod method) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        String packageName = null;
        if (psiClass != null) {
            packageName = ClassUtil.extractPackageName(psiClass.getQualifiedName());
        }
        return packageName != null && packageName.startsWith("java");
    }
}
