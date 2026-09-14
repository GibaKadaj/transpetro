package com.prep.transpetro.data.db.dao

import androidx.room.*
import com.prep.transpetro.data.db.entity.Simulado
import kotlinx.coroutines.flow.Flow

@Dao
interface SimuladoDao {
    @Query("SELECT * FROM simulados ORDER BY date DESC")
    fun getAllFlow(): Flow<List<Simulado>>

    @Query("SELECT * FROM simulados ORDER BY date ASC")
    suspend fun getAllAsc(): List<Simulado>

    @Query("SELECT * FROM simulados WHERE isPlanned = 0 ORDER BY date DESC LIMIT 1")
    suspend fun getMostRecent(): Simulado?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(simulado: Simulado): Long

    @Update
    suspend fun update(simulado: Simulado)

    @Delete
    suspend fun delete(simulado: Simulado)

    @Query("SELECT * FROM simulados WHERE id = :id")
    suspend fun getById(id: Long): Simulado?
}
