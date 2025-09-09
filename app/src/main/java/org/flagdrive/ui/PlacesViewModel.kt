package org.flagdrive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.flagdrive.data.FlaggedPlace
import org.flagdrive.data.PlacesRepository

class PlacesViewModel(
    private val repo: PlacesRepository
) : ViewModel() {

    val places: StateFlow<List<FlaggedPlace>> =
        repo.getAllFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<FlaggedPlace>())

    fun addOrUpdate(
        rawAddress: String,
        name: String? = null,
        notes: String? = null,
        tags: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            val existing = repo.findByAddressExact(rawAddress)
            val toSave = (existing ?: FlaggedPlace(rawAddress = rawAddress))
                .copy(name = name, notes = notes, tags = tags)
            repo.upsert(toSave)
        }
    }

    fun remove(id: Long) {
        viewModelScope.launch { repo.delete(id) }
    }

    fun addSample() {
        viewModelScope.launch { repo.addSample() }
    }
}