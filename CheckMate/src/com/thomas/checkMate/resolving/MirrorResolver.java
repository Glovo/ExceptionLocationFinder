package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.compiled.ClsMethodImpl;

public class MirrorResolver {

    public static PsiMethod resolve(PsiMethod method) {
        if (method instanceof ClsMethodImpl) {
            PsiMethod sourceMirror = ((ClsMethodImpl) method).getSourceMirrorMethod();
            if (sourceMirror != null) {
                return sourceMirror;
            } else {
                PsiMethod mirror = (PsiMethod) ((ClsMethodImpl) method).getMirror();
                if (mirror != null) {
                    return mirror;
                }
            }
        }
        return null;
    }
}
