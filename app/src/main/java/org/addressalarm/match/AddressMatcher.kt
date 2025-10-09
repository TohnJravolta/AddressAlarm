package org.addressalarm.match

import org.addressalarm.data.FlaggedPlace

/**
 * Picks the best matching place for the given on-screen text.
 * Scoring = token overlap between the screen text and the place address.
 */
object AddressMatcher {

    private fun normalize(s: String): String =
        s.uppercase()
            .replace(Regex("[^A-Z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

    private fun tokenOverlap(a: String, b: String): Int {
        val ta = normalize(a).split(' ').filter { it.isNotEmpty() }.toSet()
        val tb = normalize(b).split(' ').filter { it.isNotEmpty() }.toSet()
        if (ta.isEmpty() || tb.isEmpty()) return 0
        val common = ta.intersect(tb).size
        val denom  = maxOf(ta.size, tb.size)
        return (100 * common) / denom
    }

    fun check(screenText: String, places: List<FlaggedPlace>): MatchResult? {
        var bestPlace: FlaggedPlace? = null
        var bestScore = -1

        for (p in places) {
            // Compare against the place's RAW address (normalize here). Using rawAddress
            // avoids depending on a 'normAddress' property that may not exist.
            val score = tokenOverlap(screenText, p.rawAddress)
            if (score > bestScore) {
                bestScore = score
                bestPlace = p
            }
        }

        // reject weak matches to avoid noise (40–60 is a reasonable band; start at 50)
        if (bestScore < 50 || bestPlace == null) return null

        val tagsSummary =
            if (bestPlace.tags.isEmpty()) ""
            else bestPlace.tags.joinToString(", ")

        return MatchResult(
            place = bestPlace,
            score = bestScore,
            reason = "token-overlap",
            tagsSummary = tagsSummary
        )
    }
}

/** Result of matching screen text against a stored place. */
data class MatchResult(
    val place: FlaggedPlace,
    val score: Int,        // 0..100
    val reason: String,    // e.g., "token-overlap",
    val tagsSummary: String
)
