package com.arsartificia.dev.baristoteles

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class Entry(val name: String, val grind: String, val time: String, val weight: String, val outWeight: String, val note: String, val rating: Float, val temperature: String) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -4264550368558631299L
    }

    private fun writeObject(oos: ObjectOutputStream) {
        return oos.defaultWriteObject()
    }

    private fun readObject(ois: ObjectInputStream) {
        return ois.defaultReadObject()
    }

    private fun readObjectNoData() {
        return
    }
}