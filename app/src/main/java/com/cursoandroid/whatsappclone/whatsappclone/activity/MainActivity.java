package com.cursoandroid.whatsappclone.whatsappclone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
        import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

        import com.cursoandroid.whatsappclone.whatsappclone.R;
import com.cursoandroid.whatsappclone.whatsappclone.adapter.TabAdapter;
import com.cursoandroid.whatsappclone.whatsappclone.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Base64Custom;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;
import com.cursoandroid.whatsappclone.whatsappclone.helper.SlidingTabLayout;
import com.cursoandroid.whatsappclone.whatsappclone.model.Contato;
import com.cursoandroid.whatsappclone.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Button botao_sair;
    private FirebaseAuth auth;
    //private Toolbar toolbar;
    private android.support.v7.widget.Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private DatabaseReference firebase;

    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.st1_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //configurar adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.id_item_sair:
                deslogarUsuario();
                return true;
            case  R.id.id_item_configuracoes:
                //deslogarUsuario();
                return true;
            case R.id.id_item_adicionar:
                abrirCadastroContato();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirCadastroContato() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do contato");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String emailContato = editText.getText().toString();
                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha o e-mail do contato",Toast.LENGTH_SHORT).show();
                }else{
                    identificadorContato = Base64Custom.codificarBase64(emailContato);
                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase = firebase.child("usuarios").child(identificadorContato);
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                //recupera dados do contato a ser adicionado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();
                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase = firebase.child("contatos").child(identificadorUsuarioLogado).child(identificadorContato);

                                Contato contato = new Contato();
                                contato.setIdentificadorContato(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                firebase.setValue(contato);
                            }else{
                                Toast.makeText(MainActivity.this,"Usuario não tem cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void deslogarUsuario() {

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        finish();
    }

    private void voltarParaLogin() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
