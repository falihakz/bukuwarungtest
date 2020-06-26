package falih.example.bukuwarungtestapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import falih.example.bukuwarungtestapp.R
import falih.example.bukuwarungtestapp.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initView()
        initObserver()
    }

    private fun initObserver() {
        viewModel.loadingProfileListEvent.observe(viewLifecycleOwner, Observer { isLoading ->
            println("isLoading: $isLoading")
        })

        viewModel.userList.observe(viewLifecycleOwner, Observer {users ->
            println("users: ${users.joinToString(",")}")
        })
    }

    private fun initView() {

    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }
}