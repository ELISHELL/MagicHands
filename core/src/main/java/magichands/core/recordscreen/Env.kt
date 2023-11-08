package magichands.core.recordscreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.MediaCodec
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.view.Surface

class Env {
    companion object {
        var mediaProjectionManager: MediaProjectionManager? = null
        var mediaProjection: MediaProjection? = null

        var virtualDisplay: VirtualDisplay? = null
        var handler: Handler? = null
        var imageReader: ImageReader? = null
        var bitmap: Bitmap? = null
        var videoEncoder: MediaCodec? = null
        var mVideoEncodec: MediaCodec? = null
        var mVideoBuffInfo: MediaCodec.BufferInfo? = null
        var mSurface: Surface? = null
        var encodingFinished = true
        var pts: Long = 0
        var timeStamo: Long = 0
        var screenLive: SocketLive? = null

        @Synchronized
        fun setMediaProjections(mediaProjection: MediaProjection) {
            Env.mediaProjection = mediaProjection
        }

        @Synchronized
        fun getMediaProjections(): MediaProjection? {
            return Env.mediaProjection
        }
    }


}