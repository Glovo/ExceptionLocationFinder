package com.thomas.checkMate.discovery.general;

import static com.intellij.openapi.util.text.StringUtil.offsetToLineColumn;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.text.LineColumn;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;
import java.util.logging.Logger;

public abstract class ExceptionIndicatorDiscoverer<T extends PsiElement> {
    private static final Logger logger = Logger.getLogger(ExceptionIndicatorDiscoverer.class.getName());
    private ExceptionTypeResolver<T> exceptionTypeResolver;
    private Class<T> elementClass;

    public ExceptionIndicatorDiscoverer(final ExceptionTypeResolver<T> exceptionTypeResolver,
                                        final Class<T> clazz) {
        this.exceptionTypeResolver = exceptionTypeResolver;
        this.elementClass = clazz;
    }

    Discovery discover(T psiElement) {
        if (elementClass.isAssignableFrom(psiElement.getClass())) {
            if (isIndicator(psiElement)) {
                PsiType psiType = exceptionTypeResolver.resolve(psiElement);
                if (psiType != null) {
                    PsiFile psiFile = psiElement.getContainingFile();
                    final LineColumn lineColumn = offsetToLineColumn(
                        psiFile.getText(), psiElement.getTextOffset()
                    );
                    PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    String filePath = virtualFile.getPath();
                    Discovery discovery = new Discovery(
                        psiElement, psiType, lineColumn, psiFile, virtualFile, filePath
                    );
                    notifyProgress(psiType.getCanonicalText(), psiMethod);
                    return discovery;
                }
            }
        }
        return null;
    }

    protected abstract boolean isIndicator(T psiElement);

    private void notifyProgress(String exceptionName, PsiMethod encapsulatingMethod) {
        String exceptionFound = String.format("%s in %s", exceptionName, encapsulatingMethod.getName());
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        if (indicator != null) {
            indicator.setText2(exceptionFound);
        }
        logger.info(exceptionFound);
    }

}
