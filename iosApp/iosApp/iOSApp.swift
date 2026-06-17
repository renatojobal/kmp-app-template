import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Boots Koin + Napier. Wire any iOS-side bridges (Crashlytics, StoreKit, etc.)
        // before this call so they're available to the first Koin resolves.
        IosAppStartupKt.iosInitApp()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
