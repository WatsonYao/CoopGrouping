package watson.coopgrouping

import android.content.Intent
import android.widget.RemoteViewsService

class SecondWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        log("SecondWidgetService.onGetViewFactory")
        return SecondWidgetRemoteViewsFactory(this.applicationContext, intent)
    }
}
