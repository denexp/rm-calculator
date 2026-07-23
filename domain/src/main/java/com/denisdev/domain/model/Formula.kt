package com.denisdev.domain.model

interface Formula<T> {
    fun calculate(): T
}