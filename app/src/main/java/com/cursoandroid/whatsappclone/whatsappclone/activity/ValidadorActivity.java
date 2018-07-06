package com.cursoandroid.whatsappclone.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.whatsappclone.R;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

//NOTA: DAR ATENCAO AO VALIDADOR

public class ValidadorActivity extends AppCompatActivity {

    private EditText cod_validacao;
    private Button botao_validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        cod_validacao = findViewById(R.id.id_cod_validacao);
        botao_validar = findViewById(R.id.id_botao_validar);

        SimpleMaskFormatter simpleMaskCodValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskCodValidacao = new MaskTextWatcher(cod_validacao,simpleMaskCodValidacao);
        cod_validacao.addTextChangedListener(maskCodValidacao);

        botao_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);

                /*HashMap<String,String>  usuario = preferencias.getDadosUsuario();
                String tokenGerado = usuario.get("token");
                String tokenDigitado = cod_validacao.getText().toString();

                if(tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this, "Token Validado",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ValidadorActivity.this, "Token Não Validado",Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }
}
