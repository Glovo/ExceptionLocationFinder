package com.thomas.checkMate.configuration.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BlackListUtil {
    private static final Map<String, Pattern> patternCache = new HashMap<>();

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
        return fqn != null && !blackList.stream().anyMatch(bl -> {
            Pattern pattern = patternCache.get(bl);
            if (pattern == null) {
                pattern = Pattern.compile(bl);
                patternCache.put(bl, pattern);
            }
            return pattern.matcher(fqn).matches();
        });
    }
}
