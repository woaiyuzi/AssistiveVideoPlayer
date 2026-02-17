package love.yuzi.logger

import org.junit.After
import org.junit.Before
import timber.log.Timber

open class WithTimberTest {
    @Before
    fun setupWithTimber() {
        Timber.plant(TestTree())
    }

    @After
    fun tearDownWithTimber() {
        Timber.uprootAll()
    }
}