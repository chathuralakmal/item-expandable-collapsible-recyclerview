package com.beersample.app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.collections.HashMap

class FilterActivity : AppCompatActivity() {

    private var filterData: HashMap<String,String> = HashMap()

    var abvMin : TextInputEditText? = null
    var abvMax : TextInputEditText? = null
    var ibuMin : TextInputEditText? = null
    var ibuMax : TextInputEditText? = null
    var ebcMin : TextInputEditText? = null
    var ebcMax : TextInputEditText? = null
    var dateBefore : TextInputEditText? = null
    var dateAfter : TextInputEditText? = null
    var yeast : TextInputEditText? = null
    var hops : TextInputEditText? = null
    var malt : TextInputEditText? = null
    var food : TextInputEditText? = null
    var ids : TextInputEditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        abvMin = findViewById(R.id.abv_min_edit_text)
        abvMax = findViewById(R.id.abv_max_edit_text)
        ibuMin = findViewById(R.id.ibu_min_edit_text)
        ibuMax = findViewById(R.id.ibu_max_edit_text)
        ebcMin = findViewById(R.id.ebc_min_edit_text)
        ebcMax = findViewById(R.id.ebc_max_edit_text)
        dateBefore = findViewById(R.id.before_date_edit_text)
        dateAfter = findViewById(R.id.after_date_edit_text)
        yeast = findViewById(R.id.yeast_edit_text)
        hops = findViewById(R.id.hops_edit_text)
        malt = findViewById(R.id.malt_edit_text)
        food = findViewById(R.id.food_edit_text)
        ids = findViewById(R.id.ids_edit_text)


        val hashMap = intent.getSerializableExtra("hashmap") as HashMap<String, String>
        filterData = hashMap

        val buttonApply : Button = findViewById(R.id.btn_apply)
        buttonApply.setOnClickListener {
            validateFilter()
        }

        val buttonReset : Button = findViewById(R.id.btn_reset)
        buttonReset.setOnClickListener {
            val returnIntent = intent
            filterData = HashMap()
            returnIntent.putExtra("isReset",true)
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        dateBefore?.setOnClickListener {
            calendarDialog(dateBefore)
        }

        dateAfter?.setOnClickListener {
            calendarDialog(dateAfter)
        }

        loadData()
    }

    private fun calendarDialog(editText: TextInputEditText?){
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if(monthOfYear < 10){
                editText?.setText("0${monthOfYear}-${year}")
            }else{
                editText?.setText("${monthOfYear}-${year}")
            }

        }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private fun loadData(){
        abvMin?.setText(filterData.get("abv_gt"))
        abvMax?.setText(filterData.get("abv_lt"))

        ibuMin?.setText(filterData.get("ibu_gt"))
        ibuMax?.setText(filterData.get("ibu_lt"))

        ebcMin?.setText(filterData.get("ebc_gt"))
        ebcMax?.setText(filterData.get("ebc_lt"))

        dateBefore?.setText(filterData.get("brewed_before"))
        dateAfter?.setText(filterData.get("brewed_after"))

        yeast?.setText(filterData.get("yeast"))
        hops?.setText(filterData.get("hops"))
        malt?.setText(filterData.get("malt"))
        food?.setText(filterData.get("food"))
        ids?.setText(filterData.get("ids"))
    }

    private fun validateFilter(){


        if(abvMin?.text?.isNotEmpty() == true){
            filterData.put("abv_gt",abvMin?.text.toString())
        }
        if(abvMax?.text?.isNotEmpty() == true){
            filterData.put("abv_lt",abvMax?.text.toString())
        }

        if(ibuMax?.text?.isNotEmpty() == true){
            filterData.put("ibu_lt",ibuMax?.text.toString())
        }
        if(ibuMin?.text?.isNotEmpty() == true){
            filterData.put("ibu_gt",ibuMin?.text.toString())
        }

        if(ebcMax?.text?.isNotEmpty() == true){
            filterData.put("ebc_lt",ebcMax?.text.toString())
        }
        if(ebcMin?.text?.isNotEmpty() == true){
            filterData.put("ebc_gt",ebcMin?.text.toString())
        }

        if(dateBefore?.text?.isNotEmpty() == true){
            filterData.put("brewed_before",dateBefore?.text.toString())
        }
        if(dateAfter?.text?.isNotEmpty() == true){
            filterData.put("brewed_after",dateAfter?.text.toString())
        }

        if(yeast?.text?.isNotEmpty() == true){
            filterData.put("yeast",yeast?.text.toString())
        }

        if(hops?.text?.isNotEmpty() == true){
            filterData.put("hops",hops?.text.toString())
        }

        if(malt?.text?.isNotEmpty() == true){
            filterData.put("malt",malt?.text.toString())
        }

        if(food?.text?.isNotEmpty() == true){
            filterData.put("food",food?.text.toString())
        }

        if(ids?.text?.isNotEmpty() == true){
            filterData.put("ids",ids?.text.toString())
        }

        val returnIntent = intent
        returnIntent.putExtra("hashmap",filterData)
        returnIntent.putExtra("isReset",false)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}