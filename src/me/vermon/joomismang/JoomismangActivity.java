package me.vermon.joomismang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoomismangActivity extends Activity{


    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        final Button button = (Button)findViewById( R.id.b_first );
        button.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                Intent guessingIntent = new Intent(JoomismangActivity.this, GuessingActivity.class);
                guessingIntent.putExtra( GuessingActivity.EXTRA_LEVEL, R.array.first_level );
                startActivity( guessingIntent );
            }
        } );
        final Button button2 = (Button)findViewById( R.id.b_second );
        button2.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                Intent guessingIntent = new Intent(JoomismangActivity.this, GuessingActivity.class);
                guessingIntent.putExtra( GuessingActivity.EXTRA_LEVEL, R.array.second_level );
                startActivity( guessingIntent );
            }
        } );
    }

}