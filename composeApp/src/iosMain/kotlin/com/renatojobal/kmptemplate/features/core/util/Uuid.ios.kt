package com.renatojobal.kmptemplate.features.core.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
