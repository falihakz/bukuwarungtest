package falih.example.bukuwarungtestapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import falih.example.bukuwarungtestapp.R
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import falih.example.bukuwarungtestapp.databinding.FragmentHomeBinding
import falih.example.bukuwarungtestapp.ui.common.InfiniteScroll
import falih.example.bukuwarungtestapp.ui.common.adapter.BindableAdapter
import falih.example.bukuwarungtestapp.ui.home.userdetail.UserDetailFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private val adapter: BindableAdapter<HomeViewModel, Profile> by lazy {
        BindableAdapter<HomeViewModel, Profile>(viewModel = viewModel)
    }
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
        viewModel.loadingProfileListState.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading != null && isLoading == true){
                if (viewModel.mPage > 1){
                    binding.tvLoadingContainer.visibility = View.GONE
                    binding.tvLoadMoreContainer.visibility = View.VISIBLE
                } else {
                    binding.tvLoadingContainer.visibility = View.VISIBLE
                    binding.tvLoadMoreContainer.visibility = View.GONE
                }
            } else {
                binding.tvLoadingContainer.visibility = View.GONE
                binding.tvLoadMoreContainer.visibility = View.GONE
            }
        })

        viewModel.userFetchErrorEvent.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.userList.observe(viewLifecycleOwner, Observer {users ->
            println("user list size: ${users.size}")
        })

        viewModel.showUserDetailEvent.observe(viewLifecycleOwner, Observer {profile ->
            profile?.let {
                val bundle = bundleOf(UserDetailFragment.ARG_PROFILE to it)
                findNavController().navigate(R.id.action_navigation_home_to_userDetailFragment, bundle)
            }
        })
    }

    private fun initView() {
        binding.rvUserList.layoutManager = LinearLayoutManager(context)
        binding.rvUserList.adapter = adapter

        resetRvScrollListener()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun resetRvScrollListener() {
        binding.rvUserList.clearOnScrollListeners()
        binding.rvUserList.addOnScrollListener(object: InfiniteScroll(){
            override fun onLoadMore() {
                viewModel.fetchUserListFromRemote()
            }
        })
    }
}