package com.glovoapp.plugins.ui;

import static com.intellij.psi.PsiType.getJavaLangThrowable;
import static com.intellij.psi.search.GlobalSearchScope.allScope;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

public final class ExceptionSelector {

    public static PsiType selectType(final Project project) {
        PsiClass throwableType = getThrowableType(project);
        TreeClassChooser exceptionClassChooser = getExceptionChooser(project, throwableType);
        exceptionClassChooser.showDialog();
        PsiClass exceptionClassToFind = exceptionClassChooser.getSelected();
        return exceptionClassToType(project, exceptionClassToFind);
    }

    private static PsiClass getThrowableType(final Project project) {
        return getJavaLangThrowable(
            PsiManager.getInstance(project),
            allScope(project)
        ).resolve();
    }

    @NotNull
    private static TreeClassChooser getExceptionChooser(final Project project,
                                                        final PsiClass throwableType) {
        TreeClassChooserFactory classChooserFactory = TreeClassChooserFactory.getInstance(project);
        return classChooserFactory.createInheritanceClassChooser(
            "Select class to search for",
            allScope(project),
            throwableType,
            throwableType
        );
    }

    @NotNull
    private static PsiClassType exceptionClassToType(final Project project,
                                                     final PsiClass exceptionClassToFind) {
        return JavaPsiFacade.getInstance(project)
                            .getElementFactory()
                            .createType(exceptionClassToFind);
    }

}
