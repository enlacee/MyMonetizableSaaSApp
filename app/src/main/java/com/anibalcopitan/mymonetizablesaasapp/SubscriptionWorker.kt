package com.anibalcopitan.mymonetizablesaasapp

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalDate

import com.anibalcopitan.mymonetizablesaasapp.data.appconfig.AppConfigManager
import com.anibalcopitan.mymonetizablesaasapp.data.appconfig.AppConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SubscriptionWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        /**
         * 1. local data validation (simple)
         */

        /**
         * 1. Load data base
         * - KEY_SUBSCRIPTION_START_DATE
         * - KEY_SUBSCRIPTION_DURATION_DAYS
         * - KEY_SUBSCRIPTION_PLAN
         */
        val appConfigManager = AppConfigManager(applicationContext)
        val appConfig = appConfigManager.getAppConfig()
        Log.d("SubscriptionWorker", "Config cargada: $appConfig")

        /**
         * 2. con la data local validar la suscripcion
         * No necesita hacer consulta con API xq en el login ya tengo la data que necesito
         * - fecha de inicio: 2025-01-01
         * - dias de prueba: 3
         * La app solo funcionara esos 3 dias luego no funcionara los SMS y demas simple
         */
        val subscriptionExpired = isSubscriptionExpired(appConfig)
        if (subscriptionExpired == true) {
            appConfig.subscriptionPlan = "0"
            appConfigManager.saveAppConfig(appConfig)

            Log.e("SubscriptionWorker", "EXPIRO DATA   $appConfig")
            Log.e("SubscriptionWorker", appConfigManager.getAppConfig().toString())

            Log.e("SubscriptionWorker", "EXPIRO " + appConfig.subscriptionStartDate)
        } else {
            Log.e("SubscriptionWorker", "NO VENCE SUCRIPCION OK FULL " + appConfig.subscriptionStartDate)
        }

        /**
         * error worker
         */
        if (appConfig.id.isEmpty() || appConfig.username.isEmpty()) {
            Log.e("SubscriptionWorker", "AppConfig tiene valores vacíos")
            return Result.failure()
        }

        return Result.success()
    }

    /**
     * Verifica si la suscripcion ya espiro
     *
     * @return True|False si expiro el tiempo
     *
     * Usar fechas en milisegundos (sin librerías)
     * Evitando dependencias a la libreria o version de android 26 para usar la otra funcion
     *
     * return boolen, cualquier fallo retorna true rapidamente
     * true = asume que la suscripción ya expiró
     */
    private fun isSubscriptionExpired(appConfig: AppConfig): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate = sdf.parse(appConfig.subscriptionStartDate.substring(0, 10)) ?: return true
            val durationDays = appConfig.subscriptionDurationDays.toIntOrNull() ?: return true

            val calendar = Calendar.getInstance()
            calendar.time = startDate
            calendar.add(Calendar.DAY_OF_YEAR, durationDays)

            // Se compara si la fecha actual es después de la fecha de expiración.
            // Si now es después, devuelve true (la suscripción expiró).
            val expirationDate = calendar.time
            val now = Date()

            now.after(expirationDate)
        } catch (e: Exception) {
            true
        }
    }

}
