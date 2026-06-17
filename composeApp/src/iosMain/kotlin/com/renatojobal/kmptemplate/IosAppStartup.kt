package com.renatojobal.kmptemplate

/**
 * No-arg startup wrapper for iOS. Swift calls this from `iOSApp.init()` after
 * wiring StoreKit2 and Crashlytics bridges into their respective holders.
 */
fun iosInitApp() {
    initApp()
}
