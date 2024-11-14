package cl.bootcamp.moviedbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cl.bootcamp.moviedbapp.ui.theme.MovieDBAppTheme
import cl.bootcamp.moviedbapp.view.MovieListScreen
import cl.bootcamp.moviedbapp.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val viewModel : MovieViewModel by viewModels()
        setContent {
            MovieDBAppTheme {
                MovieListScreen(viewModel)
            }
        }
    }
}
