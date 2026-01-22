package com.juanje.themoviesapp.utils

import android.content.Context
import com.juanje.themoviesapp.R

object CheckLoginRobot {
    fun getFields(context: Context) = listOf(
        context.getString(R.string.login_email_test),
        context.getString(R.string.login_password_test),
        context.getString(R.string.login_login_test),
        context.getString(R.string.login_register_test)
    )
}

object CheckRegisterRobot {
    fun getFields(context: Context) = listOf(
        context.getString(R.string.register_user_name_test),
        context.getString(R.string.register_first_name_test),
        context.getString(R.string.register_last_name_test),
        context.getString(R.string.register_email_test),
        context.getString(R.string.register_password_test)
    )
}

object FillLoginRobot {
    fun getFields(context: Context, id: Int) = listOf(
        context.getString(R.string.login_email_test) to (context.getString(R.string.register_example_email_init_test) + "_$id@"
                + context.getString(R.string.register_example_email_end_test)),
        context.getString(R.string.login_password_test) to (context.getString(R.string.register_example_password_test) + "_$id")
    )
}

object FillRegisterRobot {
    fun getFields(context: Context, id: Int) = listOf(
        context.getString(R.string.register_user_name_test) to (context.getString(R.string.register_example_user_name_test) + "_$id"),
        context.getString(R.string.register_first_name_test) to (context.getString(R.string.register_example_first_name_test) + "_$id"),
        context.getString(R.string.register_last_name_test) to (context.getString(R.string.register_example_last_name_test) + "_$id"),
        context.getString(R.string.register_email_test) to (context.getString(R.string.register_example_email_init_test) + "_$id@" +
                context.getString(R.string.register_example_email_end_test)),
        context.getString(R.string.register_password_test) to (context.getString(R.string.register_example_password_test) + "_$id")
    )
}

object CheckHomeRobot {
    fun getFields(context: Context, movieId: Int) = listOf(
        context.getString(R.string.home_movie_image_test) + "_$movieId",
        context.getString(R.string.home_movie_favourite_test) + "_$movieId",
        context.getString(R.string.home_movie_title_test) + "_$movieId"
    )
}

object CheckDetailRobot {
    fun getFields(context: Context, movieId: Int) = listOf(
        context.getString(R.string.detail_movie_image_test) + "_$movieId",
        context.getString(R.string.detail_movie_title_test) + "_$movieId",
        context.getString(R.string.detail_movie_overview_test) + "_$movieId",
        context.getString(R.string.detail_movie_favourite_test) + "_$movieId"
    )
}