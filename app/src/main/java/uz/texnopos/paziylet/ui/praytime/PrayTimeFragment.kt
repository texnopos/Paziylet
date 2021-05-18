package uz.texnopos.paziylet.ui.praytime

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_namazwaqti.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.ui.location.LocationFragment
import java.text.SimpleDateFormat
import java.util.*

class PrayTimeFragment : LocationFragment(R.layout.fragment_namazwaqti) {

    private val viewModel: PrayTimeViewModel by viewModel()

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpObserver()
        btnCompass.onClick {
            val action = PrayTimeFragmentDirections.actionNamazwaqtiFragmentToCompassFragment()
            navController.navigate(action)
        }
        val timezone =
            SimpleDateFormat("Z", Locale.getDefault()).format(Calendar.getInstance().time)
                .toInt() / 100
        tvDate.text =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                .toString()
        location.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBar.visibility(true)
                ResourceState.SUCCESS -> {
                    viewModel.getTimes(timezone, it.data!!.latitude, it.data.longitude)
                    tvRegion.text =
                        getCountryName(requireContext(), it.data.latitude, it.data.longitude)
                    progressBar.visibility(false)
                }
                else -> {
                }
            }
        })
    }

    private fun getCountryName(context: Context?, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        return "${addresses[0].locality}, ${addresses[0].countryName}"
    }

    private fun setUpObserver() {
        viewModel.prayTime.observe(viewLifecycleOwner, {
            progressBar.visibility(false)
            tvTimeTan.text = it.fajr
            tvTimeQuyash.text = it.sunrise
            tvTimePesin.text = it.dhuhr
            tvTimeNamazliger.text = it.asr
            tvTimeSham.text = it.maghrib
            tvTimeQuptan.text = it.isha

        })
    }
}