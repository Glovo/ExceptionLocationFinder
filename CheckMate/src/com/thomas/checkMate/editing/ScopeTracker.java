package com.thomas.checkMate.editing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;

public class ScopeTracker<T extends PsiElement> {
    private Class classToTrack;

    public ScopeTracker(Class classToTrack) {
        this.classToTrack = classToTrack;
    }

    @SuppressWarnings("unchecked")
    public boolean foundInScopes(T psiElement, Collection<T> scopes) {
        return scopes.stream()
                .map(mce -> PsiTreeUtil.findChildrenOfType(mce, classToTrack))
                .flatMap(e -> ((Collection<T>) e).stream())
                .anyMatch(c -> c.equals(psiElement));
    }

    @SuppressWarnings("unchecked")
    public void removeSmallerScopes(T psiElement, Collection<T> scopes) {
        Collection<T> children = PsiTreeUtil.findChildrenOfType(psiElement, classToTrack);
        scopes.removeAll(children);
    }
}
