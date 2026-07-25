package com.phasmofriend.app.data.catalog

import android.content.Context
import com.phasmofriend.app.model.Behavior
import com.phasmofriend.app.model.Ghost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class CatalogStatus { LOADING, READY, USING_BUNDLED_FALLBACK }

data class CatalogState(
    val status: CatalogStatus,
    val ghosts: List<Ghost>,
    val behaviors: List<Behavior>
)

interface CatalogRepository {
    val state: StateFlow<CatalogState>
}

/**
 * Bundled data shows immediately so the app is usable offline from the first
 * frame; Firestore is fetched once in the background and replaces it if it
 * resolves. If Firestore fails or returns nothing, the bundled fallback stands.
 */
class CatalogRepositoryImpl(
    context: Context,
    externalScope: CoroutineScope,
    private val bundledSource: BundledCatalogSource = BundledCatalogSource(context),
    private val firestoreSource: FirestoreCatalogSource = FirestoreCatalogSource()
) : CatalogRepository {

    private val _state = MutableStateFlow(
        CatalogState(status = CatalogStatus.LOADING, ghosts = emptyList(), behaviors = emptyList())
    )
    override val state: StateFlow<CatalogState> = _state.asStateFlow()

    init {
        val bundled = bundledSource.load()
        _state.value = CatalogState(
            status = CatalogStatus.USING_BUNDLED_FALLBACK,
            ghosts = bundled.ghosts,
            behaviors = bundled.behaviors
        )

        externalScope.launch {
            val remote = withContext(Dispatchers.IO) { firestoreSource.fetch() }
            if (remote != null) {
                _state.value = CatalogState(
                    status = CatalogStatus.READY,
                    ghosts = remote.ghosts,
                    behaviors = remote.behaviors
                )
            }
        }
    }
}
