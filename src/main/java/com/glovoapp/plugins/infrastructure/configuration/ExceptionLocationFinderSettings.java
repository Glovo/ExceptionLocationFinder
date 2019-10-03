package com.glovoapp.plugins.infrastructure.configuration;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

@State(
    name = ExceptionLocationFinderSettings.NAME,
    storages = {@Storage(value = "ExceptionLocationFinderSettings.xml")}
)
public class ExceptionLocationFinderSettings implements
    PersistentStateComponent<MutableSettings> {

    public static final String NAME = "ExceptionLocationFinderSettings";

    private static AtomicReference<ExceptionLocationFinderSettings> INSTANCE = new AtomicReference<>();

    private Settings settings;

    public ExceptionLocationFinderSettings() {
        this.settings = Settings.clean();
    }

    public static ExceptionLocationFinderSettings getInstance() {
        return INSTANCE.updateAndGet(currentInstance ->
            Optional.ofNullable(currentInstance)
                    .orElseGet(() ->
                        getApplication().getComponent(ExceptionLocationFinderSettings.class)
                    )
        );
    }

    @NotNull
    @Override
    public MutableSettings getState() {
        return settings.makeMutable();
    }

    @Override
    public void loadState(final @NotNull MutableSettings settings) {
        this.settings = settings.makeImmutable();
    }

    public final void set(final @NotNull Settings state) {
        this.settings = state;
    }

    public final Settings getSettings() {
        return settings;
    }
}
