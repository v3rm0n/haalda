package info.kaara.haalda;

import info.kaara.haalda.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HaaldaActivity extends Activity {

	private static final String KONELE = "ee.ioc.phon.android.speak";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isKoneleInstalled()) {
			setContentView(R.layout.main);
			final Button button = (Button) findViewById(R.id.b_first);
			button.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent guessingIntent = new Intent(HaaldaActivity.this, GuessingActivity.class);
					guessingIntent.putExtra(GuessingActivity.EXTRA_LEVEL, R.array.first_level);
					startActivity(guessingIntent);
				}
			});
			final Button button2 = (Button) findViewById(R.id.b_second);
			button2.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent guessingIntent = new Intent(HaaldaActivity.this, GuessingActivity.class);
					guessingIntent.putExtra(GuessingActivity.EXTRA_LEVEL, R.array.second_level);
					startActivity(guessingIntent);
				}
			});
			final Button button3 = (Button) findViewById(R.id.b_third);
			button3.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent guessingIntent = new Intent(HaaldaActivity.this, GuessingActivity.class);
					guessingIntent.putExtra(GuessingActivity.EXTRA_LEVEL, R.array.third_level);
					startActivity(guessingIntent);
				}
			});
		} else {
			downloadKonele();
		}
	}

	private boolean isKoneleInstalled() {
		PackageManager pm = getPackageManager();
		try {
			pm.getPackageInfo(KONELE, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}

	private void downloadKonele() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.need_konele)).setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						Uri marketUri = Uri.parse("market://details?id=" + KONELE);
						Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
						startActivity(marketIntent);
					}
				}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						HaaldaActivity.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}