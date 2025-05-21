package watson.coopgrouping

import android.content.Intent
import android.widget.RemoteViewsService

class MainWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return MainWidgetRemoteViewsFactory(this.applicationContext, intent)
    }
}
