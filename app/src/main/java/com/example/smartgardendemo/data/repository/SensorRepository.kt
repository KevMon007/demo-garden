package com.example.smartgardendemo.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.smartgardendemo.core.constants.FirebasePaths
import com.example.smartgardendemo.data.model.SensorData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

open class SensorRepository {

    private val db by lazy { FirebaseDatabase.getInstance() }

    // ── LECTURA en tiempo real ────────────────────────────────────────────
    // Retorna un Flow que emite cada vez que Firebase actualiza /humedad/datos
    open fun observarDatos(): Flow<SensorData> = callbackFlow {

        val ref = db.getReference(FirebasePaths.DATOS)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // Mapear snapshot a nuestro modelo
                val data = SensorData(
                    sensorHumedad = snapshot.child("sensor_humedad")
                        .getValue(Int::class.java) ?: 0,
                    relayEstado   = snapshot.child("relay_estado")
                        .getValue(Int::class.java) ?: 0,
                    relayManualFb = snapshot.child("relay_manual_fb")
                        .getValue(Int::class.java) ?: 0
                )
                trySend(data)  // Emitir el nuevo valor al Flow
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())  // Cerrar el Flow con error
            }
        }

        // Suscribirse a cambios en tiempo real
        ref.addValueEventListener(listener)

        // Cuando el Flow se cancela, remover el listener para evitar fugas
        awaitClose { ref.removeEventListener(listener) }
    }

    // ── ESCRITURA — control manual del relé desde la app ─────────────────
    open fun setRelayManual(valor: Int, onResult: (Boolean) -> Unit) {
        db.getReference(FirebasePaths.RELAY_MANUAL)
            .setValue(valor)
            .addOnSuccessListener { onResult(true)  }
            .addOnFailureListener { onResult(false) }
    }
}