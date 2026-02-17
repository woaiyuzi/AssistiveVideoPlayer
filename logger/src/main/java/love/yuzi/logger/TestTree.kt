package love.yuzi.logger

import timber.log.Timber

class TestTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("[${tag ?: "Test"}] $message")
        t?.printStackTrace()
    }
}