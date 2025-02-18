package me.crazystone.minihabits.utils

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

object Env {

    private val dotenv: Dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of '.env', use 'env'
    }

    fun get(): Dotenv {
        return dotenv
    }

}