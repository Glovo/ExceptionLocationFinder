package com.thomas.checkMate.discovery.doc_throw_tag.type_resolving;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DocTagTypeResolver implements ExceptionTypeResolver<PsiDocTag> {
    private static final Logger logger = Logger.getLogger(DocTagTypeResolver.class.getName());
    private final PsiElementFactory elementFactory;
    private final PsiShortNamesCache shortNamesCache;
    private final JavaPsiFacade javaPsiFacade;
    private final GlobalSearchScope globalSearchScope;

    public DocTagTypeResolver(PsiElementFactory elementFactory, Project project) {
        this.elementFactory = elementFactory;
        this.shortNamesCache = PsiShortNamesCache.getInstance(project);
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
        this.globalSearchScope = GlobalSearchScope.allScope(project);
    }

    @Override
    public PsiType resolve(PsiDocTag psiDocTag) {
        PsiDocTagValue value = psiDocTag.getValueElement();
        if (value != null) {
            String exceptionName = value.getText();
            if (exceptionName != null) {
                PsiClass psiClass = javaPsiFacade.findClass(exceptionName, globalSearchScope);
                if (psiClass != null) {
                    return elementFactory.createType(psiClass);
                } else {
                    PsiClass[] classesByName = shortNamesCache.getClassesByName(exceptionName, globalSearchScope);
                    PsiClass encapsulatingClass = PsiTreeUtil.getParentOfType(psiDocTag, PsiClass.class);
                    if (encapsulatingClass != null && classesByName.length > 0) {
                        String packageName = ClassUtil.extractPackageName(encapsulatingClass.getQualifiedName());
                        PsiClass closestClass = PsiClassSelector.selectClassByPackage(packageName, classesByName);
                        return elementFactory.createType(closestClass);
                    }
                    logger.log(Level.WARNING, "No classes found with name: " + exceptionName);
                }
            }
        }
        return null;
    }
}
