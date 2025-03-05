package watson.coopgrouping

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ImageView3(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

  private val aspectRatio = 2.0 / 1.0 // 2:1 比例

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val height = (width / aspectRatio).toInt()
    val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
  }
}