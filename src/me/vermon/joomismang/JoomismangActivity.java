package me.vermon.joomismang;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoomismangActivity extends Activity{

    private List<String> players = new ArrayList<String>();

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        final Button button = (Button)findViewById( R.id.b_one_player );
        button.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                getPlayerNames( 1 );
            }
        } );
        final Button button2 = (Button)findViewById( R.id.b_two_players );
        button2.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                getPlayerNames( 2 );
            }
        } );
    }
    
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setContentView( R.layout.main );
//    }

    private void getPlayerNames( int playerCount ){
        setContentView( R.layout.name );
        TextView playerNameText = (TextView)findViewById( R.id.textView1 );
        playerNameText.setText( "Sisesta esimese mängija nimi:" );
        final EditText playerName = (EditText)findViewById( R.id.player );
        playerName.setText( "Mängija1" );
        final Intent guessingIntent = new Intent( this, GuessingActivity.class );
        final Button insertName = (Button)findViewById( R.id.b_insert_name );
        insertName.setOnClickListener( new View.OnClickListener(){

            public void onClick( View v ){
                players.add( playerName.getText().toString() );
                startActivity( guessingIntent );
            }
        } );
    }

}