package com.example.projeto06_combustivel; // ATENÇÃO: COLOQUE SEU PACOTE AQUI

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardAbastecimento = findViewById(R.id.cardAbastecimento);
        CardView cardCalculadora = findViewById(R.id.cardCalculadora);

        cardAbastecimento.setOnClickListener(v -> startActivity(new Intent(this, AbastecimentoActivity.class)));
        cardCalculadora.setOnClickListener(v -> startActivity(new Intent(this, CalculadoraActivity.class)));
    }
}