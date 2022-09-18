package com.beersample.app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.beersample.app.tableview.model.Beer
import com.beersample.app.utils.ResponseHandler
import com.beersample.app.utils.WebService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beersample.app.tableview.BeerListAdapter
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.widget.doOnTextChanged
import com.beersample.app.utils.Util


const val PER_PAGE = "25"
class MainActivity : AppCompatActivity(), ResponseHandler {

    private var filterData: HashMap<String,String> = HashMap()
    private var pageNumber = 1

    private var recyclerView: RecyclerView? = null
    private var emptyView: LinearLayout? = null
    private var adapter: BeerListAdapter? = null

    private var beerList : List<Beer> = arrayListOf<Beer>()

    private var searchQuery: String = ""

    var filterButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filterButton = findViewById(R.id.btn_filter)
        filterButton?.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("hashmap",filterData)
            resultLauncher.launch(intent)
        }

        emptyView = findViewById(R.id.empty_result_view)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    filterData.replace("page","$pageNumber")
                    getBeerData(searchQuery, filterData)
                }
            }
        })
        //Initial Get Beer Data
        searchData()

        //Search Field
        val search: EditText = findViewById(R.id.search_text)
        search.doOnTextChanged { text, start, before, count ->
            //If user enters more than 3 items, start searching
            if(count > 3){
                pageNumber = 1
                searchQuery = text.toString()
                searchData()
            }

            // User Delete Query and Refresh New List Again
            if(start ==0 && before == 1){
                pageNumber = 1
                searchQuery = ""
                searchData()
            }
        }
    }

    private fun searchData(){
        filterData.put("per_page", PER_PAGE)
        filterData.put("page","$pageNumber")
        if(filterData.size > 2){
            filterButton?.setText("Filters (${filterData.size - 2})")
        }else{
            filterButton?.setText(getString(R.string.filter))
        }
        getBeerData(searchQuery, filterData)
    }

    private fun getBeerData(searchQuery:String, hashMap: HashMap<String, String>){
        WebService.getBeerData(this ,searchQuery, hashMap)
    }

    override fun serviceResponse(status: Boolean, message: String, tag: String) {
        runOnUiThread {
            Util.showAlert(message,getString(R.string.alert),this)
        }
    }

    override fun serviceBeerResponse(
        status: Boolean,
        beers: List<Beer>,
        message: String,
        page: String,
        tag: String
    ) {
        runOnUiThread {
            loadRecyclerView(beers,page)
        }
        super.serviceBeerResponse(status, beers, message, page, tag)
    }

    private fun loadRecyclerView(beers: List<Beer>, page: String){
        if(page == "1" && beers.isNotEmpty()){
            emptyViewHandle(false)
            beerList = beers
            adapter = BeerListAdapter(beerList, this)
            recyclerView?.layoutManager = LinearLayoutManager(this)
            recyclerView?.adapter = adapter

            pageNumber = page.toInt() + 1
        }else if(page == "1" && beers.isEmpty()){
            emptyViewHandle(true)
        }else if(beers.isNotEmpty()){
            emptyViewHandle(false)
            beerList = beerList + beers
            adapter?.update(beerList)

            pageNumber = page.toInt() + 1
        }
    }

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if(data?.getBooleanExtra("isReset",false) == true){
                pageNumber = 1
                filterData = HashMap()
            }else{
                val hashMap = data?.getSerializableExtra("hashmap") as HashMap<String, String>
                pageNumber = 1
                filterData = hashMap
            }
            searchData()
        }
    }

    private fun emptyViewHandle(isEmpty: Boolean){
        if(isEmpty){
            recyclerView?.visibility = View.GONE
        }else{
            recyclerView?.visibility = View.VISIBLE
        }
    }
}