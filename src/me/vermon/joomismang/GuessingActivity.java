package me.vermon.joomismang;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class GuessingActivity extends Activity{
    
    private static final String TAG = GuessingActivity.class.getName();
    private SpeechRecognizer spc;

    
    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.guess );
        this.spc = SpeechRecognizer.createSpeechRecognizer( this );
        spc.setRecognitionListener( rl );
        final Button speak = (Button)findViewById( R.id.b_speak );
        speak.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                guess();
            }
        } );
    }
    
    public void guess(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Räägi raisk" );
        spc.startListening( intent );
    }

    private RecognitionListener rl = new RecognitionListener(){

        @Override
        public void onRmsChanged( float rmsdB ){
        }

        @Override
        public void onResults( Bundle results ){
            List<String> result = results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
            if( result != null ){
                for( String string : result ){
                    Log.d( TAG, "Result: " + string );
                }
            }
            else {
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
            Log.d( TAG, "Error: "+error );

        }

        @Override
        public void onEndOfSpeech(){
            Log.d( TAG, "User stopped talking" );

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
