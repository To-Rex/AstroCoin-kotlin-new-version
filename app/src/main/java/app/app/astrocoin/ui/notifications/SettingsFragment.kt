package app.app.astrocoin.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.DownloadManager
import android.content.*
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import app.app.astrocoin.Login
import app.app.astrocoin.R
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.ImgUpload
import app.app.astrocoin.models.SetPassword
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient.userService
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.zxing.qrcode.QRCodeWriter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import kotlin.math.atan2
import kotlin.math.sqrt


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private var width: Int = 0
    private var height: Int = 0

    private var bottomSheetDialogCamQr: BottomSheetDialog? = null
    private var sharedPreferences: SharedPreferences? = null
    private var usImage: ShapeableImageView? = null
    private var txtSetFullName: TextView? = null
    private var txtSetEmail: TextView? = null
    private var txtSetQwaSar: TextView? = null
    private var txtSetStack: TextView? = null
    private var txtSetWallets: TextView? = null
    private var imgSetGall: ImageView? = null
    private var igmSetCam: ImageView? = null

    private var imageUri: Uri? = null
    private var photo = ""

    private var lastEvent: FloatArray? = null
    private var d = 0f
    private var newRot = 0f
    private var isOutSide = false
    private var mode = 0
    private val start = PointF()
    private val mid = PointF()
    private var oldDist = 1f
    private var xCoOrdinate = 0f
    private var yCoOrdinate: Float = 0f
    private var doubleClick = 0

    //settings view elements
    private var viewRanks: View? = null
    private var viewStore: View? = null
    private var viewChPass: View? = null
    private var viewApPas: View? = null
    private var viewWallet: View? = null
    private var viewLogout: View? = null
    private var viewSetBlock: View? = null


    //BotOomSheet change password
    private var ediBotCurPass: EditText? = null
    private var ediBotNewPass: EditText? = null
    private var ediBotRePass: EditText? = null
    private var btnBotSubPass: Button? = null

    //BotOomSheet change app password
    private var txtBotLogName: TextView? = null
    private var txtBotLogPas1: TextView? = null
    private var txtBotLogPas2: TextView? = null
    private var txtBotLogPas3: TextView? = null
    private var txtBotLogPas4: TextView? = null
    private var txtBotLogPas5: TextView? = null
    private var txtBotLogPas6: TextView? = null
    private var txtBotLogPas7: TextView? = null
    private var txtBotLogPas8: TextView? = null
    private var txtBotLogPas9: TextView? = null
    private var txtBotLogPas0: TextView? = null

    private var viewBotLogOne: View? = null
    private var viewBotLogTwo: View? = null
    private var viewBotLogThree: View? = null
    private var viewBotLogFour: View? = null

    private var imgBotLogBack: ImageView? = null

    private var index = 0
    private var password = ""
    private var savePassword = ""
    private var writePassword = ""
    private var checkNewPass = false
    private var click = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            requireContext().getString(R.string.astrocoin), Context.MODE_PRIVATE
        )

        txtSetFullName = view.findViewById(R.id.txtsetfullname)
        txtSetEmail = view.findViewById(R.id.txtsetemail)
        txtSetQwaSar = view.findViewById(R.id.txtsetqwasar)
        txtSetStack = view.findViewById(R.id.txtsetstack)
        txtSetWallets = view.findViewById(R.id.txtsetwallets)
        usImage = view.findViewById(R.id.usimage)
        imgSetGall = view.findViewById(R.id.imgsetgall)
        igmSetCam = view.findViewById(R.id.igmsetcam)

        viewRanks = view.findViewById(R.id.viewranks)
        viewStore = view.findViewById(R.id.viewstore)
        viewChPass = view.findViewById(R.id.viewchpass)
        viewApPas = view.findViewById(R.id.viewappas)
        viewWallet = view.findViewById(R.id.viewwallet)
        viewLogout = view.findViewById(R.id.viewlogout)
        viewSetBlock = view.findViewById(R.id.viewSetBlock)
        getUserData()
        getUsers()

        viewRanks?.setOnClickListener {
            bottomSheetRanks("https://astrocoin.uz/ranks")
        }
        viewStore?.setOnClickListener {
            bottomSheetRanks("https://store.astrocoin.uz/")
        }
        viewChPass?.setOnClickListener {
            bottomSheetChangePassword()
        }
        viewApPas?.setOnClickListener {
            bottomSheetAppPassword()
        }

        viewWallet?.setOnClickListener {
            doubleClick++
            Handler(Looper.getMainLooper()).postDelayed({
                if (doubleClick == 1) {
                    val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", txtSetWallets?.text.toString())
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(requireContext(), requireContext().getText(R.string.coPi), Toast.LENGTH_SHORT).show()
                    doubleClick = 0
                } else {
                    if (doubleClick >= 2) {
                        doubleClick = 0
                        showBottomSheetDialogReadQr()
                    }
                }
            }, 200)

        }

        viewLogout?.setOnClickListener {
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            logOut()
        }

        igmSetCam?.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .start(requireContext(), this)
        }
        usImage?.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.settings_user_photo)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            val setSHapImgUser: ShapeableImageView = dialog.findViewById(R.id.setSHapImgUser)
            val view30: View = dialog.findViewById(R.id.view30)

            width = Resources.getSystem().displayMetrics.widthPixels
            height = Resources.getSystem().displayMetrics.heightPixels
            view30.layoutParams.height = height
            view30.layoutParams.width = width

            view30.setOnClickListener {
                dialog.dismiss()
            }

            if (photo.isEmpty()) {
                setSHapImgUser.setImageResource(R.drawable.usericons)
            } else {
                Glide.with(requireContext()).load("https://api.astrocoin.uz$photo")
                    .into(setSHapImgUser)
            }
            setSHapImgUser.setOnTouchListener { v22: View, event: MotionEvent? ->
                val view1 = v22 as ImageView
                view1.bringToFront()
                viewTransformation(view1, event!!)
                true
            }
            setSHapImgUser.setOnLongClickListener {
                dialog.dismiss()
                dialog.show()
                true
            }
            view30.setOnClickListener {
                doubleClick++
                Handler(Looper.getMainLooper()).postDelayed({
                    if (doubleClick == 1) {
                        doubleClick = 0
                        dialog.dismiss()
                    } else {
                        if (doubleClick >= 2) {
                            doubleClick = 0
                            downloadImageNew("https://api.astrocoin.uz$photo")
                        }
                    }
                }, 200)
            }

            dialog.show()
        }
    }
    //onActivityResult

    private fun getUsers() {
        val tokenResPonceCall = userService.userTokenRequest(
            "Bearer " + sharedPreferences?.getString(
                "token", ""
            )
        )
        tokenResPonceCall.enqueue(object : Callback<TokenRequest> {
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
        password = sharedPreferences?.getString("password", "")!!
        txtSetFullName?.text = user.name + " " + user.last_name
        txtSetEmail?.text = user.email
        txtSetQwaSar?.text = user.qwasar
        txtSetStack?.text = user.stack
        txtSetWallets?.text = user.wallet
        photo = user.photo
        if(user.status == "1"){
            viewSetBlock?.visibility = View.GONE
        }else{
            viewSetBlock?.visibility = View.VISIBLE
        }
        if (user.verify == "1") {
            imgSetGall?.visibility = View.VISIBLE
        } else {
            imgSetGall?.visibility = View.GONE
        }
        if (photo.isEmpty()) {
            usImage?.setImageResource(R.drawable.usericons)
        } else {
            Glide.with(requireContext()).load("https://api.astrocoin.uz$photo")
                .into(usImage!!)
        }
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
        if (txtSetWallets?.text.toString().isNotEmpty()) {
            txtReadQrBottom.text = txtSetWallets?.text.toString()

            imgReadQrBottomIcon.setOnClickListener {
                val clipboard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", txtSetWallets?.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
            }

            btnReadQrBottom.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, txtSetWallets?.text.toString())
                startActivity(Intent.createChooser(intent, "Share via"))
            }

            val writTer = QRCodeWriter()
            val bitMatrix = writTer.encode(
                txtSetWallets?.text.toString(),
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
            Toast.makeText(requireContext(), requireContext().getString(R.string.coPi), Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

    }
    @SuppressLint("InflateParams")
    private fun bottomSheetChangePassword() {
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.settings_bottom_changep, null)
        bottomSheetDialogCamQr?.setContentView(view)
        //your code
        ediBotCurPass = view.findViewById(R.id.ediBotCurPass)
        ediBotNewPass = view.findViewById(R.id.ediBotNewPass)
        ediBotRePass = view.findViewById(R.id.ediBotRePass)
        btnBotSubPass = view.findViewById(R.id.btnBotSubPass)

        btnBotSubPass?.setOnClickListener {
            val password = ediBotCurPass?.text.toString()
            val newPassword = ediBotNewPass?.text.toString()
            val rePassword = ediBotRePass?.text.toString()
            if (password.isEmpty() || password.length < 7) {
                ediBotCurPass?.error = "Password is empty"
            } else if (newPassword.isEmpty() || newPassword.length < 7) {
                ediBotNewPass?.error = "New password is empty"
            } else if (rePassword.isEmpty() || rePassword.length < 7) {
                ediBotRePass?.error = "Re password is empty"
            } else if (newPassword != rePassword) {
                ediBotRePass?.error = "Re password is not match"
            } else {
                changePassword(SetPassword(password, newPassword, rePassword))
            }
        }

        bottomSheetDialogCamQr?.show()

    }


    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun bottomSheetAppPassword() {
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.settings_bottom_changeapp, null)
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext())
        bottomSheetDialogCamQr?.setContentView(view)
        //your code
        txtBotLogName = view.findViewById(R.id.txtBotLog_Name)
        txtBotLogPas1 = view.findViewById(R.id.txtBotLog_Pas1)
        txtBotLogPas2 = view.findViewById(R.id.txtBotLog_Pas2)
        txtBotLogPas3 = view.findViewById(R.id.txtBotLog_Pas3)
        txtBotLogPas4 = view.findViewById(R.id.txtBotLog_Pas4)
        txtBotLogPas5 = view.findViewById(R.id.txtBotLog_Pas5)
        txtBotLogPas6 = view.findViewById(R.id.txtBotLog_Pas6)
        txtBotLogPas7 = view.findViewById(R.id.txtBotLog_Pas7)
        txtBotLogPas8 = view.findViewById(R.id.txtBotLog_Pas8)
        txtBotLogPas9 = view.findViewById(R.id.txtBotLog_Pas9)
        txtBotLogPas0 = view.findViewById(R.id.txtBotLog_Pas0)

        viewBotLogOne = view.findViewById(R.id.viewBotLog_One)
        viewBotLogTwo = view.findViewById(R.id.viewBotLog_Two)
        viewBotLogThree = view.findViewById(R.id.viewBotLog_Three)
        viewBotLogFour = view.findViewById(R.id.viewBotLog_Four)

        imgBotLogBack = view.findViewById(R.id.imgBotLog_Back)

        //if txtBotLogPas1 to txtBotLogPas9 is clicked writePassword
        txtBotLogPas1?.setOnClickListener {
            if (click) {
                writePassword += "1"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas2?.setOnClickListener {
            if (click) {
                writePassword += "2"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas3?.setOnClickListener {
            if (click) {
                writePassword += "3"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas4?.setOnClickListener {
            if (click) {
                writePassword += "4"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas5?.setOnClickListener {
            if (click) {
                writePassword += "5"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas6?.setOnClickListener {
            if (click) {
                writePassword += "6"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas7?.setOnClickListener {
            if (click) {
                writePassword += "7"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas8?.setOnClickListener {
            if (click) {
                writePassword += "8"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas9?.setOnClickListener {
            if (click) {
                writePassword += "9"
                index++
                cheSkPassword()
            }
        }
        txtBotLogPas0?.setOnClickListener {
            if (click) {
                writePassword += "0"
                index++
                cheSkPassword()
            }
        }

        imgBotLogBack?.setOnClickListener {
            if (click) {
                index--
                checkIndexBack()
            }
        }

        bottomSheetDialogCamQr?.show()
    }

    private fun cheSkPassword() {
        if (index > 4) {
            index = 4
        }
        when (index) {
            1 -> {
                viewBotLogOne?.setBackgroundResource(R.drawable.pascheck)
            }
            2 -> {
                viewBotLogTwo?.setBackgroundResource(R.drawable.pascheck)
            }
            3 -> {
                viewBotLogThree?.setBackgroundResource(R.drawable.pascheck)
            }
            4 -> {
                viewBotLogFour?.setBackgroundResource(R.drawable.pascheck)
                //your code
                if (password == writePassword && !checkNewPass) {
                    click = false
                    viewBotLogOne?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogTwo?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogThree?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogFour?.setBackgroundResource(R.drawable.passoucses)
                    index = 0
                    writePassword = ""
                    checkNewPass = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
                        writePassword = ""
                        index = 0
                        txtBotLogName?.text = getString(R.string.new_password)
                        click = true
                    }, 800)
                    return
                } else if (password != writePassword && !checkNewPass) {
                    click = false
                    viewBotLogOne?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogTwo?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogThree?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogFour?.setBackgroundResource(R.drawable.passerror)
                    index = 0
                    writePassword = ""
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
                        writePassword = ""
                        index = 0
                        click = true
                    }, 800)
                }
                if (checkNewPass && savePassword.isEmpty()) {
                    click = false
                    index = 0
                    savePassword = writePassword
                    writePassword = ""
                    viewBotLogOne?.setBackgroundResource(R.drawable.homecardfon)
                    viewBotLogTwo?.setBackgroundResource(R.drawable.homecardfon)
                    viewBotLogThree?.setBackgroundResource(R.drawable.homecardfon)
                    viewBotLogFour?.setBackgroundResource(R.drawable.homecardfon)
                    Handler(Looper.getMainLooper()).postDelayed({
                        txtBotLogName?.text = getString(R.string.confirm_password)
                        viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
                        writePassword = ""
                        index = 0
                        click = true
                    }, 800)
                    return
                }
                if (savePassword.isNotEmpty() && checkNewPass && savePassword == writePassword) {
                    click = false
                    viewBotLogOne?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogTwo?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogThree?.setBackgroundResource(R.drawable.passoucses)
                    viewBotLogFour?.setBackgroundResource(R.drawable.passoucses)
                    index = 0
                    writePassword = ""
                    checkNewPass = false
                    sharedPreferences?.edit()?.putString("password", savePassword)?.apply()
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
                        writePassword = ""
                        index = 0
                        click = true
                        savePassword = ""
                        checkNewPass = false
                        bottomSheetDialogCamQr?.dismiss()
                        getUsers()
                        getUserData()
                    }, 800)
                } else if (savePassword.isNotEmpty() && checkNewPass && savePassword != writePassword) {
                    click = false
                    viewBotLogOne?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogTwo?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogThree?.setBackgroundResource(R.drawable.passerror)
                    viewBotLogFour?.setBackgroundResource(R.drawable.passerror)
                    index = 0
                    writePassword = ""
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
                        viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
                        writePassword = ""
                        index = 0
                        click = true
                    }, 800)
                }
            }
        }
    }

    private fun checkIndexBack() {
        if (index > 4) {
            index = 4
        }
        if (index < 0) {
            index = 0
        }
        if (index > 0) {
            writePassword = writePassword.substring(0, writePassword.length - 1)
        }
        when (index) {
            0 -> {
                viewBotLogOne?.setBackgroundResource(R.drawable.passsign)
            }
            1 -> {
                viewBotLogTwo?.setBackgroundResource(R.drawable.passsign)
            }
            2 -> {
                viewBotLogThree?.setBackgroundResource(R.drawable.passsign)
            }
            3 -> {
                viewBotLogFour?.setBackgroundResource(R.drawable.passsign)
            }
        }
    }

    private fun logOut() {
        //your code
        val logOutResPonceCall = userService.userLogOut(
            "Bearer " + sharedPreferences?.getString("token", "")
        )
        logOutResPonceCall.enqueue(object : Callback<Any> {
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
    private fun bottomSheetRanks(link: String) {
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

    private fun changePassword(setPassword: SetPassword) {
        //your code
        val tokenResPonceCall = userService.userChangePassword(
            "Bearer " + sharedPreferences?.getString("token", ""), setPassword
        )
        tokenResPonceCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Toast.makeText(requireContext(), "Password changed", Toast.LENGTH_SHORT)
                            .show()
                        bottomSheetDialogCamQr?.dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(), "Error password not changed", Toast.LENGTH_SHORT
                        ).show()
                        ediBotCurPass?.error = "Password is not correct"
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                ediBotCurPass?.error = null
                                ediBotNewPass?.text?.clear()
                                ediBotRePass?.text?.clear()
                            }, 2000
                        )
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Error password not changed", Toast.LENGTH_SHORT
                    ).show()
                    ediBotCurPass?.error = "Password is not correct"
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            ediBotCurPass?.error = null
                            ediBotNewPass?.text?.clear()
                            ediBotRePass?.text?.clear()
                        }, 2000
                    )
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                .start(requireContext(), this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, resultUri
                    )
                    usImage?.setImageBitmap(bitmap)
                    val file = File(Uri.parse(resultUri.toString()).path.toString())
                    val filePart = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    )
                    val call = userService.userSetPhoto(
                        "Bearer " + sharedPreferences?.getString("token", ""), filePart
                    )
                    call.enqueue(object : Callback<ImgUpload?> {
                        override fun onResponse(
                            call: Call<ImgUpload?>, response: Response<ImgUpload?>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT)
                                    .show()
                                getUsers()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    getUserData()
                                }, 1500)
                                call.cancel()
                            } else {
                                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT)
                                    .show()
                                call.cancel()
                            }
                        }

                        override fun onFailure(call: Call<ImgUpload?>, t: Throwable) {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            call.cancel()
                        }
                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun downloadImageNew(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString() + ".png"
        )
        val manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
    private fun viewTransformation(view: View, event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.x - event.rawX
                yCoOrdinate = view.y - event.rawY
                start[event.x] = event.y
                isOutSide = false
                mode = 1
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    midPoint(mid, event)
                    mode = 2
                }
                lastEvent = FloatArray(4)
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
                d = rotation(event)
            }
            MotionEvent.ACTION_UP -> {
                if (mode == 1) {
                    if (isOutSide) {
                        view.x = 0f
                        view.y = 0f
                    }
                }
                isOutSide = true
                mode = 0
                lastEvent = null
            }
            MotionEvent.ACTION_OUTSIDE -> {
                isOutSide = true
                mode = 0
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = 0
                lastEvent = null
            }
            MotionEvent.ACTION_MOVE -> if (!isOutSide) {
                if (mode == 1) {
                    view.animate().x(event.rawX + xCoOrdinate).y(event.rawY + yCoOrdinate)
                        .setDuration(0).start()
                }
                if (mode == 2 && event.pointerCount == 2) {
                    val newDist1 = spacing(event)
                    if (newDist1 > 10f) {
                        val scale = newDist1 / oldDist * view.scaleX
                        view.scaleX = scale
                        view.scaleY = scale
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event)
                        view.rotation = view.rotation + (newRot - d)
                    }
                }
            }
        }
    }

    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(deltaY, deltaX)
        return Math.toDegrees(radians).toFloat()
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toInt().toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }
}