package app.app.astrocoin.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.app.astrocoin.R
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.bumptech.glide.Glide
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

    private var sharedPreferences: SharedPreferences? = null
    private var usImage: ShapeableImageView? = null
    private var txtSetFullName: TextView? = null
    private var txtSetEmail: TextView? = null
    private var txtSetQwaSar: TextView? = null
    private var txtSetStack: TextView? = null
    private var txtSetWallets: TextView? = null
    private var imgSetGall: ImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences(requireContext().getString(R.string.astrocoin), Context.MODE_PRIVATE)

        txtSetFullName = view.findViewById(R.id.txtsetfullname)
        txtSetEmail = view.findViewById(R.id.txtsetemail)
        txtSetQwaSar = view.findViewById(R.id.txtsetqwasar)
        txtSetStack = view.findViewById(R.id.txtsetstack)
        txtSetWallets = view.findViewById(R.id.txtsetwallets)
        usImage = view.findViewById(R.id.usimage)
        imgSetGall = view.findViewById(R.id.imgsetgall)
        getUserData()
        getUsers()

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
        println(user.verify)
        if (user.verify == "1"){
            imgSetGall?.visibility = View.VISIBLE
        }else{
            imgSetGall?.visibility = View.GONE
        }
        Glide.with(requireContext()).load("https://api.astrocoin.uz" + user.photo).into(usImage!!)
    }
}