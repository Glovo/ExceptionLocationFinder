package com.glovoapp.plugins.infrastructure.configuration;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

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
final class ExceptionLocationFinderSettings implements PersistentStateComponent<MutableSettings> {

    static final String NAME = Settings.NAME;

    private static AtomicReference<ExceptionLocationFinderSettings> INSTANCE = new AtomicReference<>();

    private final AtomicReference<Settings> settings = new AtomicReference<>(Settings.clean());

    public ExceptionLocationFinderSettings() {
        settings.set(Settings.clean());
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
        return settings.get()
                       .makeMutable();
    }

    @Override
    public void loadState(final @NotNull MutableSettings settings) {
        this.settings.set(settings.makeImmutable());
    }

    final void setSettings(final @NotNull Settings settings) {
        this.settings.set(settings);
    }

    final Settings getSettings() {
        return settings.get();
    }

}
