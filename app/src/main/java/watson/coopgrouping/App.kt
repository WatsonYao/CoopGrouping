package watson.coopgrouping

import android.app.Application

class App :Application() {
  override fun onCreate() {
    super.onCreate()
    log("create")
  }
}