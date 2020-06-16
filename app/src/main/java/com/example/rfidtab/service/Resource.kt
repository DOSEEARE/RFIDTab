package com.timelysoft.kainarcourierapp.service


data class Resource<out T>(val status: Status, val data: T?, val msg: String?) {
    companion object {
        fun <T> success(data: T?, msg: String = ""): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String = "", data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }


        fun <T> netwrok(msg: String = "", data: T? = null): Resource<T> {
            return Resource(Status.NETWORK, data, msg)
        }

        fun <T> empty(msg: String = "", data: T? = null): Resource<T> {
            return Resource(Status.EMPTY, data, msg)
        }

    }
}

enum class Status {
    SUCCESS,
    ERROR,
    NETWORK,
    EMPTY
}
