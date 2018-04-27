package uk.co.wedgetech.dagger2experiment

object Incrementor {
    private var next_ = 0

    val next = synchronized(this) { ++next_ }
}