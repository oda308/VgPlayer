package com.example.vgplayer

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageButton
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private val playerListener = PlayerListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, PlayerService::class.java)

        player = ExoPlayer.Builder(applicationContext).build()
        player.addListener(playerListener)

        val mediaItem: MediaItem =
            MediaItem.fromUri("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()
        Log.d("vgPlayer", "playbackState: " + player.playbackState)
        Log.d("vgPlayer", "isPlaying: " + player.isPlaying)

        val previousButton: ImageButton = findViewById(R.id.previous)
        val playButton: ImageButton = findViewById(R.id.play)
        val nextButton: ImageButton = findViewById(R.id.next)

        previousButton.setOnClickListener {
            Log.d("vgPlayer", "previousButton was tapped")
        }

        playButton.setOnClickListener {
            Log.d("vgPlayer", "playButton was tapped")
            if (player.isPlaying) {
                Log.d("vgPlayer", "pause")
                stopService(serviceIntent)
                player.pause()
                playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            } else {
                Log.d("vgPlayer", "play")
                if (Build.VERSION.SDK_INT > 25) {
                    startForegroundService(serviceIntent)
                }
                startService(serviceIntent)
                player.play()
                playButton.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }

        nextButton.setOnClickListener {
            Log.d("vgPlayer", "nextButton was tapped")
        }
    }


    private inner class PlayerListener : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            Log.d("vgPlayer", "playing changed : " + isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            Log.d("vgPlayer", "playbackState changed : " + playbackState)
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
                throw error
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("vgPlayer", "プレーヤーリソースを解放")
        player.release()
    }
}

class PlayerService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("vgPlayer", "onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        Log.d("vgPlayer", "startForegroundService()")
        return super.startForegroundService(service)
    }

    override fun startService(service: Intent?): ComponentName? {
        Log.d("vgPlayer", "startService()")
        return super.startService(service)
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("vgPlayer", "stopService()")
        return super.stopService(name)
    }
}