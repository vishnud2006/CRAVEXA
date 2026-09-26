package com.cravexa.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cravexa.R

data class OnboardingItem(
    val id: Int,
    @DrawableRes val illustrationRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
)

object OnboardingPages {
    val items = listOf(
        OnboardingItem(
            id = 1,
            illustrationRes = R.drawable.ic_onboarding_homemade,
            titleRes = R.string.onboarding_title_1,
            descriptionRes = R.string.onboarding_desc_1
        ),
        OnboardingItem(
            id = 2,
            illustrationRes = R.drawable.ic_onboarding_creators,
            titleRes = R.string.onboarding_title_2,
            descriptionRes = R.string.onboarding_desc_2
        ),
        OnboardingItem(
            id = 3,
            illustrationRes = R.drawable.ic_onboarding_regional,
            titleRes = R.string.onboarding_title_3,
            descriptionRes = R.string.onboarding_desc_3
        ),
        OnboardingItem(
            id = 4,
            illustrationRes = R.drawable.cravexa_logo,
            titleRes = R.string.onboarding_title_4,
            descriptionRes = R.string.onboarding_desc_4
        )
    )
}

