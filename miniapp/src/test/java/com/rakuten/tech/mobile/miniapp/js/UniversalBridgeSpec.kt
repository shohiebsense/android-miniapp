package com.rakuten.tech.mobile.miniapp.js

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.rakuten.tech.mobile.miniapp.*
import com.rakuten.tech.mobile.miniapp.TEST_CALLBACK_ID
import com.rakuten.tech.mobile.miniapp.TEST_MA_ID
import com.rakuten.tech.mobile.miniapp.permission.*
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.itThrows
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.*
import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
@Suppress("LargeClass")
class UniversalBridgeSpec : BridgeCommon() {

    private val miniAppBridge: MiniAppMessageBridge = Mockito.spy(
        createDefaultMiniAppMessageBridge()
    )

    private val universalBridgeCallbackObj = CallbackObj(
        action = ActionType.UNIVERSAL_BRIDGE.action,
        param = "{\"content\": \"test\"}",
        id = TEST_CALLBACK_ID
    )

    private val universalBridgeJsonStr = Gson().toJson(universalBridgeCallbackObj)

    @Before
    fun setUp() {
        ActivityScenario.launch(TestActivity::class.java).onActivity { activity ->
            When calling miniAppBridge.createBridgeExecutor(webViewListener) itReturns bridgeExecutor

            miniAppBridge.init(
                activity = activity,
                webViewListener = webViewListener,
                customPermissionCache = mock(),
                downloadedManifestCache = mock(),
                miniAppId = TEST_MA_ID,
                ratDispatcher = mock(),
                secureStorageDispatcher = mock()
            )
        }
    }

