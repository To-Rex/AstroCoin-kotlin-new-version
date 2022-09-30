package app.app.astrocoin.adapters

import app.app.astrocoin.models.TransferRequest
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import app.app.astrocoin.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.app.astrocoin.models.Getdata
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import java.util.ArrayList

class AdapterTransferR(
    var context: Context,
    private var transferRequestArrayList: ArrayList<TransferRequest>


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
        holder.tranFerData.text = transferRequest.datatransfer
        holder.trTransfer.text = transferRequest.title
        var imgIndex = 0


        if (transferRequest.datatransfer == "") {
            holder.tranFerData.visibility = View.GONE
        } else {
            holder.tranFerData.visibility = View.VISIBLE
            holder.tranFerData.text = transferRequest.datatransfer
        }

        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.astrocoin),
            Context.MODE_PRIVATE
        )
        getUserData()

        if (transferRequest.status == "returned") {
            if (transferRequest.amount == "") {
                holder.trCoin.visibility = View.VISIBLE
            } else {
                imgIndex = 1
                holder.trCoin.text = transferRequest.amount.replace(".0", "") + " ASC"
                holder.trImage.setImageResource(R.drawable.transactionsend)
            }
        }

        if (transferRequest.status == "failed" && transferRequest.wallet_to != wallet) {
            if (transferRequest.amount == "") {
                holder.trCoin.visibility = View.VISIBLE
            } else {
                imgIndex = 2
                holder.trCoin.text = transferRequest.amount.replace(".0", "")  + " ASC"
                holder.trImage.setImageResource(R.drawable.transactionfeild)
            }
        }

        if (transferRequest.status == "success" && transferRequest.wallet_to == wallet) {
            if (transferRequest.amount == "") {
                holder.trCoin.visibility = View.VISIBLE
            } else {
                imgIndex = 3
                holder.trCoin.text = "+"+transferRequest.amount.replace(".0", "")  + " ASC"
                holder.trImage.setImageResource(R.drawable.transactionsoucses)
            }

        }

        if (transferRequest.status == "success" && transferRequest.wallet_to != wallet) {
            if (transferRequest.amount == "") {
                holder.trCoin.visibility = View.VISIBLE
            } else {
                imgIndex = 4
                holder.trCoin.text = "-"+transferRequest.amount.replace(".0", "")  + " ASC"
                holder.trImage.setImageResource(R.drawable.transferfeilde)
            }
        }

        holder.itemView.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.custombottomsheet)
            val view = LayoutInflater.from(context).inflate(R.layout.home_bottom_transaction, null)
            bottomSheetDialog.setContentView(view)

            val imgBotTrTitle = view.findViewById<ImageView>(R.id.imgbottrtitle)
            val txtBotTrTransfer = view.findViewById<TextView>(R.id.txtbottrtransfer)
            val txtBotTrDate = view.findViewById<TextView>(R.id.txtbottrdate)
            val txtBotTrFio = view.findViewById<TextView>(R.id.txtbottrfio)
            val txtBotTrWallet = view.findViewById<TextView>(R.id.txtbottrwallet)
            val txtBotTrComEnt = view.findViewById<TextView>(R.id.txtbottrcoment)
            val txtOtTrStatus = view.findViewById<TextView>(R.id.txtbottrstatus)

            if (imgIndex == 0) {
                imgBotTrTitle.setImageResource(R.drawable.transactionfeild)
            }
            if (imgIndex == 1) {
                imgBotTrTitle.setImageResource(R.drawable.transactionsend)
            }
            if (imgIndex == 2) {
                imgBotTrTitle.setImageResource(R.drawable.transactionfeild)
            }
            if (imgIndex == 3) {
                imgBotTrTitle.setImageResource(R.drawable.transactionsoucses)
            }
            if (imgIndex == 4) {
                imgBotTrTitle.setImageResource(R.drawable.transferfeilde)
            }

            txtBotTrTransfer.text = transferRequest.title
            txtBotTrDate.text = transferRequest.date.replace("-", "/").
                replace("T", " - ").split(".")[0]
            txtBotTrFio.text = transferRequest.fio
            txtBotTrWallet.text = transferRequest.wallet_to
            txtBotTrComEnt.text = transferRequest.comment
            if (transferRequest.status == "success") {
                txtOtTrStatus.setTextColor(Color.parseColor("#00C853"))
            } else {
                txtOtTrStatus.setTextColor(Color.parseColor("#FF0000"))
            }
            txtOtTrStatus.text = transferRequest.status


            bottomSheetDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return transferRequestArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trTransfer: TextView
        var trCoin: TextView
        var tranFerData: TextView
        var trImage: ShapeableImageView

        init {
            trTransfer = itemView.findViewById(R.id.trtransfer)
            trCoin = itemView.findViewById(R.id.trcoin)
            tranFerData = itemView.findViewById(R.id.tranferdata)
            trImage = itemView.findViewById(R.id.trimage)
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