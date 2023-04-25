package com.example.foundation

import com.example.foundation.models.Repository

interface BaseApplication {

    val repositories: List<Repository>
}