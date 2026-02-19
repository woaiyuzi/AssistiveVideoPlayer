package love.yuzi.avp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import love.yuzi.avp.home.HomeScreen
import love.yuzi.avp.preference.PreferenceRepository
import love.yuzi.avp.ui.nav.BottomSheetSceneStrategy
import love.yuzi.avp.ui.nav.HomeNavKey
import love.yuzi.avp.ui.nav.VideoManagerNavKey
import love.yuzi.avp.ui.theme.AssistiveVideoPlayerTheme
import love.yuzi.avp.videomanager.VideoManagerScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val backStack = rememberNavBackStack(HomeNavKey)
            val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

            AssistiveVideoPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        sceneStrategy = bottomSheetStrategy,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            EntryProvider(
                                backStack = backStack
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun EntryProviderScope<NavKey>.EntryProvider(
        backStack: NavBackStack<NavKey>
    ) {
        entry<HomeNavKey> {
            val context = LocalContext.current
            HomeScreen(
                initialVideoId = preferenceRepository.lastPlayedVideoId,
                onBack = {
                    moveTaskToBack(true)
                },
                onStop = { video ->
                    if (video != null) {
                        preferenceRepository.setLastPlayedVideoId(video.id)
                    }
                },
                onRequestSwitchVideo = {
                    Toast.makeText(
                        context,
                        "Not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onRequestProgramDetail = {
                    Toast.makeText(
                        context,
                        "Not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onRequestVideoManager = { backStack.add(VideoManagerNavKey) },
            )
        }

        entry(VideoManagerNavKey) {
            VideoManagerScreen(
                onBack = {
                    backStack.removeLastOrNull()
                }
            )
        }
    }
}