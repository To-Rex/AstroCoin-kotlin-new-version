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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.app.astrocoin.models.Getdata
import com.google.android.material.bottomsheet.BottomSheetDialog
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


    @SuppressLint("SetTextI18n", "InflateParams", "MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transferRequest = transferRequestArrayList[position]
        holder.tranferdata.text = transferRequest.datatransfer
        holder.trtransfer.text = transferRequest.title
        holder.trcoin.text = transferRequest.amount
        var imgindex = 0


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
                imgindex = 1
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionsend)
            }
        }



        if(transferRequest.status == "failed"&&transferRequest.wallet_to != wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE
            }else{
                imgindex = 2
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionfeild)
            }
        }
        if(transferRequest.status == "success"&&transferRequest.wallet_to == wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE

            }else{
                imgindex = 3
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transactionsoucses)
            }

        }
        if (transferRequest.status == "success"&&transferRequest.wallet_to != wallet){
            if (transferRequest.amount==""){
                holder.trcoin.visibility = View.VISIBLE

            }else{
                imgindex = 4
                holder.trcoin.text = transferRequest.amount+" ASC"
                holder.trimage.setImageResource(R.drawable.transferfeilde)
            }

        }

        holder.itemView.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.custombottomsheet)
            val view = LayoutInflater.from(context).inflate(R.layout.home_bottom_transaction, null)
            bottomSheetDialog.setContentView(view)

            val imgbottrtitle = view.findViewById<ImageView>(R.id.imgbottrtitle)
            val txtbottrtransfer = view.findViewById<TextView>(R.id.txtbottrtransfer)
            val txtbottrdate = view.findViewById<TextView>(R.id.txtbottrdate)
            val txtbottrfio = view.findViewById<TextView>(R.id.txtbottrfio)
            val txtbottrwallet = view.findViewById<TextView>(R.id.txtbottrwallet)
            val txtbottrcoment = view.findViewById<TextView>(R.id.txtbottrcoment)
            val txtbottrstatus = view.findViewById<TextView>(R.id.txtbottrstatus)

            if(imgindex == 0){
                imgbottrtitle.setImageResource(R.drawable.transactionfeild)
            }
            if(imgindex == 1){
                imgbottrtitle.setImageResource(R.drawable.transactionsend)
            }
            if(imgindex == 2){
                imgbottrtitle.setImageResource(R.drawable.transactionfeild)
            }
            if(imgindex == 3){
                imgbottrtitle.setImageResource(R.drawable.transactionsoucses)
            }
            if(imgindex == 4){
                imgbottrtitle.setImageResource(R.drawable.transferfeilde)
            }

            txtbottrtransfer.text = transferRequest.title
            txtbottrdate.text = transferRequest.datatransfer.replace(".","/")
            txtbottrfio.text = transferRequest.fio
            txtbottrwallet.text = transferRequest.wallet_to
            txtbottrcoment.text = transferRequest.comment
            if (transferRequest.status == "success"){
                txtbottrstatus.setTextColor(context.resources.getColor(R.color.green))
            }else{
                txtbottrstatus.setTextColor(context.resources.getColor(R.color.red))
            }
            txtbottrstatus.text = transferRequest.status


            bottomSheetDialog.show()
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