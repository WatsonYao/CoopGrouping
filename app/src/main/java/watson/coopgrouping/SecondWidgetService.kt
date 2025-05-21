package watson.coopgrouping

import android.content.Intent
import android.widget.RemoteViewsService

class SecondWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return SecondWidgetRemoteViewsFactory(this.applicationContext, intent)
    }
}
