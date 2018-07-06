package com.cursoandroid.whatsappclone.whatsappclone.activity;

import android.content.Intent;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.whatsappclone.whatsappclone.R;
import com.cursoandroid.whatsappclone.whatsappclone.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Base64Custom;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;
import com.cursoandroid.whatsappclone.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button cadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.id_cadastrar_nome);
        email = findViewById(R.id.id_cadastrar_email);
        senha = findViewById(R.id.id_cadastrar_senha);

        cadastrar = findViewById(R.id.id_botao_cadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        if(     usuario.getNome().isEmpty() ||
                usuario.getEmail().isEmpty() ||
                usuario.getSenha().isEmpty()){
            Toast.makeText(getApplicationContext(),"Para efetuar o cadastro, você precisa preencher todos os campos nessa tela.",Toast.LENGTH_SHORT).show();
            return;
        }
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Sucesso ao cadastrar usuário",Toast.LENGTH_SHORT).show();
                    //FirebaseUser usuarioFirebase = task.getResult().getUser();
                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);

                    preferencias.salvarDados(identificadorUsuario);

                    abrirLoginUsuario();

                    //autenticacao.signOut();
                    //finish();
                }else{
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Sua senha é muito fraca";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Seu e-mail não foi digitado corretamente";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Seu e-mail já está cadastrado no aplicativo";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Erro ao cadastrar usuário: "+erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
