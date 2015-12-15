package com.thomas.checkMate.discovery.doc_throw_tag.type_resolving;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;

public class DocTagTypeResolver implements ExceptionTypeResolver<PsiDocTag> {
    private final PsiElementFactory elementFactory;
    private final Project project;

    public DocTagTypeResolver(PsiElementFactory elementFactory, Project project) {
        this.elementFactory = elementFactory;
        this.project = project;
    }

    @Override
    public PsiType resolve(PsiDocTag psiDocTag) {
        PsiDocTagValue value = psiDocTag.getValueElement();
        if (value != null) {
            String exceptionName = value.getText();
            if (exceptionName != null) {
                PsiClass[] classesByName = PsiShortNamesCache.getInstance(project).getClassesByName(exceptionName, GlobalSearchScope.allScope(project));
                PsiClass encapsulatingClass = PsiTreeUtil.getParentOfType(psiDocTag, PsiClass.class);
                if (encapsulatingClass != null) {
                    String packageName = ClassUtil.extractPackageName(encapsulatingClass.getQualifiedName());
                    PsiClass closestClass = PsiClassSelector.selectClassByPackage(packageName, classesByName);
                    return elementFactory.createType(closestClass);
                }
            }
        }
        return null;
    }
}
