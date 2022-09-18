package com.beersample.app.tableview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beersample.app.R
import com.beersample.app.tableview.model.Beer

import com.squareup.picasso.Picasso



class BeerListAdapter(private var mList: List<Beer>, private val context: Context) : RecyclerView.Adapter<BeerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_beer_layout, parent, false)

        return ViewHolder(view) {
            val beerItem = mList[it]
            beerItem.expanded = !beerItem.expanded
            notifyItemChanged(it)
        }
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val beerItem = mList[position]
        holder.titleText.text = beerItem.name

        if(beerItem.expanded){
            holder.subItem.visibility = View.VISIBLE
            holder.arrowImage.setImageResource(R.drawable.up_arrow)
        }else{
            holder.subItem.visibility = View.GONE
            holder.arrowImage.setImageResource(R.drawable.down_arrow)
        }



        Picasso.get().load(beerItem.imageUrl).placeholder(R.drawable.beer).into(holder.beerImage)

        holder.descriptionTitle.text = context.getString(R.string.description)
        holder.description.text = beerItem.description

        holder.foodPairingTitle.text = context.getString(R.string.food_pairing)
        holder.foodPairing.text = beerItem.foodPairing.joinToString(separator = ", ")


        holder.firstBrewed.text = (beerItem.firstBrewed)
        holder.abv.text = (context.getString(R.string.abv,beerItem.abv))

        holder.itemTag.text = beerItem.tagline

    }


    class ViewHolder(ItemView: View, onClickPosition: (Int) -> Unit) : RecyclerView.ViewHolder(ItemView) {
        val titleText: TextView = itemView.findViewById(R.id.item_title)
        val itemTag: TextView = itemView.findViewById(R.id.item_tag)
        val beerImage: ImageView = itemView.findViewById(R.id.beer_image)
        val arrowImage: ImageView = itemView.findViewById(R.id.expand_Arrow)


        val subItem: LinearLayout = itemView.findViewById(R.id.sub_item)

        val descriptionTitle: TextView = itemView.findViewById(R.id.item_description_title)
        val description: TextView = itemView.findViewById(R.id.item_description)

        val foodPairingTitle: TextView = itemView.findViewById(R.id.item_food_pairing_title)
        val foodPairing: TextView = itemView.findViewById(R.id.item_food_pairing)





        val firstBrewed: TextView = itemView.findViewById(R.id.bottom_first_brewed)
        val abv: TextView = itemView.findViewById(R.id.bottom_abv)

        init {
            itemView.setOnClickListener {
                onClickPosition(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun update(modelList:List<Beer>){
        mList = modelList
        notifyDataSetChanged()
    }
}