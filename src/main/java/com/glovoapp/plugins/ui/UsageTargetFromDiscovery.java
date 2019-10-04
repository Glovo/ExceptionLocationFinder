package com.glovoapp.plugins.ui;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usages.UsageTarget;
import com.thomas.checkMate.discovery.general.Discovery;
import java.util.List;
import java.util.Optional;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UsageTargetFromDiscovery {

    private static final Logger log = Logger.getInstance(UsageTargetFromDiscovery.class);

    public static UsageTarget[] get(final List<Discovery> discoveries) {
        final UsageTarget[] result = new UsageTarget[discoveries.size()];

        for (int i = 0; i < discoveries.size(); ++i) {
            result[i] = get(discoveries.get(i));
        }

        return result;
    }

    private static UsageTarget get(final Discovery discovery) {
        return new UsageTarget() {
            @Override
            public void findUsages() {

            }

            @Override
            public void findUsagesInEditor(@NotNull FileEditor fileEditor) {

            }

            @Override
            public void highlightUsages(@NotNull PsiFile psiFile, @NotNull Editor editor,
                                        boolean b) {

            }

            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public VirtualFile[] getFiles() {
                return new VirtualFile[]{discovery.getFile()};
            }

            @Override
            public void update() {

            }

            @Override
            public String getName() {
                return discovery.getLocation();
            }

            @Override
            public ItemPresentation getPresentation() {
                return new ItemPresentation() {
                    @Override
                    public String getPresentableText() {
                        return discovery.getLocation();
                    }

                    @Override
                    public String getLocationString() {
                        return discovery.getLocation();
                    }

                    @Nullable
                    @Override
                    public Icon getIcon(boolean b) {
                        return null;
                    }
                };
            }

            @Override
            public void navigate(boolean b) {
                Optional<Navigatable> navigable = Optional.of(discovery)
                                                          .map(Discovery::getElement)
                                                          .map(PsiElement::getNavigationElement)
                                                          .filter(it -> it instanceof Navigatable)
                                                          .map(it -> (Navigatable) it)
                                                          .filter(Navigatable::canNavigate);

                if (navigable.isPresent()) {
                    log.info("navigating to " + discovery.getLocation());
                    navigable.get()
                             .navigate(b);
                } else {
                    log.warn("cannot navigate to " + discovery.getLocation());
                }
            }

            @Override
            public boolean canNavigate() {
                return true;
            }

            @Override
            public boolean canNavigateToSource() {
                return true;
            }
        };
    }

}
