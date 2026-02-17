package love.yuzi.logger

import org.junit.Test
import timber.log.Timber

class WithTimberTestTest : WithTimberTest() {

    @Test
    fun test() {
        Timber.d("Test with Timber")
        assert(true)
    }
}