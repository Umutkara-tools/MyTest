package com.example.mytest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.example.mytest.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var exoPlayer: ExoPlayer? = null
    private var currentIndex: Int = 0
    private lateinit var channelList: ArrayList<Liste>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kanal listesini ve mevcut indeksini al
        channelList = intent.getSerializableExtra("channel_list") as ArrayList<Liste>
        currentIndex = intent.getIntExtra("current_index", 0)

        // Mevcut URL'yi oynat
        val m3uUrl = channelList[currentIndex].kanalM3u
        initializePlayer(m3uUrl)

        // Sonraki tuşu ayarla
        binding.nextButton.setOnClickListener {
            playNextChannel()
        }
    }

    private fun initializePlayer(m3uUrl: String) {
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(Uri.parse(m3uUrl))
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
    }

    private fun playNextChannel() {
        currentIndex = (currentIndex + 1) % channelList.size
        val nextUrl = channelList[currentIndex].kanalM3u
        initializePlayer(nextUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}