    @Test
    fun `miniAppBridge should call onSendJsonToHostApp if universal bridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(miniAppBridge).onSendJsonToHostApp(
            universalBridgeCallbackObj
        )
    }

    @Test
    fun `miniAppBridge should call sendJsonToHostApp if universal bridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(miniAppBridge).sendJsonToHostApp(
            eq(universalBridgeCallbackObj.param),
            any(),
            any()
        )
    }

    @Test
    fun `should invoke onError if universal bridge json is not valid`() {
        val onError: (String) -> Unit = Mockito.spy { }
        val errorMessage = "${ErrorBridgeMessage.ERR_UNIVERSAL_BRIDGE} null or blank"
        miniAppBridge.sendJsonToHostApp("", onSuccess = {}, onError = onError)
        verify(onError).invoke(errorMessage)
    }

    @Test
    fun `should invoke onSuccess if universal bridge json is valid`() {
        val onSuccess: (Any) -> Unit = Mockito.spy { }
        val content = "{\"content\": \"test\"}"
        miniAppBridge.sendJsonToHostApp(content, onSuccess = onSuccess, onError = { })
        verify(onSuccess).invoke(content)
    }

    @Test
    fun `bridgeExecutor should call postValue if universal bridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(bridgeExecutor).postValue(
            universalBridgeCallbackObj.id, universalBridgeCallbackObj.param.toString()
        )
    }

    @Test
    fun `webViewListener runSuccessCallback if universal bridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(webViewListener).runSuccessCallback(
            universalBridgeCallbackObj.id, universalBridgeCallbackObj.param.toString()
        )
    }

    @Test
    fun `webViewListener should call runSuccessCallback if universal bridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(webViewListener).runSuccessCallback(
            universalBridgeCallbackObj.id, universalBridgeCallbackObj.param.toString()
        )
    }

    @Test(expected = RuntimeException::class)
    fun `bridgeExecutor postValue should throw an exception`() {
        ActivityScenario.launch(TestActivity::class.java).onActivity { activity ->
            val miniAppBridge = Mockito.spy(createDefaultMiniAppMessageBridge())
            val webViewListener =
                spy(createErrorWebViewListener("${ErrorBridgeMessage.ERR_UNIVERSAL_BRIDGE} null or blank"))
            When calling miniAppBridge.createBridgeExecutor(webViewListener) itReturns bridgeExecutor
            When calling webViewListener.runSuccessCallback(
                any(),
                any()
            ) itThrows RuntimeException()
            miniAppBridge.init(
                activity = activity,
                webViewListener = webViewListener,
                customPermissionCache = mock(),
                downloadedManifestCache = mock(),
                miniAppId = TEST_MA_ID,
                ratDispatcher = mock(),
                secureStorageDispatcher = mock()
            )

            miniAppBridge.postMessage(universalBridgeJsonStr)
            given(
                bridgeExecutor.postValue(
                    universalBridgeCallbackObj.id, universalBridgeCallbackObj.param.toString()
                )
            ).willThrow(RuntimeException())
        }
    }

    @Test(expected = RuntimeException::class)
    fun `bridgeExecutor postError should throw an exception`() {
        ActivityScenario.launch(TestActivity::class.java).onActivity { activity ->
            val miniAppBridge = Mockito.spy(createDefaultMiniAppMessageBridge())
            When calling miniAppBridge.createBridgeExecutor(webViewListener) itReturns bridgeExecutor
            When calling webViewListener.runErrorCallback(
                org.amshove.kluent.any(),
                org.amshove.kluent.any()
            ) itThrows RuntimeException()
            miniAppBridge.init(
                activity = activity,
                webViewListener = webViewListener,
                customPermissionCache = mock(),
                downloadedManifestCache = mock(),
                miniAppId = TEST_MA_ID,
                ratDispatcher = mock(),
                secureStorageDispatcher = mock()
            )

            val nullUniversalBridge = CallbackObj(
                action = ActionType.UNIVERSAL_BRIDGE.action, param = null, id = TEST_CALLBACK_ID
            )

            miniAppBridge.postMessage(Gson().toJson(nullUniversalBridge))
            given(
                bridgeExecutor.postError(
                    universalBridgeCallbackObj.id, any()
                )
            ).willThrow(RuntimeException())
        }
    }

    @Test
    fun `bridgeExecutor should call postValue if universalBridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(bridgeExecutor).postValue(
            universalBridgeCallbackObj.id, universalBridgeCallbackObj.param.toString()
        )
    }

    @Test
    fun `bridgeExecutor should call postError if universalBridge json is null`() {
        val nullUniversalBridge = CallbackObj(
            action = ActionType.UNIVERSAL_BRIDGE.action, param = null, id = TEST_CALLBACK_ID
        )

        miniAppBridge.postMessage(Gson().toJson(nullUniversalBridge))
        val errorMessage = "${ErrorBridgeMessage.ERR_UNIVERSAL_BRIDGE} null or blank"
        verify(bridgeExecutor).postError(universalBridgeCallbackObj.id, errorMessage)
    }

    @Test
    fun `webViewListener should call runErrorCallback if universalBridge json is not valid`() {

        val nullUniversalBridge = CallbackObj(
            action = ActionType.UNIVERSAL_BRIDGE.action, param = null, id = TEST_CALLBACK_ID
        )

        miniAppBridge.postMessage(Gson().toJson(nullUniversalBridge))
        val errorMessage = "${ErrorBridgeMessage.ERR_UNIVERSAL_BRIDGE} null or blank"
        verify(webViewListener).runErrorCallback(universalBridgeCallbackObj.id, errorMessage)
    }

    @Test
    fun `bridgeExecutor should call postError if universalBridge json is not valid`() {

        val nullUniversalBridge = CallbackObj(
            action = ActionType.UNIVERSAL_BRIDGE.action, param = "", id = TEST_CALLBACK_ID
        )

        miniAppBridge.postMessage(Gson().toJson(nullUniversalBridge))
        val errorMessage = "${ErrorBridgeMessage.ERR_UNIVERSAL_BRIDGE} null or blank"
        verify(bridgeExecutor).postError(universalBridgeCallbackObj.id, errorMessage)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `miniAppBridge post message should throw an exception if bridge executor is not initialized`() {
        val miniAppBridge = Mockito.spy(createDefaultMiniAppMessageBridge())
        miniAppBridge.postMessage(universalBridgeJsonStr)
    }

    @Test
    fun `bridge executor should call postValue if universalBridge json is valid`() {
        miniAppBridge.postMessage(universalBridgeJsonStr)
        verify(bridgeExecutor, times(1)).postValue(
            TEST_CALLBACK_ID,
            universalBridgeCallbackObj.param.toString(),
        )
    }

    @Test
    fun `bridgeExecutor postValue should not be called if it is not initialized`() {
        ActivityScenario.launch(TestActivity::class.java).onActivity { activity ->
            val miniAppBridge = Mockito.spy(createDefaultMiniAppMessageBridge())
            miniAppBridge.init(
                activity = activity,
                webViewListener = webViewListener,
                customPermissionCache = mock(),
                downloadedManifestCache = mock(),
                miniAppId = TEST_MA_ID,
                ratDispatcher = mock(),
                secureStorageDispatcher = mock()
            )
            miniAppBridge.postMessage(universalBridgeJsonStr)

            verify(
                bridgeExecutor, times(0)
            ).postValue(universalBridgeCallbackObj.id, "")
        }
    }

    @Test
    fun `native event type should return the correct value`() {
        NativeEventType.RECEIVE_UNIVERSAL_JSON_INFO.value shouldBeEqualTo "miniappreceivejsoninfo"
    }
}
