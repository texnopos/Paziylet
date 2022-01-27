package uz.texnopos.paziylet.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uz.texnopos.paziylet.core.extentions.isConnected


class NetworkChangeReceiver : BroadcastReceiver() {

    private var internetConnectedListener: (isConn: Boolean) -> Unit = {}

    override fun onReceive(context: Context?, intent: Intent) {

        if (isConnected()) internetConnectedListener.invoke(true)
        else internetConnectedListener.invoke(false)


    }

    fun getResult(internetConnectedListener: (isConn: Boolean) -> Unit) {
        this.internetConnectedListener = internetConnectedListener
    }
}
