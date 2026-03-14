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
    fun getFields(context: Context, userId: Int) = listOf(
        context.getString(R.string.login_email_test) to (context.getString(R.string.register_example_email_init_test) + "_$userId@"
                + context.getString(R.string.register_example_email_end_test)),
        context.getString(R.string.login_password_test) to (context.getString(R.string.register_example_password_test) + "_$userId")
    )
}

object FillRegisterRobot {
    fun getFields(context: Context, userId: Int) = listOf(
        context.getString(R.string.register_user_name_test) to (context.getString(R.string.register_example_user_name_test) + "_$userId"),
        context.getString(R.string.register_first_name_test) to (context.getString(R.string.register_example_first_name_test) + "_$userId"),
        context.getString(R.string.register_last_name_test) to (context.getString(R.string.register_example_last_name_test) + "_$userId"),
        context.getString(R.string.register_email_test) to (context.getString(R.string.register_example_email_init_test) + "_$userId@" +
                context.getString(R.string.register_example_email_end_test)),
        context.getString(R.string.register_password_test) to (context.getString(R.string.register_example_password_test) + "_$userId")
    )
}

object CheckHomeRobot {
    fun getFields(context: Context, businessId: String) = listOf(
        context.getString(R.string.home_movie_image_test) + "_$businessId",
        context.getString(R.string.home_movie_favourite_test) + "_$businessId",
        context.getString(R.string.home_movie_title_test) + "_$businessId"
    )
}

object CheckDetailRobot {
    fun getFields(context: Context, businessId: String) = listOf(
        context.getString(R.string.detail_movie_image_test) + "_$businessId",
        context.getString(R.string.detail_movie_title_test) + "_$businessId",
        context.getString(R.string.detail_movie_overview_test) + "_$businessId",
        context.getString(R.string.detail_movie_favourite_test) + "_$businessId"
    )
}