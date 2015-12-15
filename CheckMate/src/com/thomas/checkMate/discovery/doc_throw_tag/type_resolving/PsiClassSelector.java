package com.thomas.checkMate.discovery.doc_throw_tag.type_resolving;

import com.intellij.psi.PsiClass;
import com.intellij.psi.util.ClassUtil;

public class PsiClassSelector {
    public static PsiClass selectClassByPackage(String packageName, PsiClass[] eligibleClasses) {
        PsiClass closestMatch = null;
        int highestNrOfMatchingParts = Integer.MIN_VALUE;
        for (PsiClass psiClass : eligibleClasses) {
            String packagePart = ClassUtil.extractPackageName(psiClass.getQualifiedName());
            if (packageName.equals(packagePart)) {
                return psiClass;
            }
            int nrOfMatchingParts = nrOfMatchingParts(packageName, packagePart);
            if (nrOfMatchingParts > highestNrOfMatchingParts) {
                highestNrOfMatchingParts = nrOfMatchingParts;
                closestMatch = psiClass;
            }
        }
        return closestMatch;
    }

    private static int nrOfMatchingParts(String package1, String package2) {
        String[] separated = package2.split(".");
        StringBuilder sb = new StringBuilder(package2.length());
        int nrOfMatchingParts = 0;
        for (String part : separated) {
            sb.append(part);
            if (package1.startsWith(sb.toString()))
                nrOfMatchingParts++;
        }
        return nrOfMatchingParts;
    }
}
