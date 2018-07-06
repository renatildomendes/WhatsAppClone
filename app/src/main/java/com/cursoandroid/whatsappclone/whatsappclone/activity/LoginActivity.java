package com.cursoandroid.whatsappclone.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.whatsappclone.R;
import com.cursoandroid.whatsappclone.whatsappclone.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Base64Custom;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Permissao;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;
import com.cursoandroid.whatsappclone.whatsappclone.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Random;

import static com.cursoandroid.whatsappclone.whatsappclone.config.ConfiguracaoFirebase.getFirebase;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText senha;
    private Button logar;
    //private String[] permissoesNecessarias = new String[] {android.Manifest.permission.SEND_SMS, android.Manifest.permission.INTERNET};
    private Usuario usuario;
    private Button cadastrar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.id_login_email);
        senha = findViewById(R.id.id_login_senha);
        logar = findViewById(R.id.id_botao_logar);
        cadastrar = findViewById(R.id.id_botao_cadastrar);
        verificarUsuarioLogado();

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });

        /*Permissao.validaPermissoes(1,this,permissoesNecessarias);

        nome = findViewById(R.id.id_login_email);
        telefone = findViewById(R.id.id_numero_telefone);
        cod_area = findViewById(R.id.id_cod_area);
        cod_pais = findViewById(R.id.id_cod_pais);

        SimpleMaskFormatter simpleMaskCodPais = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskCodPais = new MaskTextWatcher(cod_pais,simpleMaskCodPais);
        cod_pais.addTextChangedListener(maskCodPais);

        SimpleMaskFormatter simpleMaskCodArea = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskCodArea = new MaskTextWatcher(cod_area,simpleMaskCodArea);
        cod_area.addTextChangedListener(maskCodArea);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone,simpleMaskTelefone);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar = findViewById(R.id.id_botao_cadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        cod_pais.getText().toString() +
                        cod_area.getText().toString()+
                        telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+","").replace("-","");
                //Log.i("TELEFONE", telefoneSemFormatacao);
                Random random = new Random();

                //TOKEN - GERAR EM WEBSERVICE!!!!!!!!!!!!!!!!!!!
                int numeroRandom = random.nextInt(8999)+1000;
                String token = String.valueOf(numeroRandom);
                //Log.i("TOKEN", token);
                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario,telefoneSemFormatacao,token);
                HashMap<String,String> usuario = preferencias.getDadosUsuario();
                Log.i("TOKEN","NOME: "+usuario.get("nome")+" FONE: "+ usuario.get("telefone"));
                String mensagemDeTexto = "Token para validação do WhatsApp: "+token;
                //ENVIO DO SMS
                boolean enviadoSMS = enviaSMS("+5554",mensagemDeTexto);

                if (enviadoSMS){
                    Intent intent = new Intent (LoginActivity.this,ValidadorActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Problema ao enviar SMS, tente novamente.",Toast.LENGTH_LONG).show();
                }


            }
        });*/

    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser()!= null){
            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    preferencias.salvarDados(identificadorUsuarioLogado);
                    abrirTelaPrincipal();
                    Toast.makeText(getApplicationContext(), "Sucesso ao logar ", Toast.LENGTH_SHORT).show();
                } else {
                    //LOCAL DAS EXCEÇÕES. implementar mais tarde
                    Toast.makeText(getApplicationContext(), "Erro ao Logar ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private boolean enviaSMS(String telefone, String mensagem){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null,mensagem,null,null);
            Log.i("ENVIO DO SMS PARA "+telefone,mensagem);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();

            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o aplicativo, é necessário aceitar as permissões");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void abrirCadastroUsuario(){
        Intent intent = new Intent(LoginActivity.this,CadastroUsuarioActivity.class);
        startActivity(intent);
    }

}
