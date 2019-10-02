package com.thomas.checkMate.discovery.general;

import com.intellij.openapi.util.text.LineColumn;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import java.util.Objects;

public final class Discovery {

    private final PsiElement element;
    private final PsiType exceptionType;
    private final LineColumn lineColumn;
    private final PsiFile psiFile;
    private final VirtualFile file;
    private final String filePath;

    Discovery(final PsiElement element,
              final PsiType exceptionType,
              final LineColumn lineColumn,
              final PsiFile psiFile,
              final VirtualFile file,
              final String filePath) {
        this.element = element;
        this.exceptionType = exceptionType;
        this.lineColumn = lineColumn;
        this.psiFile = psiFile;
        this.file = file;
        this.filePath = filePath;
    }

    public final PsiElement getElement() {
        return element;
    }

    public final PsiType getExceptionType() {
        return exceptionType;
    }

    public final LineColumn getLineColumn() {
        return lineColumn;
    }

    public final PsiFile getPsiFile() {
        return psiFile;
    }

    public final VirtualFile getFile() {
        return file;
    }

    public final String getFilePath() {
        return filePath;
    }

    public final String getLocation() {
        return filePath + ":" + (lineColumn.line + 1);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Discovery otherDiscovery = (Discovery) other;
        return Objects.equals(exceptionType, otherDiscovery.exceptionType)
            && Objects.equals(lineColumn, otherDiscovery.lineColumn)
            && Objects.equals(file, otherDiscovery.file)
            && Objects.equals(filePath, otherDiscovery.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionType, lineColumn, filePath);
    }
}
