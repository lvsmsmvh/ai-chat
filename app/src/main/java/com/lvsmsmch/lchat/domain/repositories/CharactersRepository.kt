package com.lvsmsmch.lchat.domain.repositories

import com.lvsmsmch.lchat.domain.entities.Character

interface CharactersRepository {
    fun getAll(): Result<List<Character>>
    fun getById(id: Int): Result<Character>
}