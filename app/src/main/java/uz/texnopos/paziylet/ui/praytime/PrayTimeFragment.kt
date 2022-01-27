package uz.texnopos.paziylet.ui.praytime

import android.content.Context
import android.content.IntentFilter
import android.location.Address
import android.location.Geocoder
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_compass.progressBar
import kotlinx.android.synthetic.main.fragment_compass.tvDate
import kotlinx.android.synthetic.main.fragment_compass.tvRegion
import kotlinx.android.synthetic.main.fragment_pray_time.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.toast
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.settings.NetworkChangeReceiver
import uz.texnopos.paziylet.ui.location.LocationFragment
import java.text.SimpleDateFormat
import java.util.*


class PrayTimeFragment : LocationFragment(R.layout.fragment_pray_time) {
    private val receiver = NetworkChangeReceiver()
    private val viewModel: PrayTimeViewModel by viewModel()
    private var isSetData = false
    private var isFirst = true

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        requireActivity().registerReceiver(receiver, intentFilter)

        receiver.getResult {
            if (!isSetData) {
                if (it) {
                    setUpObserver()
                    setUpObserverLocation()
                } else if (isFirst) {
                    isFirst = false
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Дыққат!")
                        .setMessage("Локацияны алыў ушын интернетты косың!")
                        .setPositiveButton("Түсиникли") { dia, _ ->
                            dia.dismiss()
                        }
                        .show()
                }
            }
        }

        btnCompass.onClick {
            val action = PrayTimeFragmentDirections.actionNamazwaqtiFragmentToCompassFragment()
            navController.navigate(action)
        }

        tvDate.text =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                .toString()

    }

    private fun setUpObserver() {
        viewModel.prayTime.observe(viewLifecycleOwner, {
            isSetData = true
            progressBar.visibility(false)
            tvTimeTan.text = it.fajr
            tvTimeQuyash.text = it.sunrise
            tvTimePesin.text = it.dhuhr
            tvTimeNamazliger.text = it.asr
            tvTimeSham.text = it.maghrib
            tvTimeQuptan.text = it.isha

        })
    }

    private fun setUpObserverLocation() {
        val timezone =
            SimpleDateFormat("Z", Locale.getDefault()).format(Calendar.getInstance().time)
                .toInt() / 100
        location.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> progressBar.visibility(true)
                ResourceState.SUCCESS -> {

                    viewModel.getTimes(timezone, it.data!!.latitude, it.data.longitude)
                    tvRegion.text =
                        getCountryName(requireContext(), it.data.latitude, it.data.longitude)
                    progressBar.visibility(false)
                }
                ResourceState.ERROR -> {
                    toast("Internet qosilmag'an!")
                }
                else -> {
                    toast("Internet qosilmag'an!")
                }
            }
        })
    }

    private fun getCountryName(context: Context?, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        return "${addresses[0].locality}, ${addresses[0].countryName}"
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(receiver)
        super.onDestroy()
    }
}