package com.moraes.mobile.kisucov2.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.moraes.mobile.kisucov2.R;


public class DetalheSimplesActivity extends AppCompatActivity {
    private String[] listaQtde = {"1","2","3","4","5"};
    private String[] listaAdocantes0 = {"AÇÚCAR","POUCO AÇÚCAR","NATURAL","SEM AÇÚCAR C/ GELO","ADOÇANTE","POUCO ADOÇANTE"};
    private String[] listaAdocantes1 = {"AÇÚCAR","POUCO AÇÚCAR","NATURAL","SEM AÇÚCAR C/ GELO","ADOÇANTE","POUCO ADOÇANTE","LEITE CONDENSADO"};
    private String[] listaOpt1 = {"ADICIONAR","BANANA","BETERRADA","CAPIM SANTO","CENOURA","COUVE","GENGIBRE","HORTELÃ","MAÇÃ"};
    private String[] listaAdocantes;

    Spinner spOpt1;
    Spinner spQte;
    Spinner spLstAdocante;
    ArrayAdapter<String> adapterQtde;
    ArrayAdapter<String> adapterAdocantes;
    ArrayAdapter<String> adapOpt1;
    Bundle dadosPedido;
    String comanda = "0";
    String cliente = "0";
    String item ="0";
    int tipoMenu = -1;
    String quantidade = "1";
    String tpAdocante = " erro ";
    TextView detalheComanda;
    TextView detalheCliente;
    TextView detalheSuco;
    Button btnConfirmar;
    TextView auxAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_simples);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnConfirmar = findViewById(R.id.btnOK);

        dadosPedido = getIntent().getExtras();
        comanda = dadosPedido.getString("comandaTemp");
        cliente = dadosPedido.getString("clienteTemp");
        item = dadosPedido.getString("itemSuco");
        tipoMenu = dadosPedido.getInt("tipoMenu");

        detalheComanda = findViewById(R.id.txtCartaoNum);
        String tagCartaoNumb = "CARTÃO: "+comanda;
        detalheComanda.setText(tagCartaoNumb);

        detalheCliente = findViewById(R.id.txtNomeCliente);
        detalheCliente.setText(cliente);

        detalheSuco = findViewById(R.id.txtSucoSimplesNome);
        detalheSuco.setText(item);

        spQte = findViewById(R.id.spQtdeSS);
        spLstAdocante = findViewById(R.id.spListaAdocantes);
        spOpt1 = findViewById(R.id.spOpt1);

        if( tipoMenu == 0 ){
            listaAdocantes = listaAdocantes0;
            spOpt1.setEnabled(false);
            auxAdd = findViewById(R.id.lblOpt1);
            auxAdd.setText("");
            auxAdd.setEnabled(false);
        }else {
            listaAdocantes = listaAdocantes1;
        }
        adapterAdocantes = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,listaAdocantes);
        adapterAdocantes.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        adapterQtde = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,listaQtde);
        adapterQtde.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        adapOpt1 = new ArrayAdapter<String>( this,android.R.layout.simple_spinner_dropdown_item,listaOpt1);
        adapOpt1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spLstAdocante.setAdapter(adapterAdocantes);
        spQte.setAdapter(adapterQtde);
        spOpt1.setAdapter(adapOpt1);
        spLstAdocante.setSelection(3);

        spLstAdocante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tpAdocante = listaAdocantes1[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tpAdocante = "NATURAL C/ GELO";
            }
        });

        spQte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantidade = listaQtde[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                quantidade = "1";
            }
        });



    }


    public void confirmaPedido(View v){

        String viagem = "";
        String gelo ="";
        CheckBox viagemCheck = findViewById(R.id.ckbViagem);
        CheckBox geloCheck = findViewById(R.id.ckbSemGelo);
        ToggleButton tgbTamanho = findViewById(R.id.tgbTamSuco);
        if ( viagemCheck.isChecked() ){
            viagem = viagemCheck.getText().toString();
        }
        if ( geloCheck.isChecked() ){
            gelo = geloCheck.getText().toString();
        }

        Intent raiz = new Intent();
        raiz.putExtra("quantidadeAux",quantidade );
        raiz.putExtra("tamanhoAux",tgbTamanho.getText().toString());
        raiz.putExtra("sugarAux",tpAdocante);
        raiz.putExtra("geloAux",gelo);
        raiz.putExtra("viagemAux",viagem);
        setResult( RESULT_OK, raiz);
        finish();


    }
}
