package com.thomas.checkMate.configuration.util;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class ListFilterUtil {

    private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();
    private static final Logger LOG = Logger.getInstance(ListFilterUtil.class);

    public enum FilterListType {
        WHITE_LIST,
        BLACK_LIST
    }

    public static boolean isAllowedInWhiteList(@NotNull final PsiMethod method,
                                               @NotNull final List<String> filterList) {
        return isAllowed(method, filterList, FilterListType.WHITE_LIST);
    }

    public static boolean isAllowedInBlackList(@NotNull final PsiMethod method,
                                               @NotNull final List<String> filterList) {
        return isAllowed(method, filterList, FilterListType.BLACK_LIST);
    }


    private static boolean isAllowed(@NotNull final PsiMethod method,
                                     @NotNull final List<String> filterList,
                                     @NotNull final FilterListType filterListType) {
        if (filterList.isEmpty()) {
            return true;
        }
        PsiClass psiClass = getParentOfType(method, PsiClass.class);
        if (psiClass == null) {
            LOG.warn("PSI class is null, cannot check filter list");
            return false;
        } else {
            boolean matches = matches(psiClass, filterList, filterListType);
            return (filterListType == FilterListType.WHITE_LIST && matches)
                || (filterListType == FilterListType.BLACK_LIST && !matches);
        }
    }

    private static boolean matches(@NotNull final PsiClass psiClass,
                                   @NotNull final List<String> filterList,
                                   @NotNull final FilterListType filterListType) {
        final String qualifiedName = psiClass.getQualifiedName();
        final boolean checkResult = qualifiedName != null && matches(qualifiedName, filterList);
        LOG.warn(
            "checking if " + qualifiedName + " matches " + filterListType + ' ' + filterList
                + ", result: " + checkResult
        );
        return checkResult;
    }

    private static boolean matches(@NotNull final String qualifiedName,
                                   @NotNull final List<String> whiteList) {
        return whiteList.stream()
                        .map(entry -> PATTERN_CACHE.computeIfAbsent(entry, Pattern::compile))
                        .map(pattern -> pattern.matcher(qualifiedName))
                        .anyMatch(Matcher::matches);

    }

}
