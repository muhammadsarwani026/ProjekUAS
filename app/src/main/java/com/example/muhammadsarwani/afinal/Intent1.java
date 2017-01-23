package com.example.muhammadsarwani.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Intent1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent1);

        TextView lanjut1 = (TextView)findViewById(R.id.tvlanjut1);
        lanjut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent1.this, Intent2.class);
                startActivity(i);
            }
        });
    }
}
