package app.app.astrocoin.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.app.astrocoin.R
import app.app.astrocoin.adapters.AdapterTransferR
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.TransferRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentTransfers : Fragment() {

    private var sharedPreferences: SharedPreferences? = null

    private var samrecyclerView: RecyclerView? = null
    var trarray: ArrayList<TransferRequest>? = null
    var tradapter: AdapterTransferR? = null
    var transferRequest: TransferRequest? = null
    var manager: LinearLayoutManager? = null
    var currentItems: kotlin.Int = 0
    var totalItems: kotlin.Int = 0
    var scrollOutItems: kotlin.Int = 0
    var at: kotlin.Int = 0
    var isScrolling = false
    var id: String? = null
    var wallet_from:kotlin.String? = null
    var wallet_to:kotlin.String? = null
    var fio:kotlin.String? = null
    var amount:kotlin.String? = null
    var title:kotlin.String? = null
    var type:kotlin.String? = null
    var comment:kotlin.String? = null
    var status:kotlin.String? = null
    var date:kotlin.String? = null
    var timestamp:kotlin.String? = null
    var trdata:kotlin.String? = null
    var att:kotlin.String? = null
    var page = 0
    var token = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transfers, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        samrecyclerView = view.findViewById(R.id.samrecyclerView)
        manager = LinearLayoutManager(context);
        sharedPreferences =
            requireActivity().getSharedPreferences("astrocoin", Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "")!!
        trarray = ArrayList()
        tradapter = AdapterTransferR(context, trarray)
        samrecyclerView?.layoutManager = manager;
        getUserData()
    }


    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        readTransfer()
    }

    private fun showToasts(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun p(message: String) {
        println(message)
    }

    private fun readTransfer() {
        page = 1
        val call: Call<Any> = ApiClient.getUserService().userGetTransfers(page, "Bearer $token")
        call.enqueue(object : Callback<Any?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                if (response.isSuccessful) {
                    var parts3: Array<String>
                    val gson = Gson()
                    val json = gson.toJson(response.body())
                    assert(response.body() != null)
                    val jsonArray = response.body().toString().replace("[", "")
                    for (s in jsonArray.split("],").toTypedArray()) {
                        parts3 = s.split(",").toTypedArray()
                        if (parts3.isNotEmpty()) {
                            val parts = parts3[0].split("=").toTypedArray()
                            trdata = parts[0].substring(1)
                            try {
                                val jsonObject = JSONObject(json)
                                val jsonArray1 = jsonObject.getJSONArray(parts[0].substring(1))
                                for (i in 0 until jsonArray1.length()) {
                                    val jsonObject1 = jsonArray1.getJSONObject(i)
                                    comment = if (jsonObject1.has("comment")) {
                                        jsonObject1.getString("comment")
                                    } else {
                                        "no comment"
                                    }
                                    id = jsonObject1.getString("id")
                                    wallet_from = jsonObject1.getString("wallet_from")
                                    wallet_to = jsonObject1.getString("wallet_to")
                                    fio = jsonObject1.getString("fio")
                                    val parts2 =
                                        jsonObject1.getString("amount").split("\\.").toTypedArray()
                                    amount = parts2[0]
                                    title = jsonObject1.getString("title")
                                    type = jsonObject1.getString("type")
                                    status = jsonObject1.getString("status")
                                    date = jsonObject1.getString("date")
                                    timestamp = jsonObject1.getString("timestamp")
                                    transferRequest = TransferRequest(
                                        id,
                                        wallet_from,
                                        wallet_to,
                                        fio,
                                        amount,
                                        title,
                                        type,
                                        comment,
                                        status,
                                        date,
                                        timestamp,
                                        trdata
                                    )
                                    trarray!!.add(transferRequest!!)
                                    trdata = ""
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    samrecyclerView!!.adapter = tradapter
                    page++
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                page = 0
                call.cancel()
            }
        })
    }
}
