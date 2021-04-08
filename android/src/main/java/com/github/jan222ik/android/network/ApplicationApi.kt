//package com.github.jan222ik.android.network
//
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.client.request.url
//import io.ktor.http.Url
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//@SharedImmutable
//internal expect val ApplicationDispatcher: CoroutineDispatcher
//
//class ApplicationApi {
//    private val client = HttpClient()
//
//    private val address = Url("https://cors-test.appspot.com/test")
//
//    fun about(callback: (String) -> Unit) {
//        GlobalScope.launch(ApplicationDispatcher) {
//            val result: String = client.get {
//                url(address.toString())
//            }
//
//            callback(result)
//        }
//    }
//}
