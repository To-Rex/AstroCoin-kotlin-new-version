package app.app.astrocoin.adapters

import app.app.astrocoin.models.TransferRequest
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import app.app.astrocoin.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import app.app.astrocoin.models.Getdata
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import java.util.ArrayList

class AdapterTransferR(
    var context: Context,
    var transferRequestArrayList: ArrayList<TransferRequest>

) : RecyclerView.Adapter<AdapterTransferR.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transfer, parent, false)
        return ViewHolder(view)
    }

    private var sharedPreferences: SharedPreferences? = null
    private var wallet = ""


    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transferRequest = transferRequestArrayList[position]
        holder.tranferdata.text = transferRequest.datatransfer
        holder.trtransfer.text = transferRequest.title
        holder.trcoin.text = transferRequest.amount
        if (transferRequest.datatransfer == "") {
            holder.tranferdata.visibility = View.GONE
        } else {
            holder.tranferdata.visibility = View.VISIBLE
            holder.tranferdata.text = transferRequest.datatransfer
        }

        sharedPreferences = context.getSharedPreferences("astrocoin", Context.MODE_PRIVATE)
        getUserData()

        if(transferRequest.status == "failed"&&transferRequest.wallet_to == wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE
            }else{
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionsend)
            }
            return
        }

        if(transferRequest.status == "failed"&&transferRequest.wallet_to != wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE
            }else{
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionfeild)
            }
            return
        }
        if(transferRequest.status == "success"&&transferRequest.wallet_to == wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE

            }else{
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionsoucses)
            }
            return
        }
        if (transferRequest.status == "success"&&transferRequest.wallet_to != wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE

            }else{
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transferfeilde)
            }
            return
        }

    }

    override fun getItemCount(): Int {
        return transferRequestArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trtransfer: TextView
        var trcoin: TextView
        var tranferdata: TextView
        var trimage: ShapeableImageView

        init {
            trtransfer = itemView.findViewById(R.id.trtransfer)
            trcoin = itemView.findViewById(R.id.trcoin)
            tranferdata = itemView.findViewById(R.id.tranferdata)
            trimage = itemView.findViewById(R.id.trimage)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        wallet = user.wallet
    }
}