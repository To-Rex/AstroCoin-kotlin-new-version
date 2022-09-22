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
import com.google.android.material.imageview.ShapeableImageView
import java.util.ArrayList

class AdapterTransferR(
    var context: Context,
    var transferRequestArrayList: ArrayList<TransferRequest>
) : RecyclerView.Adapter<AdapterTransferR.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transfer, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transferRequest = transferRequestArrayList[position]
        holder.tranferdata.text = transferRequest.datatransfer
        holder.trtransfer.text = transferRequest.title
        holder.trcoin.text = transferRequest.amount
        if (transferRequest.datatransfer == "" || transferRequest.datatransfer == null) {
            holder.tranferdata.visibility = View.GONE
        } else {
            holder.tranferdata.visibility = View.VISIBLE
            holder.tranferdata.text = transferRequest.datatransfer
        }
        val sharedPreferences =
            context.getSharedPreferences("dycfvsufgjoafienvk.iso", Context.MODE_PRIVATE)
        val wallet = sharedPreferences.getString("wallet", "")
        if (transferRequest.status == "success" && transferRequest.wallet_to != wallet ||
            transferRequest.status == "failed" && transferRequest.wallet_to == wallet
        ) {
            //holder.trimage.setImageResource(R.drawable.ic_component_3__1_);
            if (transferRequest.amount == "") {
                holder.trcoin.visibility = View.GONE
            } else {
                holder.trcoin.text = "-" + transferRequest.amount + " ASC"
            }
        } else {
            if (transferRequest.status == "failed" && transferRequest.wallet_to != wallet) {
                //holder.trimage.setImageResource(R.drawable.ic_felid);
                if (transferRequest.amount == "") {
                    holder.trcoin.visibility = View.GONE
                } else {
                    holder.trcoin.text = transferRequest.amount + " ASC"
                }
            } else {
                if (transferRequest.status == "success" && transferRequest.wallet_to == wallet) {
                    //holder.trimage.setImageResource(R.drawable.ic_soucsess);
                    if (transferRequest.amount == "") {
                        holder.trcoin.visibility = View.GONE
                    } else {
                        holder.trcoin.text = "+" + transferRequest.amount + " ASC"
                    }
                } else {
                    //holder.trimage.setImageResource(R.drawable.ic_felid);
                    if (transferRequest.amount == "") {
                        holder.trcoin.visibility = View.GONE
                    } else {
                        holder.trcoin.text = transferRequest.amount + " ASC"
                    }
                }
            }
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
}