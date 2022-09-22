package app.app.astrocoin.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.app.astrocoin.R
import app.app.astrocoin.models.Getdata
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson

class SettingsFragment : Fragment() {

    private lateinit var notificationsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    private var sharedPreferences: SharedPreferences? = null
    private var usimage: ShapeableImageView? = null
    private var txtsetfullname: TextView? = null
    private var txtsetemail: TextView? = null
    private var txtsetqwasar: TextView? = null
    private var txtsetstack: TextView? = null
    private var txtsetwallets: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences("astrocoin", Context.MODE_PRIVATE)

        txtsetfullname = view.findViewById(R.id.txtsetfullname)
        txtsetemail = view.findViewById(R.id.txtsetemail)
        txtsetqwasar = view.findViewById(R.id.txtsetqwasar)
        txtsetstack = view.findViewById(R.id.txtsetstack)
        txtsetwallets = view.findViewById(R.id.txtsetwallets)
        usimage = view.findViewById(R.id.usimage)
        getUserData()

    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        txtsetfullname?.text = user.name+" "+user.last_name
        txtsetemail?.text = user.email
        txtsetqwasar?.text = user.qwasar
        txtsetstack?.text = user.stack
        txtsetwallets?.text = user.wallet
        Glide.with(requireContext()).load("https://api.astrocoin.uz" + user.photo).into(usimage!!)
    }
}