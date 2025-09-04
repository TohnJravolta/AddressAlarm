package org.flagdrive.match;

/**
 * Picks the best matching place for the given on-screen text.
 * Scoring = token overlap between the screen text and the place address.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0002J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0002\u00a8\u0006\u0010"}, d2 = {"Lorg/flagdrive/match/AddressMatcher;", "", "()V", "check", "Lorg/flagdrive/match/MatchResult;", "screenText", "", "places", "", "Lorg/flagdrive/data/FlaggedPlace;", "normalize", "s", "tokenOverlap", "", "a", "b", "app_debug"})
public final class AddressMatcher {
    @org.jetbrains.annotations.NotNull()
    public static final org.flagdrive.match.AddressMatcher INSTANCE = null;
    
    private AddressMatcher() {
        super();
    }
    
    private final java.lang.String normalize(java.lang.String s) {
        return null;
    }
    
    private final int tokenOverlap(java.lang.String a, java.lang.String b) {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final org.flagdrive.match.MatchResult check(@org.jetbrains.annotations.NotNull()
    java.lang.String screenText, @org.jetbrains.annotations.NotNull()
    java.util.List<org.flagdrive.data.FlaggedPlace> places) {
        return null;
    }
}