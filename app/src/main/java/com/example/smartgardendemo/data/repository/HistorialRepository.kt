package com.example.smartgardendemo.data.repository

import android.util.Log
import com.example.smartgardendemo.core.constants.FirebasePaths
import com.example.smartgardendemo.data.model.HistorialEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

open class HistorialRepository {

    private val db by lazy { FirebaseDatabase.getInstance() }

    open fun guardarEntrada(entry: HistorialEntry) {
        val ref = db.getReference(FirebasePaths.HISTORIAL).child(entry.timestamp.toString())
        ref.setValue(entry)
            .addOnSuccessListener { Log.d(TAG, "Entrada guardada: ${entry.timestamp}") }
            .addOnFailureListener { e -> Log.e(TAG, "Error al guardar entrada", e) }
    }

    open fun observarHistorial(): Flow<List<HistorialEntry>> = callbackFlow {
        val ref = db.getReference(FirebasePaths.HISTORIAL)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<HistorialEntry>()
                for (child in snapshot.children) {
                    val entry = child.getValue(HistorialEntry::class.java)
                    if (entry != null) {
                        lista.add(entry.copy(id = child.key ?: ""))
                    }
                }
                val ordenada = lista
                    .sortedByDescending { it.timestamp }
                    .take(50)
                trySend(ordenada)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    companion object {
        private const val TAG = "HistorialRepo"
    }
}
