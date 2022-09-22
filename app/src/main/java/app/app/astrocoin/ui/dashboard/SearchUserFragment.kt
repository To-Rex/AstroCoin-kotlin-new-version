package app.app.astrocoin.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.app.astrocoin.R
import app.app.astrocoin.adapters.AdapterUserSearch
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.UserRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchUserFragment : Fragment() {

    private lateinit var searchUserViewModel: SearchUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchUserViewModel = ViewModelProvider(this)[SearchUserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_searchuser, container, false)
    }

    private var sharedPreferences: SharedPreferences? = null
    var token: String? = null
    var dataModalArrayList: ArrayList<UserRequest>? = null
    var adapter: AdapterUserSearch? = null
    var listViewsearch: ListView? = null
    var searchprogressBar: ProgressBar? = null
    var usersearchView: SearchView? = null
    var id =
        ""
    var name: String? = ""
    var last_name: String? = ""
    var stack: String? = ""
    var photo: String? = ""
    var qwasar: String? = ""
    var status: String? = ""
    var balance: String? = ""
    var wallet: String? = ""
    var imagesetting: ImageView? = null
    var imgsample: ImageView? = null
    var imgsearch: ImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("astrocoin", Context.MODE_PRIVATE)
        dataModalArrayList = ArrayList()
        listViewsearch = view.findViewById(R.id.listViewsearch)
        listViewsearch?.divider = null
        listViewsearch?.dividerHeight = 20
        searchprogressBar = view.findViewById(R.id.searchprogressBar)
        adapter = AdapterUserSearch(dataModalArrayList, context)
        getUserData()
        listViewsearch?.adapter = adapter
        usersearchView = view.findViewById(R.id.usersearchView)
        usersearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })

    }
    private fun getUserData() {
        token = sharedPreferences?.getString("token", "")
        getData()
    }
    private fun getData() {
        searchprogressBar!!.visibility = View.VISIBLE
        val call: Call<Any> = ApiClient.getUserService().userSearchRequest("Bearer $token")
        call.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val json = gson.toJson(response.body())
                    val parser = JsonParser()
                    val array = parser.parse(json).asJsonArray
                    for (element in array) {
                        val `object` = element.asJsonObject
                        id = `object`["id"].asString
                        name = `object`["name"].asString
                        last_name = `object`["last_name"].asString
                        stack = `object`["stack"].asString
                        photo = if (`object`["photo"] != null) {
                            `object`["photo"].asString
                        } else {
                            ""
                        }
                        qwasar = `object`["qwasar"].asString
                        status = `object`["status"].asString
                        balance = `object`["balance"].asString
                        wallet = `object`["wallet"].asString
                        dataModalArrayList!!.add(
                            UserRequest(
                                id, name!!, last_name!!, stack!!, photo!!, qwasar!!, status!!, balance!!, wallet!!
                            )
                        )
                    }
                    listViewsearch!!.adapter = adapter
                    searchprogressBar!!.visibility = View.GONE
                    call.cancel()
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }
}