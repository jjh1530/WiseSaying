package com.jjhadr.wisesaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initeData()
        initeView()
    }

    private fun initeView() {
        //스와이프 할 때 점진적으로 없어지고 생기는 효과를 줌
        viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue >= 1F -> {
                    page.alpha = 0F
                }
                //화면중앙
                position == 0F -> {
                    page.alpha = 1F
                } else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }


    private fun initeData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 앱을 들어갈 때 마다 패치
            }
        )
        //파이어베이스 비동기 통신
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility = View.GONE //통신이 완료되면 ProgressBar 안보이게함
            //작업이 성공했을 시
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes")) // jsom 가져오기
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed") //true 값
                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it //비어있는 jsonList에 JSONObject + 추가된 객체를 넣어줌
            }
        }
        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name"))
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        val adapter =  QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter =  adapter
        //왼쪽으로도 스와이프 가능하게 하기
        viewPager.setCurrentItem(adapter.itemCount/2, false)
    }
}
