package com.thomas.checkMate.discovery.factories;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.thomas.checkMate.discovery.doc_throw_tag.DocTagDiscoverer;
import com.thomas.checkMate.discovery.doc_throw_tag.type_resolving.DocTagTypeResolver;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementTypeResolver;

import java.util.ArrayList;
import java.util.List;

public class DiscovererFactory {
    public static ThrowStatementDiscoverer createThrowStatementDiscoverer() {
        ThrowStatementTypeResolver throwStatementTypeResolver = new ThrowStatementTypeResolver();
        return new ThrowStatementDiscoverer(throwStatementTypeResolver);
    }

    public static DocTagDiscoverer createDocTagDiscoverer(Project project) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        DocTagTypeResolver typeResolver = new DocTagTypeResolver(elementFactory, project);
        return new DocTagDiscoverer(typeResolver);
    }

    public static List<ExceptionIndicatorDiscoverer> createAllDiscoverers(Project project) {
        List<ExceptionIndicatorDiscoverer> discoverers = new ArrayList<>();
        discoverers.add(createThrowStatementDiscoverer());
        discoverers.add(createDocTagDiscoverer(project));
        return discoverers;
    }
}
