package com.denisdev.domain.rmcalculator

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.repository.AuthorRepository

class FakeAuthorRepository(private val value: Author = Author.Brzycki()): AuthorRepository {
    override suspend fun getAuthor(rmParams: RmParams, autoFx: Boolean, default: Author): Author {
        return value
    }
}