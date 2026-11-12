package com.example.practicaapilibros.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicaapilibros.ui.screens.BookFormScreen
import com.example.practicaapilibros.ui.screens.BookFormViewModel.Companion.BOOK_ID_ARG
import com.example.practicaapilibros.ui.screens.BookFormViewModel.Companion.NO_BOOK_ID
import com.example.practicaapilibros.ui.screens.BookListScreen

enum class Screen(val route: String) {
    BookList("book_list"),
    BookForm("book_form?$BOOK_ID_ARG={$BOOK_ID_ARG}");

    companion object {
        fun bookFormRoute(bookId: Int? = null): String =
            if (bookId == null) "book_form" else "book_form?$BOOK_ID_ARG=$bookId"
    }
}

private const val SAVED_BOOK_ID_KEY = "saved_book_id"

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BookList.route,
        modifier = modifier
    ) {
        composable(Screen.BookList.route) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val savedBookId by savedStateHandle
                .getStateFlow(SAVED_BOOK_ID_KEY, NO_BOOK_ID)
                .collectAsStateWithLifecycle()

            BookListScreen(
                savedBookId = savedBookId.takeIf { it != NO_BOOK_ID },
                onSavedBookHandled = { savedStateHandle[SAVED_BOOK_ID_KEY] = NO_BOOK_ID },
                onAddClick = { navController.navigate(Screen.bookFormRoute()) },
                onBookClick = { id -> navController.navigate(Screen.bookFormRoute(id)) }
            )
        }
        composable(
            route = Screen.BookForm.route,
            arguments = listOf(
                navArgument(BOOK_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = NO_BOOK_ID
                }
            )
        ) {
            BookFormScreen(
                // Ignores repeated taps while the exit transition runs, so the list
                // screen is not popped as well.
                onBack = dropUnlessResumed { navController.popBackStack() },
                onBookSaved = { bookId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(SAVED_BOOK_ID_KEY, bookId)
                    navController.popBackStack()
                }
            )
        }
    }
}
