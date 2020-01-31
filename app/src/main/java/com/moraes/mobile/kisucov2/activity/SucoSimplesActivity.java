package com.moraes.mobile.kisucov2.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.moraes.mobile.kisucov2.R;
import com.moraes.mobile.kisucov2.model.Suco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SucoSimplesActivity extends AppCompatActivity {
    private ListView sucosSimples;
    private String[] listaSuco = { "ABACAXI","boACEROLA","CAJÁ","CAJÚ","GOIABA","LARANJA","LIMÃO","LIMONADA SUÍCA","MAMÃO","MANGA","MARACUJÁ","MELÃO","MELANCIA","MORANGO"};  // OPCAO 0

    private String[] listaSucoMix = { "ABACAXI","ABACAXI + HORTELA","ABACAXI + LIMÃO","ABACAXI + GENGIBRE","ABACAXI + CAPIM SANTO","ACEROLA","ACEROLA + LIMÃO","CAJÁ","CAJÚ","GOIABA","LARANJA",
            "LARANJA + ABACAXI","LARANJA + ACEROLA","LARANJA + BETERRABA","LARANJA + BETERRABA + CENOURA","LARANJA + CENOURA","LARANJA + COUVE","LARANJA + GENGIBRE","LARANJA + LIMÃO","LARANJA + MAMÃO",
            "LARANJA + MARACUJÁ","LARANJA + MORANGO","LIMÃO","LIMÃO + HORTELA","LIMÃO + CAPIM SANTO","LIMÃO + GENGIBRE","LIMÃO + MELÃO","LIMONADA SUIÇA","MAMÃO","MANGA",
            "MARACUJÁ","MELÃO","MELANCIA","MELANCIA + GENGIBRE","MELANCIA + MORANGO","MORANGO"};   // OPCAO 1
    private String[] listaSucoDetox = { "DETOX BRANCO","DETOX VERDE","DETOX LARANJA","DETOX VERMELHO"};

    private String[] listaVitamina = { "VITAMINA ABACATE","VITAMINA MAMÃO","VITAMINA MISTA","VITAMINA MORANGO","MARACUJÁ + LEITE","GOIABA + LEITE","ACEROLA + LEITE"};
    private String[] listaSuco2Combo ={ "ABACAXI + CAPIM LIMÃO", "ABACAXI + HORTELA", "LARANJA + CENOURA", "LARANJA + GENGIBRE", "LARANJA + MARACUJÁ", "LARANJA + MORANGO","MELANCIA + GENGIBRE"};
    private String [] listaSuco3Combo = {"LARANJA + ABACAXI + HORTELÃ","LARANJA + CENOURA + BETERRABA", "LARANJA + CENOURA + GENGIBRE", "LARANJA + COUVE + GENGIBRE", "LIMÃO + HORTELA + CAPIM LIMÃO",
            "LIMÃO + HORTELA + COUVE", "LIMÃO + HORTELA + GENGIBRE"};


    ArrayAdapter<String> adaptaSuco;
    Bundle dadosComanda;
    String comanda1;
    String cliente1;
    int tipoMenu;
    String itemSelecionado = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suco_simples);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        dadosComanda = getIntent().getExtras();
        comanda1 = dadosComanda.getString("comandaGo");
        cliente1 = dadosComanda.getString("clienteGo");
        tipoMenu = dadosComanda.getInt("menu");

        sucosSimples = findViewById(R.id.sucoSimplesListView);


        if ( tipoMenu == 0 ){

            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaSuco);
        }
        if (tipoMenu == 1 ){
            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaSucoMix);
        }

        if (tipoMenu == 2 ){
            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaSuco2Combo);
        }
        if (tipoMenu == 3 ){
            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaSuco3Combo);
        }
        if (tipoMenu == 4 ){
            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaSucoDetox);
        }
        if (tipoMenu == 5 ){
            adaptaSuco = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listaVitamina);
        }

        sucosSimples.setAdapter(adaptaSuco);
        sucosSimples.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if ( tipoMenu == 0 ){
                    itemSelecionado = listaSuco[position];
                }
                if ( tipoMenu == 1 ){
                    itemSelecionado = listaSucoMix[position];
                }
                if ( tipoMenu == 2 ){
                    itemSelecionado = listaSuco2Combo[position];
                }
                if ( tipoMenu == 3 ){
                    itemSelecionado = listaSuco3Combo[position];
                }
                if ( tipoMenu == 4 ){
                    itemSelecionado = listaSucoDetox[position];
                }
                if ( tipoMenu == 5 ){
                    itemSelecionado = listaVitamina[position];
                }
                Intent intentDetalhes = new Intent(getApplicationContext(),DetalheSimplesActivity.class);
                intentDetalhes.putExtra("comandaTemp",comanda1);
                intentDetalhes.putExtra("clienteTemp",cliente1);
                intentDetalhes.putExtra("itemSuco",itemSelecionado);
                intentDetalhes.putExtra("tipoMenu",tipoMenu);
                startActivityForResult(intentDetalhes,1);

            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent dataOpt){
        if (requestCode == 1) {
            if (resultCode == RESULT_OK ) {
                Suco aux = new Suco();
                aux.setItem(itemSelecionado);
                aux.setTamanho( dataOpt.getExtras().getString("tamanhoAux"));
                aux.setQuantidade( dataOpt.getExtras().getString("quantidadeAux"));
                aux.setSugarTipo( dataOpt.getExtras().getString("sugarAux"));
                aux.setGelo( dataOpt.getExtras().getString("geloAux"));
                aux.setViagem( dataOpt.getExtras().getString("viagemAux"));
                Intent suco = new Intent();
                suco.putExtra("pedido",aux);
                setResult( RESULT_OK, suco );
                finish();
            }
        }

    }

    public String[] criarNovaLista( String[] a1, String[] a2 ){
        String[] lista;
        ArrayList<String> aux = new ArrayList<String>();
        int i = 0;
        while ( i < a1.length ){
            aux.add(a1[i]);
            i++;
        } i = 0;
        while ( i < a2.length ) {
            aux.add(a2[i]);
            i++;
        }
        Collections.sort(aux);
        lista = (String[]) aux.toArray();
        return lista;
    }
}
