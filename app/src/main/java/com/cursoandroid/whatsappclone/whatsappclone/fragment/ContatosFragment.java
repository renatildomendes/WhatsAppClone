package com.cursoandroid.whatsappclone.whatsappclone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cursoandroid.whatsappclone.whatsappclone.R;
import com.cursoandroid.whatsappclone.whatsappclone.config.ConfiguracaoFirebase;
import com.cursoandroid.whatsappclone.whatsappclone.helper.Preferencias;
import com.cursoandroid.whatsappclone.whatsappclone.model.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> contatos;
    private DatabaseReference firebase;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contatos = new ArrayList<String>();
        contatos.add("Maria Silva");
        contatos.add("Leticia Almeida");
        contatos.add("Dud Dud");
        View view = inflater.inflate(R.layout.fragment_contatos,container,false);
        listView = (ListView) view.findViewById(R.id.id_listView_contatos);
        adapter = new ArrayAdapter(getActivity(),R.layout.lista_contatos,contatos);
        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase().child("contatos").child(identificadorUsuarioLogado);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contatos.clear();
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato.getNome());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
