package be.tabtabstudio.veganapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.tabtabstudio.veganapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent k = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(k);
    }
}