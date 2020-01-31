package com.moraes.mobile.kisucov2.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.moraes.mobile.kisucov2.R;
import com.moraes.mobile.kisucov2.model.Suco;

import java.util.ArrayList;


public class AdaptadorDeSuco extends ArrayAdapter {


    private Context ctx;
    private ArrayList<Suco> listaItens;
    private ArrayList<Integer> listaParaDeletar = new ArrayList<Integer>();



    public AdaptadorDeSuco(Context context, int resource,  ArrayList<Suco> objects) {
        super(context, resource, objects);
        ctx = context;
        listaItens = objects;
    }


    @Override
    public int getCount() {
        int s = listaItens.size();
        return s;
    }

    @Override
    public Suco getItem(int position) {
        Suco aux = listaItens.get(position);
        return aux;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




    private View initView (final int position, View convertView, final ViewGroup parent){
        View initView = convertView;
        if (initView == null ){
            initView = LayoutInflater.from(ctx).inflate(R.layout.adaptador_suco_itemlist,parent,false);
        }
        Suco aux = listaItens.get(position);

        StringBuilder stb = new StringBuilder();
        stb.append(aux.getQuantidade());
        stb.append("x ");
        stb.append(aux.getItem());
        stb.append(" ");
        stb.append(aux.getTamanho());
        String resumo = stb.toString();

        TextView  auxResumo;
        auxResumo = initView.findViewById(R.id.txtItemSuco);
        auxResumo.setText(resumo);

        return initView;
    }


    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    public ArrayList<Integer> deletarItens(){
        return listaParaDeletar;
    }

}
