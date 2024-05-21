package com.example.quick_med

import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import com.squareup.picasso.Picasso

class Search_Med : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private val serviceKey = "YOUR_SERVICE_KEY" // 실제 발급받은 서비스 키를 사용하세요

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchmed)

        searchView = findViewById(R.id.search_view)
        listView = findViewById(R.id.list_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchMedicines(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchMedicines(query: String) {
        thread {
            try {
                val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
                val url = URL("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=$serviceKey&trustEntpName=$encodedQuery&pageNo=1&startPage=1&numOfRows=3")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(response)

                val medicines = mutableListOf<Medicine>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("itemName")
                    val description = jsonObject.getString("efcyQesitm")
                    val imageUrl = jsonObject.getString("itemImage")
                    medicines.add(Medicine(name, description, imageUrl))
                }

                runOnUiThread {
                    listView.adapter = MedicineAdapter(this@Search_Med, medicines)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}