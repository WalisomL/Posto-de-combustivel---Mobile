package com.example.projeto06_combustivel; // ATENÇÃO: COLOQUE SEU PACOTE AQUI

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        EditText etPrecoEtanol = findViewById(R.id.etPrecoEtanol);
        EditText etPrecoGasolina = findViewById(R.id.etPrecoGasolina);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        TextView txtResultado = findViewById(R.id.txtResultado);

        btnCalcular.setOnClickListener(v -> {
            String strEtanol = etPrecoEtanol.getText().toString();
            String strGasolina = etPrecoGasolina.getText().toString();

            if (strEtanol.isEmpty() || strGasolina.isEmpty()) {
                Toast.makeText(this, "Preencha os dois valores", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float precoEtanol = Float.parseFloat(strEtanol);
                float precoGasolina = Float.parseFloat(strGasolina);

                if (precoEtanol <= (precoGasolina * 0.70)) {
                    txtResultado.setText("Abasteça com ETANOL");
                    txtResultado.setTextColor(0xFF10B981); // Verde
                } else {
                    txtResultado.setText("Abasteça com GASOLINA");
                    txtResultado.setTextColor(0xFF3B82F6); // Azul
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do novo botão de Voltar
        Button btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());
    }
}