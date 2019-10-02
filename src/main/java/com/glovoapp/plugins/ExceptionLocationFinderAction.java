package com.glovoapp.plugins;

import static com.thomas.checkMate.discovery.factories.DiscovererFactory.createSelectedDiscovers;
import static java.util.stream.Collectors.toList;

import com.glovoapp.plugins.infrastructure.configuration.ExceptionLocationFinderSettings;
import com.glovoapp.plugins.ui.ExceptionSelector;
import com.glovoapp.plugins.ui.UsageTargetFromDiscovery;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.usages.TextChunk;
import com.intellij.usages.Usage;
import com.intellij.usages.UsagePresentation;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.UsageViewManager;
import com.intellij.usages.UsageViewPresentation;
import com.intellij.usages.impl.UsageAdapter;
import com.thomas.checkMate.configuration.util.FirstRunUtil;
import com.thomas.checkMate.discovery.ComputableExceptionFinder;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.MultipleMethodException;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public final class ExceptionLocationFinderAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ExceptionLocationFinderAction.class);


    private final ProgressManager progressManager = ProgressManager.getInstance();
    private final HintManager hintManager = HintManager.getInstance();
    private final ExceptionLocationFinderSettings settings = ExceptionLocationFinderSettings.getInstance();

    @Override
    public final void actionPerformed(final @NotNull AnActionEvent actionEvent) {
        PsiFile psiFile = actionEvent.getData(LangDataKeys.PSI_FILE);
        Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
        Project project = actionEvent.getProject();
        actionEvent.getPlace();
        if (psiFile == null || editor == null) {
            return;
        }
        //Prompt a settings dialog when this is the first run
        FirstRunUtil.promptSettings(settings, project);
        PsiType exceptionTypeToFind = ExceptionSelector.selectType(project);
        //Extract all selected method call expressions
        Caret currentCaret = editor.getCaretModel()
                                   .getCurrentCaret();
        PsiStatementExtractor statementExtractor = new PsiStatementExtractor(psiFile,
            currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
        PsiMethodCallExpressionExtractor methodCallExpressionExtractor = new PsiMethodCallExpressionExtractor(
            statementExtractor);
        Set<PsiCallExpression> psiMethodCalls;
        try {
            psiMethodCalls = methodCallExpressionExtractor.extract();
        } catch (MultipleMethodException mme) {
            hintManager.showErrorHint(editor, "Please keep your selection within one method");
            return;
        }
        if (psiMethodCalls.size() < 1) {
            hintManager.showErrorHint(editor, "No expressions found in current selection");
            return;
        } else if (psiMethodCalls.size() != 1) {
            hintManager.showErrorHint(editor, "Please select only a single expression");
            return;
        }

        //Get selected discoverers
        List<ExceptionIndicatorDiscoverer> discovererList = createSelectedDiscovers();
        //Find all uncaught unchecked exceptions in extracted method call expressions with the selected discoverers
        ComputableExceptionFinder exceptionFinder = new ComputableExceptionFinder(
            psiMethodCalls,
            discovererList
        );
        Map<PsiType, Set<Discovery>> discoveredExceptions;
        try {
            Map<PsiType, Set<Discovery>> unfilteredExceptions = progressManager.runProcessWithProgressSynchronously(
                exceptionFinder,
                "Searching For Locations of " + exceptionTypeToFind.getPresentableText(),
                true,
                project
            );
            discoveredExceptions = Maps.filterKeys(
                unfilteredExceptions,
                settings.exactSearch()
                    ? exceptionTypeToFind::equals
                    : exceptionTypeToFind::isAssignableFrom
            );

        } catch (StackOverflowError | OutOfMemoryError er) {
            hintManager.showErrorHint(editor,
                "Too many statements too process, consider adding more items to the override blacklist "
                    +
                    "or disabling the estimate overrides option");
            return;
        }
        if (discoveredExceptions.keySet()
                                .size() < 1) {
            showInformationHint(editor,
                "No given exceptions found in the selected statements");
            return;
        }

        UsageTarget[] usageTargets =
            UsageTargetFromDiscovery.get(discoveredExceptions.values()
                                                             .stream()
                                                             .flatMap(Collection::stream)
                                                             .collect(toList()));
        Usage[] usages = {new UsageAdapter() {
            @NotNull
            @Override
            public UsagePresentation getPresentation() {
                return new UsagePresentation() {
                    @NotNull
                    @Override
                    public TextChunk[] getText() {
                        return new TextChunk[]{new TextChunk(new TextAttributes(), "bla")};
                    }

                    @NotNull
                    @Override
                    public String getPlainText() {
                        return "blaaa";
                    }

                    @Override
                    public Icon getIcon() {
                        return null;
                    }

                    @Override
                    public String getTooltipText() {
                        return "bloo";
                    }
                };
            }

            @Override
            public FileEditorLocation getLocation() {
                return new FileEditorLocation() {
                    @NotNull
                    @Override
                    public FileEditor getEditor() {
                        return FileEditorManager.getInstance(project)
                                                .getSelectedEditor();
                    }

                    @Override
                    public int compareTo(@NotNull FileEditorLocation fileEditorLocation) {
                        return 0;
                    }
                };
            }
        }};
        UsageViewManager.getInstance(project)
                        .showUsages(
                            usageTargets,
                            usages,
                            new UsageViewPresentation() {
                                @Override
                                public String getTabText() {
                                    String sourceText = psiMethodCalls.stream()
                                                                      .findAny()
                                                                      .orElseThrow(
                                                                          RuntimeException::new
                                                                      )
                                                                      .getText();
                                    return "Locations of "
                                        + exceptionTypeToFind.getPresentableText()
                                        + " in " + sourceText;
                                }

                                @Override
                                public String getTabName() {
                                    return getTabText();
                                }

                                @Override
                                public String getToolwindowTitle() {
                                    return getTabText();
                                }
                            }
                        );

        //Navigate to the current file
        psiFile.navigate(true);
    }

    private void showInformationHint(Editor editor, String hint) {
        hintManager.showInformationHint(editor, hint);
    }

}
