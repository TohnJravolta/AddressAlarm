package org.flagdrive.service

import org.flagdrive.data.FlaggedPlace

/** Result of a screen-text match against a stored place. */
data class MatchResult(
    val place: FlaggedPlace,
    val score: Int,          // 0..100
    val reason: String,      // e.g., "token-overlap"
    val tagsSummary: String  // comma-separated tags for UI/notification
)
