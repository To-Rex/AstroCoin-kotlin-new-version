package app.app.astrocoin.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
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

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

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

    //settings view elements
    private var viewRanks: View? = null
    private var viewStore: View? = null
    private var viewChPass: View? = null
    private var viewApPas: View? = null
    private var viewLogout: View? = null


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
        viewLogout = view.findViewById(R.id.viewlogout)
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
        viewLogout?.setOnClickListener {
            Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            logOut()
        }

        igmSetCam?.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(requireContext(), this)
        }

    }
    //onActivityResult

    private fun getUsers() {
        val tokenResPonceCall = userService.userTokenRequest(
            "Bearer " + sharedPreferences?.getString(
                "token",
                ""
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
        if (user.verify == "1") {
            imgSetGall?.visibility = View.VISIBLE
        } else {
            imgSetGall?.visibility = View.GONE
        }
        Glide.with(requireContext()).load("https://api.astrocoin.uz" + user.photo).into(usImage!!)
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
        //your code
        bottomSheetDialogCamQr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.settings_bottom_renks, null)
        bottomSheetDialogCamQr?.setContentView(view)
        //your code
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
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(requireContext(), this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                try {
                    /*val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        resultUri
                    )*/
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        resultUri
                    )
                    usImage?.setImageBitmap(bitmap)
                    val file = File(Uri.parse(resultUri.toString()).path.toString())
                    val filePart = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    )
                    val call = userService.userSetPhoto("Bearer "+
                            sharedPreferences?.getString("token", ""), filePart)
                    call.enqueue(object : Callback<ImgUpload?> {
                        override fun onResponse(call: Call<ImgUpload?>, response: Response<ImgUpload?>) {
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
}