package com.mango.task.ui.screens.authentication.enterPhoneNumber

import com.mango.task.R

data class Country(
    val code: String,
    val name: Int,
    val flagRes: Int
) {
    companion object {
        val placeholderCountry = Country(
            code = "",
            name = R.string.country_name_unknown,
            flagRes = R.drawable.ic_flag_unknown
        )
        val countryList = listOf(
            Country(
                code = "+7",
                name = R.string.country_name_russia,
                flagRes = R.drawable.ic_flag_russia
            ),
            Country(
                code = "+374",
                name = R.string.country_name_armenia,
                flagRes = R.drawable.ic_flag_armenia
            ),
            Country(
                code = "+995",
                name = R.string.country_name_georgia,
                flagRes = R.drawable.ic_flag_georgia
            ),
        )
    }
}