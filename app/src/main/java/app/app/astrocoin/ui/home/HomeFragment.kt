package app.app.astrocoin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import app.app.astrocoin.R
import app.app.astrocoin.adapters.TabAdapters
import app.app.astrocoin.fragments.FragmentOrder
import app.app.astrocoin.fragments.FragmentTransfers
import app.app.astrocoin.models.CheckWallet
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.SendTransferRequest
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient
import app.app.astrocoin.sampleclass.ApiClient.userService
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class HomeFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var bottomSheetDialogcamqr: BottomSheetDialog? = null

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var tabAdapters: TabAdapters? = null

    private var txtHomeBalance: TextView? = null
    private var imgHomeReadQr: ImageView? = null
    private var imgHomeSendWallet: ImageView? = null
    private var imgHomeScanQr: ImageView? = null
    private var surfaceView: SurfaceView? = null

    var wallet = ""
    private var requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

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
        imgHomeScanQr = view.findViewById(R.id.imghomescanqr)

        sharedPreferences =
            requireActivity().getSharedPreferences(
                getString(R.string.astrocoin),
                Context.MODE_PRIVATE
            )
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

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun showBottomSheetDialogSend() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.home_bottom_send, null)
        bottomSheetDialog.setContentView(view)

        val edibotsendwaladress = view.findViewById<EditText>(R.id.edibotsendwaladress)
        val txtbotsendfio = view.findViewById<TextView>(R.id.txtbotsendfio)
        val edibotsendwallet = view.findViewById<EditText>(R.id.edibotsendwallet)
        val edibotdendcoment = view.findViewById<TextInputEditText>(R.id.edibotdendcoment)
        val btnbotsend = view.findViewById<Button>(R.id.btnbotsend)
        val imgbotsendpaste = view.findViewById<ImageView>(R.id.imgbotsendpast)

        edibotsendwaladress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && s.toString().length > 29) {
                    val checkWallet = CheckWallet(s.toString())
                    val walletUserNameCall = ApiClient.userService.userWalletname(
                        "Bearer " + sharedPreferences?.getString(
                            "token",
                            ""
                        ), checkWallet
                    )
                    walletUserNameCall.enqueue(object : retrofit2.Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                val walletName = response.body()
                                if (walletName != null) {
                                    txtbotsendfio.text =
                                        walletName.toString().split("=")[1].replace("}", "")
                                }
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            if (s.toString().length > 31) {
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        imgbotsendpaste.setOnClickListener {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null) {
                val item = clip.getItemAt(0)
                edibotsendwaladress.setText(item.text.toString())
            }
        }
        btnbotsend.setOnClickListener {
            val walletadress = edibotsendwaladress.text.toString()
            val wallet = edibotsendwallet.text.toString()
            val comment = edibotdendcoment.text.toString()
            if (walletadress.isEmpty()){
                edibotsendwaladress.error = "Enter wallet address"
                return@setOnClickListener
            }
            if (wallet.isEmpty()){
                edibotsendwallet.error = "Enter amount"
                return@setOnClickListener
            }

            val sendTransferRequest = SendTransferRequest()
            sendTransferRequest.wallet_to = walletadress
            val amount: Double = wallet.toDouble()
            sendTransferRequest.amount = amount
            sendTransferRequest.comment = comment
            sendTransferRequest.type = ""
            sendTransferRequest.title = ""
            val call: Call<Any> = userService.sendTransfers(
                "Bearer " + sharedPreferences?.getString("token", ""), sendTransferRequest)
            call.enqueue(object : retrofit2.Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
        bottomSheetDialog.show()
    }

    @SuppressLint("InflateParams", "MissingInflatedId", "ServiceCast")
    private fun showBottomSheetDialogCamQr() {
        bottomSheetDialogcamqr = BottomSheetDialog(requireContext(), R.style.custombottomsheet)
        val view = layoutInflater.inflate(R.layout.home_bottom_qrscan, null)
        bottomSheetDialogcamqr?.setContentView(view)

        surfaceView = view.findViewById(R.id.cameraSurfaceView)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ackForCameraPermission()
        } else {
            setUpControls()
        }
        bottomSheetDialogcamqr?.show()
    }

    //qr scan code
    private fun setUpControls() {
        detector = BarcodeDetector.Builder(requireContext()).build()
        cameraSource = CameraSource.Builder(requireContext(), detector)
            .setRequestedPreviewSize(640, 480)
            .setAutoFocusEnabled(true)
            .build()
        surfaceView?.holder?.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun ackForCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpControls()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {
        @SuppressLint("MissingPermission")
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(surfaceView?.holder!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }
    }
    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {
        }

        @SuppressLint("ServiceCast")
        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            val qrCodes = detections.detectedItems
            if (qrCodes.size() != 0) {
                bottomSheetDialogcamqr?.dismiss()
                println(qrCodes.valueAt(0).displayValue)
            }
        }
    }
}