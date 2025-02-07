package com.example.mytest

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListeAdapter(private val liste: List<Liste>) : 
    RecyclerView.Adapter<ListeAdapter.ListeViewHolder>() {

    class ListeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liste, parent, false)
        return ListeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListeViewHolder, position: Int) {
        val currentItem = liste[position]
    
        // Görsel ve metin ayarları
        holder.textView.text = currentItem.kanalAdi
        Glide.with(holder.itemView.context)
            .load(currentItem.kanalLogo)
            .override(150, 150) // Resim boyutunu sabitle
            .centerCrop()
            .into(holder.imageView)
    
        // Cihazın TV olup olmadığını kontrol edin
        if (isTvDevice(holder.itemView.context)) {
            holder.itemView.isFocusable = true
            holder.itemView.isFocusableInTouchMode = true
    
            // Focus değişimlerini yönet sadece TV cihazlarında
            holder.itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    // Büyütme animasyonu
                    holder.itemView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                } else {
                    // Küçültme animasyonu
                    holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                }
            }
        } else {
            // Dokunmatik cihazlarda fokus özelliklerini devre dışı bırakın
            holder.itemView.isFocusable = false
            holder.itemView.isFocusableInTouchMode = false
        }
    
        // Tüm item'a tıklama özelliği ekle
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlayerActivity::class.java).apply {
                putExtra("channel_list", ArrayList(liste))
                putExtra("current_index", position)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = liste.size

    fun isTvDevice(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }
}