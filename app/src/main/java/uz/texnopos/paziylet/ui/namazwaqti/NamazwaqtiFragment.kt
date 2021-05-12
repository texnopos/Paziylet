package uz.texnopos.paziylet.ui.namazwaqti

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_namazwaqti.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.di.ResourceState

class NamazwaqtiFragment:Fragment(R.layout.fragment_namazwaqti) {

    private val viewModel:NamazwaqtiViewModel by viewModel()

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        setUpObserver()

    }

    private fun setUpObserver(){
        viewModel.time.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBar.visibility(true)
                ResourceState.SUCCESS -> {
                    progressBar.visibility(false)
                    viewModel.getTimes()
                }
                ResourceState.ERROR -> {
                    progressBar.visibility(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}