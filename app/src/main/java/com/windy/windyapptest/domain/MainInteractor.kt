package com.windy.windyapptest.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MainInteractor {

    suspend fun getFlowsByCount(count: Int): List<Flow<Int>> {
        return withContext(Dispatchers.IO) {
            val flows = mutableListOf<Flow<Int>>()
            for (index in 0 until count) {
                val flow = flow {
                    delay(BASE_DELAY * (index + 1))
                    emit(index + 1)
                }.flowOn(Dispatchers.IO)
                flows.add(flow)
            }
            flows
        }
    }

    companion object {
        private const val BASE_DELAY = 100L
    }
}