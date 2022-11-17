package com.wakeup.domain.model

data class Picture(
    val bitmap: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Picture

        if (!bitmap.contentEquals(other.bitmap)) return false

        return true
    }

    override fun hashCode(): Int {
        return bitmap.contentHashCode()
    }
}