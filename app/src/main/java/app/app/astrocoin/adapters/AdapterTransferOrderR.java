package app.app.astrocoin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import app.app.astrocoin.R;
import app.app.astrocoin.models.TransferRequest;

public class AdapterTransferOrderR extends RecyclerView.Adapter<AdapterTransferOrderR.ViewHolder> {

    Context context;
    ArrayList<TransferRequest> transferRequestArrayList;

    public AdapterTransferOrderR(Context context, ArrayList<TransferRequest> transferRequestArrayList) {
        this.context = context;
        this.transferRequestArrayList = transferRequestArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transfer, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransferRequest transferRequest = transferRequestArrayList.get(position);
        holder.tranferdata.setText(transferRequest.getDatatransfer());
        holder.trtransfer.setText(transferRequest.getTitle());
        holder.trcoin.setText(transferRequest.getAmount());

        if (transferRequest.getDatatransfer().equals("") || transferRequest.getDatatransfer() == null) {
            holder.tranferdata.setVisibility(View.GONE);
        } else {
            holder.tranferdata.setVisibility(View.VISIBLE);
            holder.tranferdata.setText(transferRequest.getDatatransfer());
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("dycfvsufgjoafienvk.iso", Context.MODE_PRIVATE);
        String wallet = sharedPreferences.getString("wallet", "");

        if (Objects.equals(transferRequest.getStatus(), "success") && !Objects.equals(transferRequest.getWallet_to(), wallet)||
                Objects.equals(transferRequest.getStatus(), "failed") && Objects.equals(transferRequest.getWallet_to(), wallet)) {
            //holder.trimage.setImageResource(R.drawable.ic_component_3__1_);
            if (Objects.equals(transferRequest.getAmount(), "")) {
                holder.trcoin.setVisibility(View.GONE);
            } else {
                holder.trcoin.setText("-" + transferRequest.getAmount() + " ASC");
            }
        } else {
            if (Objects.equals(transferRequest.getStatus(), "failed") && !Objects.equals(transferRequest.getWallet_to(), wallet)) {
                //holder.trimage.setImageResource(R.drawable.ic_felid);
                if (Objects.equals(transferRequest.getAmount(), "")) {
                    holder.trcoin.setVisibility(View.GONE);
                } else {
                    holder.trcoin.setText(transferRequest.getAmount() + " ASC");
                }
            } else {
                if (Objects.equals(transferRequest.getStatus(), "success") && Objects.equals(transferRequest.getWallet_to(), wallet)) {
                    //holder.trimage.setImageResource(R.drawable.ic_soucsess);
                    if (Objects.equals(transferRequest.getAmount(), "")) {
                        holder.trcoin.setVisibility(View.GONE);
                    } else {
                        holder.trcoin.setText("+" + transferRequest.getAmount() + " ASC");
                    }
                } else {
                    //holder.trimage.setImageResource(R.drawable.ic_felid);
                    if (Objects.equals(transferRequest.getAmount(), "")) {
                        holder.trcoin.setVisibility(View.GONE);
                    } else {
                        holder.trcoin.setText(transferRequest.getAmount() + " ASC");
                    }
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return transferRequestArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trtransfer, trcoin, tranferdata;
        ShapeableImageView trimage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            trtransfer = itemView.findViewById(R.id.trtransfer);
            trcoin = itemView.findViewById(R.id.trcoin);
            tranferdata = itemView.findViewById(R.id.tranferdata);
            trimage = itemView.findViewById(R.id.trimage);

        }
    }
}
