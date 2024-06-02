package com.example.quick_med


import MedicineAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import android.widget.Toast
import com.example.quick_med.Medicine

class Search_Med : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private val serviceKey = "zp%2FXmsF6TzhsNiU1jUF2ElWrarTPBUzV7ccDYcc8jPtbcz3%2BkkzF9ZG%2BegIM2ib7CgLvq1LEZF%2FrG0MH1gDqLw%3D%3D"

    private fun searchMedicines(query: String) {
        thread {
            try {
                val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
                val url = URL("https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=$serviceKey&pageNo=1&numOfRows=100&itemName=$encodedQuery&type=json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().use { it.readText() }
                println("API Response: $response")

                val jsonObject = JSONObject(response)
                val body = jsonObject.getJSONObject("body")
                val items = body.getJSONArray("items")

                val medicines = mutableListOf<Medicine>()
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val name = item.getString("itemName")
                    val description = item.getString("efcyQesitm")
                    val imageUrl = if (item.has("itemImage") && !item.isNull("itemImage")) item.getString("itemImage") else null
                    medicines.add(Medicine(name, description, imageUrl))
                }

                runOnUiThread {
                    if (medicines.isNotEmpty()) {
                        listView.adapter = MedicineAdapter(this@Search_Med, medicines)
                    } else {
                        Toast.makeText(this@Search_Med, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@Search_Med, "검색에 실패했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
}
