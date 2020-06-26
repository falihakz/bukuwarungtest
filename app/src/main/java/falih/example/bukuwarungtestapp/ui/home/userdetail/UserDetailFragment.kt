package falih.example.bukuwarungtestapp.ui.home.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import falih.example.bukuwarungtestapp.R
import falih.example.bukuwarungtestapp.data.room.entity.Profile
import falih.example.bukuwarungtestapp.databinding.FragmentUserDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModel()
    private lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            viewModel.setProfile(it.getParcelable(ARG_PROFILE))
        }

        initBinding()
        initView()
        initObserver()
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun initView() {

    }

    private fun initObserver() {

    }

    companion object {
        const val ARG_PROFILE = "profile"

        @JvmStatic
        fun newInstance(profile: Profile) =
            UserDetailFragment()
                .apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PROFILE, profile)
                }
            }
    }
}