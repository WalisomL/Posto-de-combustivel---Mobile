package com.example.projeto06_combustivel; // ATENÇÃO: COLOQUE SEU PACOTE AQUI

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AbastecimentoActivity extends AppCompatActivity {

    private EditText etData, etOdo, etLitros, etValor;
    private RadioGroup rgCombustivel;
    private CardView cardAnalise;
    private TextView txtConsumoMedio, txtCustoKm, txtMediaDia;

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "FuelTrackerPrefs";
    private static final String KEY_LAST_ODO = "lastOdo";
    private static final String KEY_LAST_DATE = "lastDate";

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento);

        vincularComponentes();
        configurarCalendario();

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(v -> analisarESalvar());

        // Ação do novo botão de Voltar
        Button btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void vincularComponentes() {
        etData = findViewById(R.id.etData);
        etOdo = findViewById(R.id.etOdo);
        etLitros = findViewById(R.id.etLitros);
        etValor = findViewById(R.id.etValor);
        rgCombustivel = findViewById(R.id.rgCombustivel);
        cardAnalise = findViewById(R.id.cardAnalise);
        txtConsumoMedio = findViewById(R.id.txtConsumoMedio);
        txtCustoKm = findViewById(R.id.txtCustoKm);
        txtMediaDia = findViewById(R.id.txtMediaDia);
    }

    private void configurarCalendario() {
        etData.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                String dateStr = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
                etData.setText(dateStr);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void analisarESalvar() {
        if (etData.getText().toString().isEmpty() || etOdo.getText().toString().isEmpty() ||
                etLitros.getText().toString().isEmpty() || etValor.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rgCombustivel.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione o tipo de combustível", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float odoAtual = Float.parseFloat(etOdo.getText().toString());
            float litros = Float.parseFloat(etLitros.getText().toString());
            float valorLitro = Float.parseFloat(etValor.getText().toString());
            String dataAtualStr = etData.getText().toString();

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            float odoAnterior = prefs.getFloat(KEY_LAST_ODO, 0);
            String dataAnteriorStr = prefs.getString(KEY_LAST_DATE, "");

            if (odoAnterior == 0 || dataAnteriorStr.isEmpty()) {
                Toast.makeText(this, "Primeiro registro salvo! O consumo será exibido no próximo abastecimento.", Toast.LENGTH_LONG).show();
                cardAnalise.setVisibility(View.GONE);
            } else {
                if (odoAtual <= odoAnterior) {
                    Toast.makeText(this, "Hodômetro atual deve ser maior que o anterior (" + odoAnterior + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                float distPercorrida = odoAtual - odoAnterior;
                float kmPorLitro = distPercorrida / litros;
                float custoPorKm = valorLitro / kmPorLitro;

                Date dataAtual = sdf.parse(dataAtualStr);
                Date dataAnterior = sdf.parse(dataAnteriorStr);

                long diffEmMilis = Math.abs(dataAtual.getTime() - dataAnterior.getTime());
                long diasCorridos = TimeUnit.DAYS.convert(diffEmMilis, TimeUnit.MILLISECONDS);
                if (diasCorridos == 0) diasCorridos = 1;

                float kmPorDia = distPercorrida / diasCorridos;

                txtConsumoMedio.setText(String.format(Locale.getDefault(), "• Consumo Médio: %.2f km/l", kmPorLitro));
                txtCustoKm.setText(String.format(Locale.getDefault(), "• Custo da Viagem: R$ %.2f por km", custoPorKm));
                txtMediaDia.setText(String.format(Locale.getDefault(), "• Média Percorrida: %.0f km por dia", kmPorDia));

                cardAnalise.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Relatório Gerado!", Toast.LENGTH_SHORT).show();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(KEY_LAST_ODO, odoAtual);
            editor.putString(KEY_LAST_DATE, dataAtualStr);
            editor.apply();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erro nos valores numéricos.", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(this, "Erro ao processar datas.", Toast.LENGTH_SHORT).show();
        }
    }
}