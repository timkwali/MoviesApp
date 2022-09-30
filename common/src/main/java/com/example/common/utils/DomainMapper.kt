package com.example.common.utils

interface DomainMapper<DomainModel, Dto> {

    suspend fun mapToDomain(entity: DomainModel): Dto
}