package com.rakuten.tech.mobile.testapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rakuten.tech.mobile.miniapp.AppManifestConfig
import com.rakuten.tech.mobile.miniapp.analytics.MiniAppAnalyticsConfig
import com.rakuten.tech.mobile.miniapp.errors.MiniAppAccessTokenError
import com.rakuten.tech.mobile.miniapp.js.userinfo.Contact
import com.rakuten.tech.mobile.miniapp.js.userinfo.Points
import com.rakuten.tech.mobile.miniapp.js.userinfo.TokenData
import com.rakuten.tech.mobile.miniapp.testapp.BuildConfig
import com.rakuten.tech.mobile.miniapp.testapp.R
import com.rakuten.tech.mobile.testapp.BuildVariant


internal class Cache(context: Context, manifestConfig: AppManifestConfig) {

    private val gson = Gson()
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "com.rakuten.tech.mobile.miniapp.sample.settings",
        Context.MODE_PRIVATE
    )
    val productionBaseUrl = context.getString(R.string.prodBaseUrl)
    val stagingBBaseUrl = context.getString(R.string.stagingBaseUrl)

    val rasCredentialData = RasCredentialData(context, manifestConfig)

    fun getBaseUrl(isProduction: Boolean) = if (isProduction) {
        productionBaseUrl
    } else stagingBBaseUrl

    var uniqueId: String?
        get() = prefs.getString(UNIQUE_ID, null)
        set(uuid) = prefs.edit().putString(UNIQUE_ID, uuid).apply()

    var uniqueIdError: String?
        get() = prefs.getString(UNIQUE_ID_ERROR, null)
        set(uniqueIdError) = prefs.edit().putString(UNIQUE_ID_ERROR, uniqueIdError).apply()

    var messagingUniqueIdError: String?
        get() = prefs.getString(MESSAGING_UNIQUE_ID_ERROR, null)
        set(messagingUniqueIdError) = prefs.edit()
            .putString(MESSAGING_UNIQUE_ID_ERROR, messagingUniqueIdError).apply()

    var mauIdError: String?
        get() = prefs.getString(MAUID_ERROR, null)
        set(mauIdError) = prefs.edit().putString(MAUID_ERROR, mauIdError).apply()

    var isSettingSaved: Boolean
        get() = prefs.getBoolean(IS_SETTING_SAVED, false)
        set(isSettingSaved) = prefs.edit().putBoolean(IS_SETTING_SAVED, isSettingSaved).apply()

    var profileName: String?
        get() = prefs.getString(PROFILE_NAME, null)
        set(profileName) = prefs.edit().putString(PROFILE_NAME, profileName).apply()

    var profilePictureUrl: String?
        get() = prefs.getString(PROFILE_PICTURE_URL, null)
        set(profilePictureUrl) = prefs.edit().putString(PROFILE_PICTURE_URL, profilePictureUrl)
            .apply()

    var profilePictureUrlBase64: String?
        get() = prefs.getString(PROFILE_PICTURE_URL_BASE_64, null)
        set(profilePictureUrlBase64) = prefs.edit()
            .putString(PROFILE_PICTURE_URL_BASE_64, profilePictureUrlBase64)
            .apply()

    var tokenData: TokenData?
        get() = gson.fromJson(prefs.getString(TOKEN_DATA, null), TokenData::class.java)
        set(tokenData) = prefs.edit().putString(TOKEN_DATA, gson.toJson(tokenData))
            .apply()

    var contacts: ArrayList<Contact>?
        get() = Gson().fromJson(
            prefs.getString(CONTACTS, null),
            object : TypeToken<ArrayList<Contact>>() {}.type
        )
        set(contacts) = prefs.edit().putString(CONTACTS, Gson().toJson(contacts)).apply()

    val isContactsSaved: Boolean
        get() = prefs.contains(CONTACTS)

    var urlParameters: String?
        get() = prefs.getString(URL_PARAMETERS, null)
        set(urlParameters) = prefs.edit().putString(URL_PARAMETERS, urlParameters).apply()

    var miniAppAnalyticsConfigs: List<MiniAppAnalyticsConfig>?
        get() = Gson().fromJson(
            prefs.getString(ANALYTIC_CONFIGS, null),
            object : TypeToken<List<MiniAppAnalyticsConfig>>() {}.type
        )
        set(miniAppAnalyticsConfigs) = prefs.edit()
            .putString(ANALYTIC_CONFIGS, Gson().toJson(miniAppAnalyticsConfigs)).apply()

    var accessTokenError: MiniAppAccessTokenError?
        get() = gson.fromJson(
            prefs.getString(ACCESS_TOKEN_ERROR, null),
            MiniAppAccessTokenError::class.java
        )
        set(accessTokenError) = prefs.edit()
            .putString(ACCESS_TOKEN_ERROR, gson.toJson(accessTokenError))
            .apply()

    var points: Points?
        get() = gson.fromJson(prefs.getString(POINTS, null), Points::class.java)
        set(points) = prefs.edit().putString(POINTS, gson.toJson(points))
            .apply()

    var dynamicDeeplinks: ArrayList<String>?
        get() = Gson().fromJson(
            prefs.getString(DYNAMIC_DEEPLINKS, null),
            object : TypeToken<ArrayList<String>>() {}.type
        )
        set(deeplinks) = prefs.edit().putString(DYNAMIC_DEEPLINKS, Gson().toJson(deeplinks)).apply()

    val isDynamicDeeplinksSaved: Boolean
        get() = prefs.contains(DYNAMIC_DEEPLINKS)

    var maxStorageSizeLimitInBytes: String
        get() = prefs.getString(MAX_STORAGE_SIZE_LIMIT, "5242880")
            .toString() // Default max storage is 5MB
        set(maxStorageSizeLimitInBytes) = prefs.edit()
            .putString(MAX_STORAGE_SIZE_LIMIT, maxStorageSizeLimitInBytes).apply()

    companion object {
        private const val IS_PREVIEW_MODE_DISPLAY_BY_INPUT = "is_display_by_input_preview_mode"
        private const val IS_PREVIEW_MODE = "is_preview_mode"
        private const val TEMP_IS_PREVIEW_MODE = "temp_is_preview_mode"
        private const val IS_PREVIEW_MODE_2 = "is_preview_mode_2"
        private const val TEMP_IS_PREVIEW_MODE_2 = "temp_is_preview_mode"
        private const val REQUIRE_SIGNATURE_VERIFICATION = "require_signature_verification"
        private const val TEMP_REQUIRE_SIGNATURE_VERIFICATION =
            "temp_require_signature_verification"
        private const val REQUIRE_SIGNATURE_VERIFICATION_2 = "require_signature_verification_2"
        private const val TEMP_REQUIRE_SIGNATURE_VERIFICATION_2 =
            "temp_require_signature_verification_2"
        private const val IS_PROD_VERSION_ENABLED = "is_prod_version_enabled"
        private const val TEMP_IS_PROD_VERSION_ENABLED = "temp_is_prod_version_enabled"
        private const val IS_PROD_VERSION_ENABLED_2 = "is_prod_version_enabled_2"
        private const val TEMP_IS_PROD_VERSION_ENABLED_2 = "temp_is_prod_version_enabled_2"
        private const val APP_ID = "app_id"
        private const val APP_ID_2 = "app_id_2"
        const val TEMP_APP_ID = "temp_app_id"
        const val TEMP_APP_ID_2 = "temp_app_id_2"
        private const val SUBSCRIPTION_KEY = "subscription_key"
        private const val SUBSCRIPTION_KEY_2 = "subscription_key_2"
        const val TEMP_SUBSCRIPTION_KEY = "temp_subscription_key"
        const val TEMP_SUBSCRIPTION_KEY_2 = "temp_subscription_key_2"
        private const val UNIQUE_ID = "unique_id"
        private const val UNIQUE_ID_ERROR = "unique_id_error"
        private const val MESSAGING_UNIQUE_ID_ERROR = "messaging_unique_id_error"
        private const val MAUID_ERROR = "mauid_error"
        private const val IS_SETTING_SAVED = "is_setting_saved"
        private const val PROFILE_NAME = "profile_name"
        private const val PROFILE_PICTURE_URL = "profile_picture_url"
        private const val PROFILE_PICTURE_URL_BASE_64 = "profile_picture_url_base_64"
        private const val CONTACTS = "contacts"
        private const val TOKEN_DATA = "token_data"
        private const val URL_PARAMETERS = "url_parameters"
        private const val ANALYTIC_CONFIGS = "analytic_configs"
        private const val ACCESS_TOKEN_ERROR = "access_token_error"
        private const val POINTS = "points"
        private const val DYNAMIC_DEEPLINKS = "dynamic_deeplinks"
        private const val MAX_STORAGE_SIZE_LIMIT = "max_storage_size_limit"
        private const val IS_TEMP_CLEARED = "is_temp_cleared"
    }

    @Suppress("TooManyFunctions")
    inner class RasCredentialData(context: Context, manifestConfig: AppManifestConfig) {
        val isDefaultProductionEnabled = BuildConfig.BUILD_TYPE == BuildVariant.RELEASE.value

        val tab1MiniAppCredentialCache = MiniAppCredentialCache(
            IS_PROD_VERSION_ENABLED,
            REQUIRE_SIGNATURE_VERIFICATION,
            IS_PREVIEW_MODE,
            APP_ID,
            SUBSCRIPTION_KEY
        )

        val tab1TempMiniAppCredentialCache = MiniAppCredentialCache(
            TEMP_IS_PROD_VERSION_ENABLED,
            TEMP_REQUIRE_SIGNATURE_VERIFICATION,
            TEMP_IS_PREVIEW_MODE,
            TEMP_APP_ID,
            TEMP_SUBSCRIPTION_KEY
        )

        val tab2MiniAppCredentialCache = MiniAppCredentialCache(
            IS_PROD_VERSION_ENABLED_2,
            REQUIRE_SIGNATURE_VERIFICATION_2,
            IS_PREVIEW_MODE_2,
            APP_ID_2,
            SUBSCRIPTION_KEY_2
        )

        val tab2TempMiniAppCredentialCache = MiniAppCredentialCache(
            TEMP_IS_PROD_VERSION_ENABLED_2,
            TEMP_REQUIRE_SIGNATURE_VERIFICATION_2,
            TEMP_IS_PREVIEW_MODE_2,
            TEMP_APP_ID_2,
            TEMP_SUBSCRIPTION_KEY_2
        )

        var isTempCleared: Boolean
            get() = prefs.getBoolean(IS_TEMP_CLEARED, false)
            set(isTempSaved) = prefs.edit()
                .putBoolean(IS_TEMP_CLEARED, isTempSaved).apply()

        val defaultProductionData = MiniAppCredentialData(
            isProduction = isDefaultProductionEnabled,
            isVerificationRequired = manifestConfig.requireSignatureVerification(),
            isPreviewMode = manifestConfig.isPreviewMode(),
            projectId = context.getString(R.string.prodProjectId),
            subscriptionId = context.getString(R.string.prodSubscriptionKey)
        )

        val defaultStagingData = MiniAppCredentialData(
            isProduction = isDefaultProductionEnabled,
            isVerificationRequired = manifestConfig.requireSignatureVerification(),
            isPreviewMode = manifestConfig.isPreviewMode(),
            projectId = context.getString(R.string.stagingProjectId),
            subscriptionId = context.getString(R.string.stagingSubscriptionKey)
        )


        var isPreviewModeDisplayByInput: Boolean?
            get() =
                if (prefs.contains(IS_PREVIEW_MODE_DISPLAY_BY_INPUT))
                    prefs.getBoolean(IS_PREVIEW_MODE_DISPLAY_BY_INPUT, true)
                else
                    null
            set(isPreviewMode) = prefs.edit()
                .putBoolean(IS_PREVIEW_MODE_DISPLAY_BY_INPUT, isPreviewMode!!).apply()

        var isDisplayInputPreviewMode: Boolean?
            get() =
                if (prefs.contains(IS_PREVIEW_MODE))
                    prefs.getBoolean(IS_PREVIEW_MODE, true)
                else
                    null
            set(isPreviewMode) = prefs.edit().putBoolean(IS_PREVIEW_MODE, isPreviewMode!!).apply()


        fun getDefaultData(
        ): MiniAppCredentialData {
            return if (isDefaultProductionEnabled) defaultProductionData
            else defaultStagingData
        }

        fun getTab1Data(): MiniAppCredentialData =
            tab1MiniAppCredentialCache.getData(prefs, getDefaultData())

        private fun getTab1TempData(): MiniAppCredentialData =
            tab1TempMiniAppCredentialCache.getData(prefs, getDefaultData())

        fun getTab1CurrentData(): MiniAppCredentialData =
            if (isTempCleared && isSettingSaved) getTab1Data() else getTab1TempData()

        fun getTab2Data(): MiniAppCredentialData =
            tab2MiniAppCredentialCache.getData(prefs, getDefaultData())

        fun getTab2CurrentData(): MiniAppCredentialData =
            if (isTempCleared && isSettingSaved) getTab2Data() else getTab2TempData()

        private fun getTab2TempData(): MiniAppCredentialData =
            tab2TempMiniAppCredentialCache.getData(prefs, getDefaultData())


        fun saveTab1Data() {
            tab1MiniAppCredentialCache.setData(
                prefs.edit(),
                getTab1TempData()
            )
            tab1TempMiniAppCredentialCache.clear(prefs.edit())
        }


        fun setTempTab1IsProduction(isProduction: Boolean) {
            tab1TempMiniAppCredentialCache.setIsProduction(prefs.edit(), isProduction)
            isTempCleared = false
        }

        fun setTempTab1IsVerificationRequired(isVerificationRequired: Boolean) {
            tab1TempMiniAppCredentialCache.setIsVerificationRequired(
                prefs.edit(),
                isVerificationRequired
            )
            isTempCleared = false
        }

        fun setTempTab1IsPreviewMode(isPreviewMode: Boolean) {
            tab1TempMiniAppCredentialCache.setIsPreviewMode(prefs.edit(), isPreviewMode)
        }

        fun setTempTab1Data(
            credentialData: MiniAppCredentialData
        ) {
            tab1TempMiniAppCredentialCache.setData(
                prefs.edit(),
                credentialData
            )
        }

        fun saveTab2Data() {
            tab2MiniAppCredentialCache.setData(
                prefs.edit(),
                getTab2TempData()
            )
            tab2TempMiniAppCredentialCache.clear(prefs.edit())
        }

        fun setTempTab2IsProduction(isProduction: Boolean) {
            tab2TempMiniAppCredentialCache.setIsProduction(prefs.edit(), isProduction)
        }

        fun setTempTab2IsVerificationRequired(isSignatureVerificationRequired: Boolean) {
            tab2TempMiniAppCredentialCache.setIsVerificationRequired(
                prefs.edit(),
                isSignatureVerificationRequired
            )
        }

        fun setTempTab2IsPreviewMode(isPreviewMode: Boolean) {
            tab2TempMiniAppCredentialCache.setIsPreviewMode(prefs.edit(), isPreviewMode)
        }

        fun setTempTab2Data(
            credentialData: MiniAppCredentialData
        ) {
            tab2TempMiniAppCredentialCache.setData(
                prefs.edit(),
                credentialData
            )
        }
    }

}
