package com.example.cryptos.data.network.entities.mappers

interface FromEntityToDomainMapper<Entity, Domain> {

    fun mapToDomain(entity: Entity?): Domain

}