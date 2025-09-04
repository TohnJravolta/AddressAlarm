package org.flagdrive.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.flagdrive.data.AppDatabase
import org.flagdrive.data.FlaggedPlace

class PlacesViewModel(app: Application) : AndroidViewModel(app) {
    private val db by lazy { AppDatabase.get(app) }

    private val _places = MutableStateFlow<List<FlaggedPlace>>(emptyList())
    val places: StateFlow<List<FlaggedPlace>> = _places

    init { refresh() }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        // 👇 rename to match the DAO
        _places.value = db.places().getAll()
    }

    fun delete(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        db.places().deleteById(id)
        refresh()
    }

    fun addSample() = viewModelScope.launch(Dispatchers.IO) {
        val sample = FlaggedPlace(
            rawAddress = "420 Beef Street, Ohio",
            name = "Sample Diner",
            tags = listOf("diner", "test")
        )
        db.places().upsert(sample)
        refresh()
    }
}
