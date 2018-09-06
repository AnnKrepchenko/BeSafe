package com.krepchenko.besafe.utils

interface CustomConsumer<T> {
    /**
     * Consume the given value.
     *
     * @param t the value
     * @throws Exception on error
     */
    fun accept(t: T)
}
