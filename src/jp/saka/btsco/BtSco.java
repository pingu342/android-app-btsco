package jp.saka.btsco;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.media.MediaRecorder;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.util.Log;
import java.lang.InterruptedException;
import android.os.Handler;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.util.Random;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.CheckBox;

import android.content.IntentFilter;

public class BtSco extends Activity
{
	private CheckBox mBluetoothScoOnCheckbox;
	private AudioManager mAudioManager;
	private Activity mThisActivity;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mThisActivity = this;

		// the stream type must be STREAM_VOICE_CALL
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

		mAudioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));

		// Bluetooth SCOをONするかどうかのチェックボックス
		mBluetoothScoOnCheckbox = (CheckBox) findViewById(R.id.BluetoothScoOnCheckbox);
		mBluetoothScoOnCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox check_box = (CheckBox) v;
				if (check_box.isChecked()) {
					mAudioManager.startBluetoothSco();
				} else {
					mAudioManager.stopBluetoothSco();
				}
			}
		});

		BroadcastReceiver bluetoothScoMonitorBcRecver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean btScoOn = mAudioManager.isBluetoothScoOn();
				Toast.makeText(mThisActivity, "BT SCO " + (btScoOn ? "ON" : "OFF"), Toast.LENGTH_LONG).show();
				mBluetoothScoOnCheckbox.setChecked(btScoOn);
			}
		};

		IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED);
		registerReceiver(bluetoothScoMonitorBcRecver, intentFilter);

		mBluetoothScoOnCheckbox.setChecked(mAudioManager.isBluetoothScoOn());
	}
}
