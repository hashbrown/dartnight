package com.myinterwebspot.app.dartnight;

import com.myinterwebspot.app.dartnight.db.DBHelper;
import com.myinterwebspot.app.dartnight.model.Player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditPlayerActivity extends Activity {
	
	private DBHelper db;
	private Player player;
	private EditText firstName;
	private EditText lastName;
	private EditText shortName;
	private Button saveBtn;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(getApplicationContext());
        
        final Intent intent = getIntent();
        String playerId = intent.getStringExtra("playerId");
        if(playerId != null){
        	player = db.getPlayer(playerId);        	
        } else {
        	player = new Player();
        }
        
        setContentView(R.layout.edit_player);
        initViews();
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		this.db.close();
	}

	private void initViews() {
		firstName = (EditText)findViewById(R.id.EditTextFirstName);
		lastName = (EditText)findViewById(R.id.EditTextLastName);
		shortName = (EditText)findViewById(R.id.EditTextShortName);
		saveBtn = (Button)findViewById(R.id.save_button);
		
		firstName.setText(player.getFirstName());
		lastName.setText(player.getLastName());
		shortName.setText(player.getNickName());
		
		saveBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				savePlayer();
				EditPlayerActivity.this.setResult(RESULT_OK);
				EditPlayerActivity.this.finish();
				//startActivity(new Intent(EditPlayerActivity.this,ManagePlayersActivity.class));
			}
		});
		
	}

	protected void savePlayer() {
		
		player.setFirstName(firstName.getText().toString());
		player.setLastName(lastName.getText().toString());
		player.setNickName(shortName.getText().toString());
		
		this.db.savePlayer(player);
		
	}

}
