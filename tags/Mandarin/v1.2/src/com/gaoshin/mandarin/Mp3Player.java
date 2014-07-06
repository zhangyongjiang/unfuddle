package com.gaoshin.mandarin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.Toast;

public class Mp3Player {
	static MediaPlayer mediaPlayer = new MediaPlayer();
	static AssetFileDescriptor descriptor;
	static AudioManager am;
	
	public static void playMedia(Context context, String filename){
		String path = "mp3" + File.separator + filename + ".mp3";
		AssetManager manager = context.getAssets();
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		if(mediaPlayer != null){
			mediaPlayer.reset();
		}
		if(am.getStreamVolume(AudioManager.STREAM_MUSIC) == 0){
			Toast.makeText(context, "Please turn up your Media Volume", Toast.LENGTH_LONG).show();
		}
		try {
			descriptor = manager.openFd(path);
			long start = descriptor.getStartOffset();
			long end = descriptor.getLength();
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetPlayer(){
		mediaPlayer.stop();
	}
	
}
