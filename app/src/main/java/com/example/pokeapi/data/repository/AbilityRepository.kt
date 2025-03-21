package com.example.pokeapi.data.repository

import com.example.pokeapi.data.model.Resource
import com.example.pokeapi.data.local.dao.AbilityDao
import com.example.pokeapi.data.local.entities.AbilityData
import com.example.pokeapi.data.remote.api.PokeApiService
import com.example.pokeapi.model.AbilityDetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AbilityRepository @Inject constructor(
    private val api: PokeApiService,
    private val abilityDao: AbilityDao
) {
    fun getAllItem(): Flow<Resource<List<AbilityData>>> = flow{
        emit(Resource.Loading())
        try {
            val response = api.getAbility()
            if(response.isSuccessful){
                response.body()?.let { abilityResponse->
                    abilityDao.insertAll(abilityResponse.results)
                }
            }
            emit(Resource.Success(abilityDao.getAll().first()))
        }catch (e: Exception){
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    //suspend fun deleteAllPokeData(allAbilityData: List<AbilityData>) = abilityDao.deleteAllItemData(allAbilityData)

    suspend fun getAbilityDetail(name: String): AbilityDetailResponse?{
        val response =  api.getAbilityDetail(name)
        if(response.isSuccessful){
            return response.body()
        }
        return null
    }
}