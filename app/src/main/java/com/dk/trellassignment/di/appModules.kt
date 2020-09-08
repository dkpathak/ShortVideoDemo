package com.dk.trellassignment.di

import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.dk.trellassignment.R
import com.dk.trellassignment.coreplayer.PlayHelper
import com.dk.trellassignment.coreplayer.TrellMediaPlayer
import com.dk.trellassignment.data.DataHelper
import com.dk.trellassignment.data.DataManager
import com.dk.trellassignment.data.local.LocalDataHelper
import com.dk.trellassignment.data.local.LocalDataManager
import com.dk.trellassignment.features.main.MainViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    viewModel { MainViewModel(get()) }

    single { PagerSnapHelper () }

    single<DataHelper> { DataManager(get(), Dispatchers.IO) }

    single<LocalDataHelper> { LocalDataManager(androidApplication()) }

    factory<PlayHelper.PlayerCallback> { TrellMediaPlayer(androidContext(),get(),get()) }

    factory {
        (androidContext().applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager)
            .createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, androidContext().getString(R.string.app_name))
    }

    factory { androidContext().applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    factory<DataSource.Factory> {

        DefaultDataSourceFactory(
            androidContext().applicationContext,
            Util.getUserAgent(androidContext().applicationContext,
                androidContext().getString(R.string.app_name)), null
        )
    }

    factory {
        SimpleExoPlayer.Builder(androidContext()).build().apply {
            audioAttributes = AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()
        }
    }


    factory {
        val builder = DefaultTrackSelector.ParametersBuilder( /* context= */androidContext())
        val tunneling: Boolean = true
        if (Util.SDK_INT >= 21 && tunneling) {
            builder.setTunnelingAudioSessionId(
                C.generateAudioSessionIdV21( /* context= */androidContext())
            )
        }
    }
}