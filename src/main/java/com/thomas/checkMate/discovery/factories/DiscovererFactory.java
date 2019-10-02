package com.thomas.checkMate.discovery.factories;

import static java.util.Collections.singletonList;

import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementDiscoverer;
import com.thomas.checkMate.discovery.throw_statement.ThrowStatementTypeResolver;
import java.util.List;

public class DiscovererFactory {

    public static List<ExceptionIndicatorDiscoverer> createSelectedDiscovers() {
        return singletonList(createThrowStatementDiscoverer());
    }

    private static ThrowStatementDiscoverer createThrowStatementDiscoverer() {
        ThrowStatementTypeResolver throwStatementTypeResolver = new ThrowStatementTypeResolver();
        return new ThrowStatementDiscoverer(throwStatementTypeResolver);
    }

}
