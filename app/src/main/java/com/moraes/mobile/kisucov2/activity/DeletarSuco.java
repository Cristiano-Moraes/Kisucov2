package com.moraes.mobile.kisucov2.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.moraes.mobile.kisucov2.R;
import com.moraes.mobile.kisucov2.model.Suco;
import java.util.ArrayList;

public class DeletarSuco extends AppCompatActivity {
    private ArrayList<Suco> listaDel;
    private ListView listaDeSucosDel;
    ArrayList<String> listaResumo;
    ArrayAdapter<String> adaptaDel;
    Bundle dadosListaDel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_lista_de_pedidos);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listaDeSucosDel = findViewById(R.id.listViewDeletarSuco);
        dadosListaDel = getIntent().getExtras();
        listaDel = (ArrayList<Suco>) dadosListaDel.getSerializable("listaDelete");
        listaResumo = criarListaResumo(listaDel);
        adaptaDel = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_single_choice,listaResumo);
        listaDeSucosDel.setAdapter(adaptaDel);
        listaDeSucosDel.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaDeSucosDel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionaItemParaExcluir(position);
            }
        });


    }
     public void selecionaItemParaExcluir(int position){
         Intent deletarSuco =  new Intent();
         deletarSuco.putExtra("itemParaDeletar",position);
         setResult( RESULT_OK, deletarSuco );
         finish();
     }
     public ArrayList<String> criarListaResumo (ArrayList<Suco> l ){
        ArrayList<String> listaR = new ArrayList<String>();
        int i = 0;
        while ( i < l.size() ) {
            Suco aux = l.get(i);
            StringBuilder stb = new StringBuilder();
            stb.append(aux.getQuantidade());
            stb.append("x ");
            stb.append(aux.getItem());
            stb.append(" ");
            stb.append(aux.getTamanho());
            String resumo = stb.toString();
            listaR.add(resumo);
            i++;
        }
        return listaR;

     }
}
