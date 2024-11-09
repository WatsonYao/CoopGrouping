package watson.coopgrouping

import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class TeamActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

  private lateinit var tts: TextToSpeech
  private var countdown = 100
  private var timer: CountDownTimer? = null
  private var clock: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_team)
    tts = TextToSpeech(this, this)
    tts.setSpeechRate(1.2f)
    clock = findViewById(R.id.clock)

    //w3
    findViewById<Button>(R.id.w3_180).setOnClickListener {
      runTTs(bossW3_180)
    }
    //w4
    findViewById<Button>(R.id.w4_210).setOnClickListener {
      runTTs(bossW4_210)
    }
    findViewById<Button>(R.id.w4_240).setOnClickListener {
      runTTs(bossW4_240)
    }
    //w5
    findViewById<Button>(R.id.w5_240).setOnClickListener {
      runTTs(bossW5_240)
    }
    findViewById<Button>(R.id.w5_270).setOnClickListener {
      runTTs(bossW5_270)
    }
    findViewById<Button>(R.id.w5_300).setOnClickListener {
      runTTs(bossW5_300)
    }


    findViewById<Button>(R.id.close).setOnClickListener {
      closeTTs()
    }
    findViewById<Button>(R.id.exit).setOnClickListener {
      this.finish()
    }
  }

  private fun closeTTs() {
    timer?.cancel()
    timer = null
    tts.stop()
    clock?.text = "100"
  }

  private fun checkBoss(index: Int, boss: List<Thing>) {
    val find = boss.filter { it.index == index }
    if (find.size > 0) {
      speak(find)
    }
  }

  private fun speak(list: List<Thing>) {
    val items = list.map {
      "${it.position}${it.name}"
    }
    val text = items.joinToString(separator = ",")
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
  }

  private fun runTTs(boss: List<Thing>) {
    closeTTs()
    countdown = 100
    timer = object : CountDownTimer(100000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        if (countdown >= 0) {
          clock?.text = "$countdown"
          checkBoss(countdown, boss)
          countdown--
        }
      }

      override fun onFinish() {
        // 倒计时结束时的逻辑
        clock?.text = "100"
      }
    }
    timer?.start()
  }

  private val A = "左"
  private val B = "中"
  private val C = "右"

  private val bossW3_180 by lazy {
    listOf(
      Thing(100, "车", B),
      Thing(96, "塔", B),
      Thing(95, "海豚", B),
      Thing(88, "车", A),
      Thing(86, "鼹鼠", A),
      Thing(78, "蛇", A),
      Thing(76, "铁", C),
      Thing(74, "绿帽", C),
      Thing(66, "塔", C),
      Thing(64, "锅盖", A),
      Thing(61, "蛇、塔、鼹鼠", A),
      Thing(52, "鼹鼠", B),
      Thing(46, "两塔", B),
      Thing(40, "蛇", B),
      Thing(40, "塔", C),
      Thing(38, "塔", C),
      Thing(31, "垫肩、车", C),
      Thing(28, "伞", A),
    )
  }

  private val bossW4_210 by lazy {
    listOf(
      Thing(100, "伞", B),
      Thing(98, "蛇", B),
      Thing(91, "车", A),
      Thing(89, "铁", B),
      Thing(79, "塔", C),
      Thing(79, "铁", B),
      Thing(72, "绿帽", A),
      Thing(69, "海豚", A),
      Thing(68, "柱子", A),
      Thing(61, "锅盖", B),
      Thing(58, "车", C),
      Thing(58, "鼹鼠", B),
      Thing(48, "垫肩", B),
      Thing(47, "塔", C),
      Thing(47, "垫肩", C),
      Thing(43, "蛇", C),
      Thing(37, "车", C),
      Thing(35, "柱子", A),
      Thing(31, "绿帽", A),
      Thing(28, "塔", B),
    )
  }
  private val bossW4_240 by lazy {
    listOf(
      Thing(100, "伞", B),
      Thing(98, "蛇", B),
      Thing(91, "车", A),
      Thing(89, "铁", B),
      Thing(79, "塔", C),
      Thing(79, "铁", B),
      Thing(76, "绿帽", A),
      Thing(72, "海豚", A),
      Thing(69, "柱子", A),
      Thing(68, "锅盖", B),
      Thing(61, "蛇", C),
      Thing(60, "伞", A),
      Thing(58, "绿帽", B),
      Thing(58, "塔", C),
      Thing(55, "垫肩", C),
      Thing(48, "车", B),
      Thing(47, "车", C),
      Thing(43, "柱子", A),
      Thing(38, "绿帽", B),
      Thing(37, "塔", B),
      Thing(31, "铁", C),
      Thing(28, "海豚", B),
    )
  }

  private val bossW5_240 by lazy {
    listOf(
      Thing(100, "锅盖", B),
      Thing(96, "伞", B),
      Thing(94, "铁", B),
      Thing(89, "绿帽", A),
      Thing(89, "锅盖", A),
      Thing(84, "柱子", A),
      Thing(79, "铁", C),
      Thing(76, "两塔", C),
      Thing(72, "车", C),
      Thing(69, "塔", B),
      Thing(68, "铁", B),
      Thing(63, "鼹鼠", B),
      Thing(60, "铁", B),
      Thing(58, "伞", C),
      Thing(56, "垫肩", C),
      Thing(48, "铁", C),
      Thing(48, "蛇", C),
      Thing(48, "车", A),
      Thing(47, "鼹鼠", A),
      Thing(38, "鼹鼠", A),
      Thing(38, "绿帽", A),
      Thing(38, "塔", C),
      Thing(34, "鼹鼠", C),
      Thing(31, "垫肩", C),
      Thing(31, "海豚", C),
      Thing(28, "鼹鼠", A),

      )
  }
  private val bossW5_270 by lazy {
    listOf(
      Thing(100, "锅盖", B),
      Thing(99, "伞", B),
      Thing(94, "铁", B),
      Thing(91, "绿帽", A),
      Thing(88, "锅盖", A),
      Thing(87, "柱子", A),
      Thing(82, "铁", C),
      Thing(77, "两塔", C),
      Thing(73, "垫肩", A),
      Thing(72, "鼹鼠", A),
      Thing(66, "铁", A),
      Thing(64, "塔", B),
      Thing(64, "铁", B),
      Thing(61, "柱子", B),
      Thing(56, "蛇", B),
      Thing(55, "铁", C),
      Thing(54, "蛇", C),
      Thing(52, "蛇", C),
      Thing(46, "伞", C),
      Thing(46, "鼹鼠", B),
      Thing(40, "绿帽", B),
      Thing(37, "两鼹鼠", B),
      Thing(37, "绿帽", C),
      Thing(34, "锅盖", C),
      Thing(31, "塔", C),
      Thing(28, "垫肩", C),
      Thing(28, "车", A),
    )
  }

  private val bossW5_300 by lazy {
    listOf(
      Thing(100, "锅盖", B),
      Thing(99, "伞", B),
      Thing(94, "铁", B),
      Thing(91, "绿帽", A),
      Thing(88, "锅盖", A),
      Thing(87, "柱子", A),
      Thing(84, "锅盖", A),
      Thing(82, "蛇", C),
      Thing(78, "车", C),
      Thing(77, "绿帽", C),
      Thing(77, "车", C),
      Thing(73, "铁", B),
      Thing(72, "两鼹鼠", B),
      Thing(66, "车", B),
      Thing(64, "绿帽", C),
      Thing(64, "柱子", C),
      Thing(61, "伞", C),
      Thing(56, "蛇", C),
      Thing(55, "车", A),
      Thing(54, "绿帽", A),
      Thing(52, "锅盖", A),
      Thing(46, "绿帽", A),
      Thing(46, "垫肩", C),
      Thing(40, "鼹鼠", C),
      Thing(37, "鼹鼠", C),
      Thing(37, "蛇", C),
      Thing(37, "绿帽", B),
      Thing(34, "垫肩", B),
      Thing(31, "鼹鼠", B),
      Thing(28, "锅盖", B),
      Thing(28, "鼹鼠", C),
    )
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      tts.language = Locale.CHINA // 设置语言为中文，如果需要其他语言可以修改
    }
  }

  override fun onDestroy() {
    tts.stop()
    tts.shutdown()
    super.onDestroy()
  }

}


data class Thing(val index: Int, val name: String, val position: String)