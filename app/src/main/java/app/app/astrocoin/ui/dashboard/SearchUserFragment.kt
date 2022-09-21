package app.app.astrocoin.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.app.astrocoin.R

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
}