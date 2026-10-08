package uz.ttpu.movieshelf

import android.app.Application
import uz.ttpu.movieshelf.di.AppContainer

class MovieShelfApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}