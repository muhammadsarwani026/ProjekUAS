package com.example.muhammadsarwani.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Intent2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent2);

        TextView lanjut2 = (TextView)findViewById(R.id.tvlanjut2);
        lanjut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent2.this, Intent3.class);
                startActivity(i);
            }
        });


    }
}
