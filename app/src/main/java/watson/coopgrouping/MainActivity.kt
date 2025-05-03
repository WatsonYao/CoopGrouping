package watson.coopgrouping

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    log("a create")
    setContentView(R.layout.activity_main)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    val viewPager: ViewPager2 = findViewById(R.id.main)
    val adapter = PagerAdapter(this)

    viewPager.adapter = adapter
  }
}

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

  override fun getItemCount(): Int = 2

  override fun createFragment(position: Int): Fragment {
    return if (position == 0) {
      MainFragment()
    } else {
      SecondFragment()
    }
  }
}

//https://splatoon2.ink/data/coop-schedules.json
//https://splatoon3.ink/data/schedules.json





