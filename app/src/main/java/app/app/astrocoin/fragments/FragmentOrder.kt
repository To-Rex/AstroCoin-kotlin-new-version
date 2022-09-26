package app.app.astrocoin.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.app.astrocoin.R
import app.app.astrocoin.adapters.AdapterTransferR
import app.app.astrocoin.models.TransferRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentOrder : Fragment() {


    private var sharedPreferences: SharedPreferences? = null
    private var samOrderRecycler: RecyclerView? = null
    private var trArray: ArrayList<TransferRequest>? = null
    private var trAdapter: AdapterTransferR? = null
    private var transferRequest: TransferRequest? = null
    private var manager: LinearLayoutManager? = null
    private var currentItems: Int = 0
    private var totalItems: Int = 0
    private var scrollOutItems: Int = 0
    private var isScrolling = false
    private var id: String? = null
    private var walletFrom:String? = null
    private var walletTo:String? = null
    private var fio:String? = null
    private var amount:String? = null
    private var title:String? = null
    private var type:String? = null
    private var comment:String? = null
    private var status:String? = null
    private var date:String? = null
    private var timestamp:String? = null
    private var trData:String? = null
    private var page = 0
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        samOrderRecycler = view.findViewById(R.id.samorderrecycler)
        manager = LinearLayoutManager(context)
        sharedPreferences =
            requireActivity().getSharedPreferences(requireContext().getString(R.string.astrocoin), Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "")!!
        trArray = ArrayList()
        trAdapter = context?.let { AdapterTransferR(it, trArray!!) }
        samOrderRecycler?.layoutManager = manager
        getUserData()

        samOrderRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentItems = manager!!.childCount
                totalItems = manager!!.itemCount
                scrollOutItems = manager!!.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    scrollInfinity()
                    isScrolling = false
                }
            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        readTransfer()
    }
    private fun readTransfer() {
        page = 1
        val call: Call<Any> = ApiClient.userService.userOrderRequest(page, "Bearer $token")
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
                            trData = parts[0].substring(1)
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
                                    walletFrom = jsonObject1.getString("wallet_from")
                                    walletTo = jsonObject1.getString("wallet_to")
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
                                        id!!, walletFrom!!, walletTo!!, fio!!, amount!!, title!!, type!!,
                                        comment!!, status!!, date!!, timestamp!!, trData!!
                                    )
                                    trArray!!.add(transferRequest!!)
                                    trData = ""
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    samOrderRecycler!!.adapter = trAdapter
                    page++
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                page = 0
            }
        })
    }
    private fun scrollInfinity(){
        if (page > 0) {
            val call: Call<Any> = ApiClient.userService.userOrderRequest(page, "Bearer $token")
            call.enqueue(object : Callback<Any?> {
                @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
                override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                    if (response.isSuccessful) {
                        page++
                        var parts3: Array<String>
                        val gson = Gson()
                        val json = gson.toJson(response.body())
                        assert(response.body() != null)
                        val jsonArray = response.body().toString().replace("[", "")
                        for (s in jsonArray.split("],").toTypedArray()) {
                            parts3 = s.split(",").toTypedArray()
                            if (parts3.isNotEmpty()) {
                                val parts = parts3[0].split("=").toTypedArray()
                                trData = parts[0].substring(1)
                                try {
                                    val jsonObject = JSONObject(json)
                                    val jsonArray1 = jsonObject.getJSONArray(parts[0].substring(1))
                                    for (i in 0 until jsonArray1.length()) {
                                        val jsonObject1 = jsonArray1.getJSONObject(i)
                                        comment = if (jsonObject1.has("comment")) {
                                            jsonObject1.getString("comment")
                                        } else "no comment"
                                        if (jsonObject1.has("id")) id = jsonObject1.getString("id")
                                        if (jsonObject1.has("wallet_from")) walletFrom =
                                            jsonObject1.getString("wallet_from")
                                        if (jsonObject1.has("wallet_to")) walletTo =
                                            jsonObject1.getString("wallet_to")
                                        if (jsonObject1.has("fio")) fio =
                                            jsonObject1.getString("fio")
                                        val parts2 = jsonObject1.getString("amount").split("\\.")
                                            .toTypedArray()
                                        amount = parts2[0]
                                        if (jsonObject1.has("title")) title =
                                            jsonObject1.getString("title")
                                        if (jsonObject1.has("type")) type =
                                            jsonObject1.getString("type")
                                        if (jsonObject1.has("status")) status =
                                            jsonObject1.getString("status")
                                        if (jsonObject1.has("date")) date =
                                            jsonObject1.getString("date")
                                        timestamp = if (jsonObject1.has("timestamp")) {
                                            jsonObject1.getString("timestamp")
                                        } else "no timestamp"
                                        transferRequest = TransferRequest(
                                            id!!, walletFrom!!, walletTo!!, fio!!, amount!!, title!!, type!!,
                                            comment!!, status!!, date!!, timestamp!!, trData!!
                                        )
                                        trArray!!.add(transferRequest!!)
                                        trData = ""
                                        trAdapter?.notifyDataSetChanged()
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        trAdapter?.notifyDataSetChanged()
                        call.cancel()
                    } else page = 0
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    page = 0
                    call.cancel()
                }
            })
        }
    }
}