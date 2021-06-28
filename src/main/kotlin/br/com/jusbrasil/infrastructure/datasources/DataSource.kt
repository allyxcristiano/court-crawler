package br.com.jusbrasil.infrastructure.datasources

interface DataSource<T> {

    fun getDatabase(): T
}
