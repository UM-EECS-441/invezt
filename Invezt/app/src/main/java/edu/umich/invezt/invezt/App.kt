package edu.umich.invezt.invezt

import android.app.Application
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe

class App : Application() {
    companion object {
        var inveztID : String? = null
        var stripeID : String? = null
    }

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51HmWsQLCRJXQF7TjzWSdvAYoFkfqCFI4VleesPx0zfZSLnGPh45MCg6S4A5NchWLwCzamErq01luAde21KDAZdBB00SW6uNNBI"
        )
    }
}