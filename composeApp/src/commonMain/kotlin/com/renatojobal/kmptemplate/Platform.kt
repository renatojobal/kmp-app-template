package com.renatojobal.kmptemplate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform