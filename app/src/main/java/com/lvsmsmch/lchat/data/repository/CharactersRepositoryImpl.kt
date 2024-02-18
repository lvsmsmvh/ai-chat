package com.lvsmsmch.lchat.data.repository

import com.lvsmsmch.lchat.domain.entities.Character
import com.lvsmsmch.lchat.domain.repositories.CharactersRepository
import com.lvsmsmch.lchat.utils.RemoteConfigHelper

class CharactersRepositoryImpl : CharactersRepository {

    private val all get() = RemoteConfigHelper.characters()

    override fun getAll(): Result<List<Character>> {
        return when (all.isEmpty()) {
            true -> Result.failure(Exception("Character list is empty."))
            false -> Result.success(all)
        }
    }

    override fun getById(id: Int): Result<Character> {
        all.find { it.id == id }?.let {
            return Result.success(it)
        }
        return Result.failure(Exception("Did not find character with id $id in the list of ${all.size} items."))
    }
}