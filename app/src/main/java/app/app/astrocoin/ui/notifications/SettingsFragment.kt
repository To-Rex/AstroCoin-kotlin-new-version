package app.app.astrocoin.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.app.astrocoin.Login
import app.app.astrocoin.R
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private var bottomSheetDialogCamQr: BottomSheetDialog? = null
    private var bottomSheetChangePass: BottomSheetDialog? = null
    private var sharedPreferences: SharedPreferences? = null
    private var usImage: ShapeableImageView? = null
    private var txtSetFullName: TextView? = null
    private var txtSetEmail: TextView? = null
    private var txtSetQwaSar: TextView? = null
    private var txtSetStack: TextView? = null
    private var txtSetWallets: TextView? = null
    private var imgSetGall: ImageView? = null

    private var viewRanks: View? = null
    private var viewstore: View? = null
    private var viewchpass: View? = null
    private var viewappas: View? = null
    private var viewlogout: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(requireContext().getString(R.string.astrocoin), Context.MODE_PRIVATE)

        txtSetFullName = view.findViewById(R.id.txtsetfullname)
        txtSetEmail = view.findViewById(R.id.txtsetemail)
        txtSetQwaSar = view.findViewById(R.id.txtsetqwasar)
        txtSetStack = view.findViewById(R.id.txtsetstack)
        txtSetWallets = view.findViewById(R.id.txtsetwallets)
        usImage = view.findViewById(R.id.usimage)
        imgSetGall = view.findViewById(R.id.imgsetgall)

        viewRanks = view.findViewById(R.id.viewranks)
        viewstore = view.findViewById(R.id.viewstore)
        viewchpass = view.findViewById(R.id.viewchpass)
        viewappas = view.findViewById(R.id.viewappas)
        viewlogout = view.findViewById(R.id.viewlogout)
        getUserData()
        getUsers()

        viewRanks?.setOnClickListener {
            bottomSheetRanks("https://astrocoin.uz/ranks")
        }
        viewstore?.setOnClickListener {
            bottomSheetRanks("https://store.astrocoin.uz/")
        }
        viewchpass?.setOnClickListener {
            bottomSheetChangePassword()
        }
        viewappas?.setOnClickListener {
            bottomSheetAppPassword()
        }
        viewlogout?.setOnClickListener {
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            logOut()
        }

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

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        txtSetFullName?.text = user.name+" "+user.last_name
        txtSetEmail?.text = user.email
        txtSetQwaSar?.text = user.qwasar
        txtSetStack?.text = user.stack
        txtSetWallets?.text = user.wallet
        if (user.verify == "1"){
            imgSetGall?.visibility = View.VISIBLE
        }else{
            imgSetGall?.visibility = View.GONE
        }
        Glide.with(requireContext()).load("https://api.astrocoin.uz" + user.photo).into(usImage!!)
    }

    @SuppressLint("InflateParams")
    private fun  bottomSheetChangePassword() {
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.settings_bottom_changep, null)
        bottomSheetDialogCamQr?.setContentView(view)
        bottomSheetDialogCamQr?.show()

    }
    @SuppressLint("InflateParams")
    private fun  bottomSheetAppPassword() {
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.fragment_home, null)
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext())
        bottomSheetDialogCamQr?.setContentView(view)
        bottomSheetDialogCamQr?.show()
    }

    private fun  logOut() {
        val logOutResPonceCall = ApiClient.userService.userLogOut("Bearer " + sharedPreferences?.getString("token", ""))
        logOutResPonceCall.enqueue(object : retrofit2.Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                sharedPreferences?.edit()?.clear()?.apply()
                startActivity(Intent(requireContext(), Login::class.java))
                activity?.finish()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })

    }

    @SuppressLint("InflateParams", "SetJavaScriptEnabled", "MissingInflatedId")
    private fun bottomSheetRanks(link : String){
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.settings_bottom_renks, null)
        bottomSheetDialogCamQr?.setContentView(view)
        val progressRank = view.findViewById<ProgressBar>(R.id.progressRank)

        val webViewSetRank = view.findViewById<WebView>(R.id.webViewsetRank)
        webViewSetRank.loadUrl(link)
        webViewSetRank.settings.javaScriptEnabled = true
        webViewSetRank.settings.domStorageEnabled = true
        webViewSetRank.settings.databaseEnabled = true
        webViewSetRank.settings.setSupportZoom(true)
        webViewSetRank.settings.builtInZoomControls = true
        webViewSetRank.settings.displayZoomControls = false
        webViewSetRank.settings.useWideViewPort = true
        webViewSetRank.settings.loadWithOverviewMode = true
        webViewSetRank.settings.setSupportMultipleWindows(true)
        webViewSetRank.settings.allowFileAccess = true
        webViewSetRank.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webViewSetRank.settings.setGeolocationEnabled(true)
        webViewSetRank.settings.loadsImagesAutomatically = true
        webViewSetRank.settings.defaultTextEncodingName = "utf-8"
        webViewSetRank.settings.defaultFontSize = 16
        webViewSetRank.settings.setNeedInitialFocus(true)

        bottomSheetDialogCamQr?.show()
        Handler(Looper.getMainLooper()).postDelayed({
            progressRank.visibility = View.GONE
        }, 3000)
    }
}