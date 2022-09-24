package app.app.astrocoin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
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
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


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
    private var surfaceView: SurfaceView? = null

    var wallet = ""
    private var requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector
    private var isFlashOn = false

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
        val view = layoutInflater.inflate(R.layout.home_bottom_qrscan, null)
        bottomSheetDialog.setContentView(view)
        //your code
        surfaceView = view.findViewById(R.id.surfaceView)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ackForCameraPermission()
        } else {
            setUpControls()
        }
        bottomSheetDialog.show()
    }

    private fun setUpControls(){
        detector = BarcodeDetector.Builder(requireActivity()).build()
        cameraSource = CameraSource.Builder(requireActivity(), detector)
            .setRequestedPreviewSize(640, 480)
            .setAutoFocusEnabled(true)
            .build()
        surfaceView?.holder?.addCallback(surgaceCallback)
        detector.setProcessor(processor)
    }
    private fun ackForCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            1
        )
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onRequestPermissionsResult(requestCode, permissions, grantResults)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                setUpControls()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val surgaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ackForCameraPermission()
                    return
                }
                surfaceView?.holder?.let { cameraSource.start(it) }
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

        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            if(detections.detectedItems.isNotEmpty()){
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                Toast.makeText(requireContext(), code.rawValue, Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), code.displayValue, Toast.LENGTH_SHORT).show()
                showBottomSheetDialogSend()
                txtHomeBalance?.text = code.rawValue
            }else{
                Toast.makeText(requireContext(), "No QR code detected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}