package app.app.astrocoin.ui.home

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import app.app.astrocoin.R
import app.app.astrocoin.adapters.TabAdapters
import app.app.astrocoin.fragments.FragmentOrder
import app.app.astrocoin.fragments.FragmentTransfers
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null


    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var tabAdapters: TabAdapters? = null

    private var txtHomeBalance: TextView? = null
    private var imgHomeReadQr: ImageView? = null
    private var imgHomeSendWallet: ImageView? = null
    private var imgHomeScanQr: ImageView? = null

    var wallet = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        txtHomeBalance = view.findViewById(R.id.txthomebalance)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        imgHomeReadQr = view.findViewById(R.id.imghomereadqr)
        imgHomeSendWallet = view.findViewById(R.id.imghomesendwallet)

        sharedPreferences =
            requireActivity().getSharedPreferences(getString(R.string.astrocoin), Context.MODE_PRIVATE)
        swipeRefreshLayout!!.setOnRefreshListener {
            toLsAllFun()
            getUsers()
            swipeRefreshLayout!!.isRefreshing = false
        }
        toLsAllFun()

        imgHomeReadQr!!.setOnClickListener {
            showBottomSheetDialogReadQr()
        }
        imgHomeSendWallet!!.setOnClickListener {
            showBottomSheetDialogSend()
        }
        imgHomeScanQr!!.setOnClickListener {
            showBottomSheetDialogCamQr()
        }
    }

    private fun toLsAllFun() {
        tabAdapters = TabAdapters(childFragmentManager)
        tabAdapters?.addFragment(FragmentTransfers())
        tabAdapters?.addFragment(FragmentOrder())

        viewPager!!.adapter = tabAdapters
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setTabTextColors(Color.parseColor("#C9C9C9"), Color.parseColor("#5733D1"))
        tabLayout!!.setSelectedTabIndicatorColor(Color.parseColor("#5733D1"))

        tabLayout?.getTabAt(0)!!.text = getString(R.string.transfers)
        tabLayout?.getTabAt(1)!!.text = getString(R.string.orders)
        getUserData()
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        txtHomeBalance!!.text = user.balance + " ASC"
        wallet = user.wallet
    }

    private fun getUsers() {
        val tokenResPonceCall = ApiClient.userService
            .userTokenRequest("Bearer " + sharedPreferences?.getString("token", ""))
        tokenResPonceCall.enqueue(object : retrofit2.Callback<TokenRequest> {
            override fun onResponse(call: Call<TokenRequest>, response: Response<TokenRequest>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val gson = Gson()
                        val json = gson.toJson(loginResponse)
                        val editor = sharedPreferences?.edit()
                        editor?.putString("user", json)
                        editor?.apply()
                        getUserData()
                    }
                }
            }

            override fun onFailure(call: Call<TokenRequest>, t: Throwable) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun showBottomSheetDialogReadQr() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.home_bottom_qrcode, null)
        bottomSheetDialog.setContentView(view)
        val imgReadQrBottom = view.findViewById<ImageView>(R.id.imgreadqrbottom)
        val txtReadQrBottom = view.findViewById<TextView>(R.id.txtreadqrbottom)
        val imgReadQrBottomIcon = view.findViewById<ImageView>(R.id.imgreadqrbottomicon)
        val btnReadQrBottom = view.findViewById<Button>(R.id.btnreadqrbottom)
        if (wallet.isNotEmpty()) {
            txtReadQrBottom.text = wallet

            imgReadQrBottomIcon.setOnClickListener {
                val clipboard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", wallet)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
            }

            btnReadQrBottom.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, wallet)
                startActivity(Intent.createChooser(intent, "Share via"))
            }

            val writTer = QRCodeWriter()
            val bitMatrix = writTer.encode(
                wallet,
                com.google.zxing.BarcodeFormat.QR_CODE,
                512,
                512
            )
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = android.graphics.Bitmap.createBitmap(
                width,
                height,
                android.graphics.Bitmap.Config.RGB_565
            )
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            imgReadQrBottom.setImageBitmap(bmp)
            bottomSheetDialog.show()

        } else {
            Toast.makeText(requireContext(), "Wallet is empty", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialogSend() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.home_bottom_send, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialogCamQr() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.home_bottom_qrcode, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}