package me.vermon.joomismang;

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

public class GuessingActivity extends Activity{

    private static final String TAG = GuessingActivity.class.getName();
    public static final String EXTRA_LEVEL = "EXTRA_LEVEL";
    private SpeechRecognizer spc;
    private String currentWord;
    private int level = R.array.first_level;

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        Intent intent = getIntent();
        level = intent.getIntExtra( EXTRA_LEVEL, R.array.first_level );
        setContentView( R.layout.guess );
        changeWord();
        this.spc = SpeechRecognizer.createSpeechRecognizer( this );
        spc.setRecognitionListener( rl );
        final Button speak = (Button)findViewById( R.id.b_speak );
        speak.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                listen();
                speak.setVisibility( View.GONE );
            }
        } );
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        spc.destroy();
    }

    private void listen(){
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        spc.startListening( intent );
    }

    private void changeWord(){
        currentWord = getRandomWord();
        final TextView word = (TextView)findViewById( R.id.word );
        word.setText( "\"" + currentWord + "\"" );
    }

    private String getRandomWord(){
        CharSequence[] firstLevel = getResources().getTextArray( level );
        Random r = new Random();
        int index = r.nextInt( firstLevel.length );
        return firstLevel[index].toString();
    }

    private void showDialog( boolean success, String match ){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        String message = success ? "Väga tubli. Proovid edasi?" : "Sina ütlesid " + match + ". Proovid uuesti?";
        builder.setMessage( message ).setCancelable( false ).setPositiveButton( "Jah", new DialogInterface.OnClickListener(){

            public void onClick( DialogInterface dialog, int id ){
                changeWord();
                final Button speak = (Button)findViewById( R.id.b_speak );
                speak.setVisibility( View.VISIBLE );
            }
        } ).setNegativeButton( "Ei", new DialogInterface.OnClickListener(){

            public void onClick( DialogInterface dialog, int id ){
                GuessingActivity.this.finish();
            }
        } );
        AlertDialog alert = builder.create();
        alert.show();
    }

    private RecognitionListener rl = new RecognitionListener(){

        @Override
        public void onRmsChanged( float rmsdB ){
        }

        @Override
        public void onResults( Bundle results ){
            final ProgressBar progress = (ProgressBar)findViewById( R.id.progress );
            progress.setVisibility( View.GONE );
            List<String> result = results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
            if( result != null ){
                for( String string : result ){
                    Log.d( TAG, "Result: " + string );
                    boolean success = false;
                    if( currentWord.equalsIgnoreCase( string ) ){
                        success = true;
                        Log.d( TAG, "We have a match!" );
                    }
                    showDialog( success, string );
                }
            }
            else{
                Log.d( TAG, "No results" );
            }
        }

        @Override
        public void onReadyForSpeech( Bundle params ){
        }

        @Override
        public void onPartialResults( Bundle partialResults ){
        }

        @Override
        public void onEvent( int eventType, Bundle params ){
        }

        @Override
        public void onError( int error ){
            final ProgressBar progress = (ProgressBar)findViewById( R.id.progress );
            progress.setVisibility( View.GONE );
            Log.d( TAG, "Error: " + error );
            AlertDialog.Builder builder = new AlertDialog.Builder( GuessingActivity.this );
            builder.setMessage( "Tehniline viga. Palun proovi uuesti." ).setCancelable( false ).setPositiveButton( "OK", new DialogInterface.OnClickListener(){

                public void onClick( DialogInterface dialog, int id ){
                    final Button speak = (Button)findViewById( R.id.b_speak );
                    speak.setVisibility( View.VISIBLE );
                }
            } );
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        public void onEndOfSpeech(){
            Log.d( TAG, "User stopped talking" );
            final ProgressBar progress = (ProgressBar)findViewById( R.id.progress );
            progress.setVisibility( View.VISIBLE );
        }

        @Override
        public void onBufferReceived( byte[] buffer ){
        }

        @Override
        public void onBeginningOfSpeech(){
            Log.d( TAG, "User started talking" );

        }
    };

}
