package love.yuzi.logger

import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class TestTreeTest {
    @Before
    fun setup() {
        Timber.plant(TestTree())
    }

    @Test
    fun log() {
        Timber.d("Hello World")
        assert(true)
    }

    @After
    fun tearDown() {
        Timber.uprootAll()
    }
}