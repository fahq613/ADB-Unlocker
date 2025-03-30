package com.example.adbunlocker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.adbunlocker.R.*
import com.example.adbunlocker.R.id.statusText
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val unlockButton: Button = findViewById(id.unlockButton)
        val statusText: TextView = findViewById(statusText)
        val checkDeviceButton: Button = findViewById(id.checkDeviceButton)
        val resetPinButton: Button = findViewById(id.resetPinButton)
        val bypassFrpButton: Button = findViewById(id.bypassFrpButton)
        val enableUsbDebugButton: Button = findViewById(id.enableUsbDebugButton)
        val bypassFrpOtgButton: Button = findViewById(id.bypassFrpOtgButton)
        val runExploitScriptButton: Button = findViewById(id.runExploitScriptButton)

        checkDeviceButton.setOnClickListener {
            val result = checkDeviceConnection()
            statusText.text = result
        }

        unlockButton.setOnClickListener {
            val result = unlockDevice()
            statusText.text = result
        }

        resetPinButton.setOnClickListener {
            val result = resetPin()
            statusText.text = result
        }

        bypassFrpButton.setOnClickListener {
            val result = bypassFRP()
            statusText.text = result
        }

        enableUsbDebugButton.setOnClickListener {
            val result = enableUsbDebugging()
            statusText.text = result
        }

        bypassFrpOtgButton.setOnClickListener {
            val result = bypassFRPWithOTG()
            statusText.text = result
        }

        runExploitScriptButton.setOnClickListener {
            val result = runExploitScript()
            statusText.text = result
        }
    }

    private fun checkDeviceConnection(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb devices")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            if (output.contains("device")) {
                "Enhet ansluten och redo."
            } else {
                "Ingen enhet upptäckt. Se till att ADB och USB-felsökning är aktiverat."
            }
        } catch (e: Exception) {
            "Fel: ${e.message}. Kontrollera att ADB är installerat."
        }
    }

    private fun unlockDevice(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb shell rm /data/system/gesture.key")
            process.waitFor()
            val rebootProcess = Runtime.getRuntime().exec("adb reboot")
            rebootProcess.waitFor()
            "Skärmlås borttaget! Starta om enheten."
        } catch (e: Exception) {
            "Fel: ${e.message}. Se till att USB-felsökning är aktiverat."
        }
    }

    private fun resetPin(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb shell rm /data/system/locksettings.db")
            process.waitFor()
            val rebootProcess = Runtime.getRuntime().exec("adb reboot")
            rebootProcess.waitFor()
            "PIN-kod återställd! Starta om enheten."
        } catch (e: Exception) {
            "Fel: ${e.message}. Se till att USB-felsökning är aktiverat."
        }
    }

    private fun bypassFRP(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb shell am broadcast -a android.intent.action.MASTER_CLEAR")
            process.waitFor()
            "FRP-lås (Google-konto) har återställts. Starta om enheten."
        } catch (e: Exception) {
            "Fel: ${e.message}. FRP kan kräva extra steg för att avlägsnas."
        }
    }

    private fun enableUsbDebugging(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb shell settings put global development_settings_enabled 1")
            process.waitFor()
            "USB-felsökning aktiverad!"
        } catch (e: Exception) {
            "Fel: ${e.message}. Kräver tillgång till ADB."
        }
    }

    private fun bypassFRPWithOTG(): String {
        return try {
            "Anslut en OTG-enhet med en bypass-app. Öppna filhanteraren och installera APK."
        } catch (e: Exception) {
            "Fel: ${e.message}. Kontrollera att OTG är aktiverat."
        }
    }

    private fun runExploitScript(): String {
        return try {
            val process = Runtime.getRuntime().exec("adb shell sh /sdcard/exploit.sh")
            process.waitFor()
            "Exploit-skriptet har körts. Kontrollera enheten."
        } catch (e: Exception) {
            "Fel: ${e.message}. Se till att skriptet är uppladdat på enheten."
        }
    }
}