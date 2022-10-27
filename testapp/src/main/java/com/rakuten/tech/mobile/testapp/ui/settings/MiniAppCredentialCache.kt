package com.rakuten.tech.mobile.testapp.ui.settings

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log


class MiniAppCredentialCache(
    private val isproductionKey: String,
    private val isSignatureVerificationRequiredKey: String,
    private val isPreviewModeKey: String,
    private val projectidKey: String,
    private val subscriptionKey: String,
) {

    var isCleared = false


    fun getData(
        prefs: SharedPreferences,
        defaultData: MiniAppCredentialData
    ): MiniAppCredentialData {
        return MiniAppCredentialData(
            prefs.getBoolean(isproductionKey, defaultData.isProduction),
            prefs.getBoolean(
                isSignatureVerificationRequiredKey,
                defaultData.isVerificationRequired
            ),
            prefs.getBoolean(isPreviewModeKey, defaultData.isPreviewMode),
            prefs.getString(projectidKey, null) ?: defaultData.projectId,
            prefs.getString(subscriptionKey, null) ?: defaultData.subscriptionId
        )
    }

    /**
     * no OnSharedPreferenceChangeListener added, thus requires immediate value
     */

    fun setIsProduction(
        editor: Editor,
        isProduction: Boolean
    ) {
        editor.putBoolean(isproductionKey, isProduction).commit()
    }

    fun setIsVerificationRequired(
        editor: Editor,
        isSignatureVerificationRequired: Boolean
    ) {
        editor.putBoolean(isSignatureVerificationRequiredKey, isSignatureVerificationRequired)
            .commit()
    }

    fun setIsPreviewMode(
        editor: Editor,
        isPreviewMode: Boolean
    ) {
        editor.putBoolean(isPreviewModeKey, isPreviewMode).commit()
    }

    fun setData(
        editor: Editor,
        credentialData: MiniAppCredentialData,
    ) {
        editor.apply {
            putBoolean(isproductionKey, credentialData.isProduction).commit()
            putBoolean(
                isSignatureVerificationRequiredKey,
                credentialData.isVerificationRequired
            ).commit()
            putBoolean(isPreviewModeKey, credentialData.isPreviewMode).commit()
            editor.putString(projectidKey, credentialData.projectId).commit()
            editor.putString(subscriptionKey, credentialData.subscriptionId).commit()
        }
    }

    fun clear(editor: Editor) {
        editor.apply {
            remove(isproductionKey).commit()
            remove(isPreviewModeKey).commit()
            remove(isSignatureVerificationRequiredKey).commit()
            remove(projectidKey).commit()
            remove(subscriptionKey).commit()
        }

    }

}
