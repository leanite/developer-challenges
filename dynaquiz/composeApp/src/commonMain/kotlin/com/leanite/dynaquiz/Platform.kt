package com.leanite.dynaquiz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform