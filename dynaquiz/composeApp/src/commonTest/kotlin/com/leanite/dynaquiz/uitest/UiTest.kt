package com.leanite.dynaquiz.uitest

/**
 * Base class for Compose UI tests in commonTest.
 *
 * Compose UI testing on the Android target runs on the JVM and needs an Android runtime
 * (Build, Looper, ComponentActivity), which Robolectric provides. Robolectric is activated by
 * `@RunWith(RobolectricTestRunner::class)`, a JVM-only annotation that cannot live in commonTest.
 *
 * This expect/actual base centralizes that constraint: the Android actual class wires Robolectric,
 * the iOS actual class is a no-op, and tests in commonTest just extend [UiTest].
 */
expect abstract class UiTest()
