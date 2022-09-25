package app.app.astrocoin.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import app.app.astrocoin.R;
import app.app.astrocoin.models.CheckWallet;
import app.app.astrocoin.models.SendTransferRequest;
import app.app.astrocoin.models.UserRequest;
import app.app.astrocoin.sampleclass.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterUserSearch extends BaseAdapter implements Filterable {
    SharedPreferences sharedPreferences;
    BottomSheetDialog bottomSheetDialogCamQr;
    private final List<UserRequest> itemsModelsl;
    private List<UserRequest> itemsModelListFiltered;
    private final Context context;
    String token = "";
    private ClipboardManager clipboard;
    ImageView photoview2;
    ImageView imggall;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private final PointF start = new PointF();
    private final PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;

    public AdapterUserSearch(List<UserRequest> itemsModelsl, Context context) {
        this.itemsModelsl = itemsModelsl;
        this.itemsModelListFiltered = itemsModelsl;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_searchuser, parent, false);
        }
        UserRequest dataModal = (UserRequest) getItem(position);

        TextView nameTV = listItemView.findViewById(R.id.ustransfer);
        TextView idtxt = listItemView.findViewById(R.id.uscoin);
        imggall = listItemView.findViewById(R.id.imggall);

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.astrocoin), Context.MODE_PRIVATE);

        ShapeableImageView courseIV = listItemView.findViewById(R.id.usimage);
        if (dataModal.getVerify().equals("1.0")) {
            imggall.setVisibility(View.VISIBLE);
        } else {
            imggall.setVisibility(View.INVISIBLE);
        }

        if (dataModal.getName().length() > 12 || dataModal.getLast_name().length() > 12) {
            assert nameTV != null;
            nameTV.setText(dataModal.getName());
        } else {
            assert nameTV != null;
            nameTV.setText(dataModal.getName());
        }
        String ccoin = dataModal.getBalance().split("\\.0")[0];

        if (ccoin.length() > 0) {
            int coiint = Integer.parseInt(ccoin);
            if (coiint < 0) {
                idtxt.setText("-" + dataModal.getBalance().split("\\.0")[0] + " ASC");
            } else {
                if (coiint == 0) {
                    idtxt.setText("0" + " ASC");
                } else {
                    idtxt.setText("+" + dataModal.getBalance().split("\\.0")[0] + " ASC");
                }
            }
        } else {
            idtxt.setText("0" + " ASC");
        }

        String image = dataModal.getPhoto();

        if (Objects.equals(dataModal.getPhoto(), "")) {
            courseIV.setImageResource(R.drawable.usericons);
        } else {
            dataModal.getPhoto();
            Glide.with(context).load("https://api.astrocoin.uz" + image).into(courseIV);
        }

        //user sellected bottom sheet

        listItemView.setOnClickListener(v -> {
            bottomSheetDialogCamQr = new BottomSheetDialog(context, R.style.custombottomsheet);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.search_bottom_sheet, null);
            bottomSheetDialogCamQr.setContentView(view);
            //your code here
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView txtSetFio = view.findViewById(R.id.txtSeaFio),
                    txtSetStack = view.findViewById(R.id.txtSeaStack),
                    txtSeaCoins = view.findViewById(R.id.txtSeaCoins),
                    txtSeaQwName = view.findViewById(R.id.txtSeaQwName),
                    txtSeaWallets = view.findViewById(R.id.txtSeaWallets);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            ShapeableImageView imgShapeSea = view.findViewById(R.id.imgShapeSea);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            ImageView imgSeaGalley = view.findViewById(R.id.imgSeaGall);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            View viewSeaSendCoin = view.findViewById(R.id.viewSeaSendCoin);

            if (dataModal.getVerify().equals("1.0")) {
                imgSeaGalley.setVisibility(View.VISIBLE);
            } else {
                imgSeaGalley.setVisibility(View.INVISIBLE);
            }
            if (!dataModal.getPhoto().equals("")) {
                Glide.with(context).load("https://api.astrocoin.uz" + dataModal.getPhoto()).into(imgShapeSea);
            }
            txtSetFio.setText(dataModal.getName() + " " + dataModal.getLast_name());
            txtSetStack.setText(dataModal.getStack());
            txtSeaCoins.setText(dataModal.getBalance().split("\\.0")[0] + " ASC");
            txtSeaQwName.setText(dataModal.getQwasar());
            txtSeaWallets.setText(dataModal.getWallet());

            viewSeaSendCoin.setOnClickListener(v2-> showBottomSheetDialogSend(dataModal.getWallet()));
            txtSeaWallets.setOnLongClickListener(v1 -> {
                clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", txtSeaWallets.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "copy wallet id", Toast.LENGTH_SHORT).show();
                return false;
            });

            bottomSheetDialogCamQr.show();
        });
        return listItemView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemsModelListFiltered = itemsModelsl;
                } else {
                    List<UserRequest> filteredList = new ArrayList<>();
                    for (UserRequest row : itemsModelsl) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getLast_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getStack().toLowerCase().contains(charString.toLowerCase())
                                || row.getQwasar().toLowerCase().contains(charString.toLowerCase())
                                || row.getStatus().toLowerCase().contains(charString.toLowerCase())
                                || row.getVerify().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    itemsModelListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsModelListFiltered = (ArrayList<UserRequest>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage) {
        try {
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(context, "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation(view.getRotation() + (newRot - d));
                        }
                    }
                }
                break;
        }
    }

    private void showBottomSheetDialogSend(String wallet) {
        bottomSheetDialogCamQr = new BottomSheetDialog(context, R.style.custombottomsheet);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.home_bottom_send, null);
        bottomSheetDialogCamQr.setContentView(view);

        EditText ediBotSendWalAdrEss = view.findViewById(R.id.edibotsendwaladress);
        TextView txtBotSendFio = view.findViewById(R.id.txtbotsendfio);
        EditText ediBotSendWallet = view.findViewById(R.id.edibotsendwallet);
        TextInputEditText ediBotEndComEnt = view.findViewById(R.id.edibotdendcoment);
        Button btnBotSend = view.findViewById(R.id.btnbotsend);
        ImageView imgBotSendPaste = view.findViewById(R.id.imgbotsendpast);

        ediBotSendWalAdrEss.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 29) {
                    CheckWallet checkWallet = new CheckWallet(s.toString());
                    Call<Object> walletUserNameCall = ApiClient.INSTANCE.getUserService().userWalletname("Bearer " + sharedPreferences.getString("token", ""), checkWallet);
                    walletUserNameCall.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                            if (response.isSuccessful()) {
                                Object walletName = response.body();
                                if (walletName != null) {
                                    txtBotSendFio.setText(walletName.toString().split("=")[1].replace("}", ""));
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                            if (s.toString().length() > 31) {
                                Toast.makeText(context, "Error transfer", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ediBotSendWalAdrEss.setText(wallet);
        imgBotSendPaste.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {
                ClipData.Item item = clip.getItemAt(0);
                ediBotSendWalAdrEss.setText(item.getText().toString());
            }
        });

        btnBotSend.setOnClickListener(v -> {
            String walletAdEss = ediBotSendWalAdrEss.getText().toString();
            String wallets = ediBotSendWallet.getText().toString();
            String comment = Objects.requireNonNull(ediBotEndComEnt.getText()).toString();
            if (walletAdEss.isEmpty()) {
                ediBotSendWalAdrEss.setError("Enter wallet address");
                return;
            }
            if (wallets.isEmpty()) {
                ediBotSendWallet.setError("Enter amount");
                return;
            }

            SendTransferRequest sendTransferRequest = new SendTransferRequest();
            sendTransferRequest.setWallet_to(walletAdEss);
            double amount = Double.parseDouble(wallets);
            sendTransferRequest.setAmount(amount);
            sendTransferRequest.setComment(comment);
            sendTransferRequest.setType("");
            sendTransferRequest.setTitle("");
            Call<Object> call = ApiClient.INSTANCE.getUserService().sendTransfers("Bearer " + sharedPreferences.getString("token", ""), sendTransferRequest);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        bottomSheetDialogCamQr.dismiss();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
        bottomSheetDialogCamQr.show();
    }


    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
