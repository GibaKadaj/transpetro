package com.prep.transpetro.data.repository

import com.prep.transpetro.data.db.dao.SimuladoDao
import com.prep.transpetro.data.db.entity.Simulado
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimuladoRepository @Inject constructor(
    private val dao: SimuladoDao
) {
    fun getAllFlow(): Flow<List<Simulado>> = dao.getAllFlow()
    suspend fun getAllAsc() = dao.getAllAsc()
    suspend fun getMostRecent() = dao.getMostRecent()
    suspend fun insert(simulado: Simulado) = dao.insert(simulado)
    suspend fun update(simulado: Simulado) = dao.update(simulado)
    suspend fun delete(simulado: Simulado) = dao.delete(simulado)
    suspend fun getById(id: Long) = dao.getById(id)
}
