package com.phasmofriend.app.data.catalog

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Locale

/**
 * Public-read-only: all edits happen via the Firebase console or the one-time
 * seed step, never from the client. A one-time fetch (not a live listener) is
 * enough since content changes are rare/manual; Firestore's own disk cache
 * covers "was online once, now offline" on top of this.
 */
class FirestoreCatalogSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun fetch(): CatalogSnapshot? {
        return try {
            val locale = Locale.getDefault().language

            val behaviorDocs = firestore.collection(BEHAVIORS_COLLECTION).get().await()
                .documents.mapNotNull { it.toBehaviorDoc() }
            val behaviors = behaviorDocs.map { it.toBehavior(locale) }

            val ghostDocs = firestore.collection(GHOSTS_COLLECTION).get().await()
                .documents.mapNotNull { it.toGhostDoc() }

            if (ghostDocs.isEmpty()) null else ghostDocs.toCatalog(locale, behaviors)
        } catch (e: Exception) {
            null
        }
    }

    private fun DocumentSnapshot.toGhostDoc(): GhostDoc? =
        toObject(GhostDoc::class.java)?.copy(id = id)

    private fun DocumentSnapshot.toBehaviorDoc(): BehaviorDoc? =
        toObject(BehaviorDoc::class.java)?.copy(id = id)

    private companion object {
        const val GHOSTS_COLLECTION = "ghosts"
        const val BEHAVIORS_COLLECTION = "behaviors"
    }
}
