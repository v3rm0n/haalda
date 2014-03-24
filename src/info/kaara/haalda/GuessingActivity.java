package info.kaara.haalda;

import info.kaara.haalda.R;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GuessingActivity extends Activity implements RecognitionListener {

	private static final String TAG = GuessingActivity.class.getName();
	public static final String EXTRA_LEVEL = "EXTRA_LEVEL";
	private SpeechRecognizer spc;
	private String currentWord;
	private int level = R.array.first_level;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		level = intent.getIntExtra(EXTRA_LEVEL, R.array.first_level);
		setContentView(R.layout.guess);
		changeWord();
		this.spc = SpeechRecognizer.createSpeechRecognizer(this);
		spc.setRecognitionListener(this);
		final Button speak = (Button) findViewById(R.id.b_speak);
		speak.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				listen();
				speak.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		spc.destroy();
	}

	private void listen() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		spc.startListening(intent);
	}

	private void changeWord() {
		currentWord = getRandomWord();
		final TextView word = (TextView) findViewById(R.id.word);
		word.setText("\"" + currentWord + "\"");
	}

	private String getRandomWord() {
		CharSequence[] words = getResources().getTextArray(level);
		Random r = new Random();
		int index = r.nextInt(words.length);
		return words[index].toString();
	}

	private void showDialog(boolean success, String match) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String message = null;
		if (match == null) {
			message = getResources().getString(R.string.you_said_nothing) + ". " + getResources().getString(R.string.try_again2);
		} else {
			message = success ? getResources().getString(R.string.success) : getResources().getString(R.string.you_said) + " \"" + match + "\". "
					+ getResources().getString(R.string.try_again2);
		}
		builder.setMessage(message).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				changeWord();
				final Button speak = (Button) findViewById(R.id.b_speak);
				speak.setVisibility(View.VISIBLE);
			}
		}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				GuessingActivity.this.finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onRmsChanged(float rmsdB) {
	}

	@Override
	public void onResults(Bundle bundle) {
		final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		boolean success = false;
		String match = null;
		if (results != null) {
			for (String result : results) {
				Log.d(TAG, "Result: " + result);
				if (currentWord.equalsIgnoreCase(result)) {
					success = true;
					Log.d(TAG, "We have a match!");
				}
				match = result;
			}
		} else {
			Log.d(TAG, "No results");
		}
		showDialog(success, match);
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
	}

	@Override
	public void onError(int error) {
		final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		Log.d(TAG, "Error: " + error);
		AlertDialog.Builder builder = new AlertDialog.Builder(GuessingActivity.this);
		String message = "";
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			message = message.concat(getResources().getString(R.string.error_audio));
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			message = message.concat(getResources().getString(R.string.error_client));
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			message = message.concat(getResources().getString(R.string.error_insufficient_permissions));
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			message = message.concat(getResources().getString(R.string.error_network));
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			message = message.concat(getResources().getString(R.string.error_network_timeout));
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			message = message.concat(getResources().getString(R.string.error_no_match));
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			message = message.concat(getResources().getString(R.string.error_recognizer_busy));
			break;
		case SpeechRecognizer.ERROR_SERVER:
			message = message.concat(getResources().getString(R.string.error_server));
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = message.concat(getResources().getString(R.string.error_speech_timeout));
			break;
		}
		message = message.concat(getResources().getString(R.string.try_again));
		builder.setMessage(message).setCancelable(false).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				final Button speak = (Button) findViewById(R.id.b_speak);
				speak.setVisibility(View.VISIBLE);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onEndOfSpeech() {
		Log.d(TAG, "User stopped talking");
		final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.d(TAG, "User started talking");

	}

}
