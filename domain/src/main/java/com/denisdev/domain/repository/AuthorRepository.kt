package com.denisdev.domain.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams

interface AuthorRepository {
    suspend fun getAuthor(rmParams: RmParams, autoFx: Boolean, default: Author): Author
}