package org.flagdrive.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lorg/flagdrive/service/AlertManager;", "", "()V", "CHANNEL_ID", "", "ensureChannel", "", "ctx", "Landroid/content/Context;", "notifyMatch", "match", "Lorg/flagdrive/match/MatchResult;", "app_debug"})
public final class AlertManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "flagdrive_alerts";
    @org.jetbrains.annotations.NotNull()
    public static final org.flagdrive.service.AlertManager INSTANCE = null;
    
    private AlertManager() {
        super();
    }
    
    private final void ensureChannel(android.content.Context ctx) {
    }
    
    public final void notifyMatch(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx, @org.jetbrains.annotations.NotNull()
    org.flagdrive.match.MatchResult match) {
    }
}