package com.example.mytest

import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytest.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import java.io.InputStream
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var listeler : ArrayList<Liste>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        //showAlertDialog()
        
        listeler = ArrayList()
        
        try {
            readM3uFile()
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Dosya okunurken hata oluştu: ${e.message}")
        }
        
        // RecyclerView'u ayarla
        
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels / displayMetrics.density
        val itemWidth = 150 // Her bir öğenin genişliği (dp cinsinden)
        val spanCount = (screenWidth / itemWidth).toInt()
        
        binding.recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        
        val adapter = ListeAdapter(listeler)
        binding.recyclerView.adapter = adapter
    }

    private fun readM3uFile() {
        val inputStream: InputStream = assets.open("yourfile.m3u") // Dosya adını ve yolunu buraya girin
        val fileContent = inputStream.bufferedReader().use { it.readText() }
        val lines = fileContent.split("\n")

        var kanalAdi = ""
        var kanalLogo = ""
        var kanalM3u = ""

        lines.forEach { line ->
            if (line.startsWith("#EXTINF")) {
                val kanalAdiRegex = "tvg-name=\"([^\"]+)\"".toRegex()
                val kanalLogoRegex = "tvg-logo=\"([^\"]+)\"".toRegex()
                
                val kanalAdiMatch = kanalAdiRegex.find(line)
                if (kanalAdiMatch != null) {
                    kanalAdi = kanalAdiMatch.groupValues[1]
                }

                val kanalLogoMatch = kanalLogoRegex.find(line)
                if (kanalLogoMatch != null) {
                    kanalLogo = kanalLogoMatch.groupValues[1]
                }
            } else if (line.startsWith("http")) {
                kanalM3u = line
                if (kanalAdi.isNotBlank() && kanalLogo.isNotBlank() && kanalM3u.isNotBlank()) {
                    listeler.add(Liste(kanalAdi, kanalLogo, kanalM3u))
                }
            }
        }
    }
    
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Uyarı Başlığı")
            .setMessage("Bu bir Material3 uyarı diyalog örneğidir.")
            .setPositiveButton("Tamam") { dialog, which ->
                // Tamam butonuna tıklanıldığında yapılacak işlemler
                dialog.dismiss()
            }
            .setNegativeButton("İptal") { dialog, which ->
                // İptal butonuna tıklanıldığında yapılacak işlemler
                dialog.dismiss()
            }
            .show()
    }
}
