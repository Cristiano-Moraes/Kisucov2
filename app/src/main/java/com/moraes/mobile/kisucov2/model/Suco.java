package com.moraes.mobile.kisucov2.model;

import java.io.Serializable;

public class Suco implements Serializable {


    private String item;
    private String tamanho;
    private String sugarTipo;
    private String viagem;
    private String gelo;
    private String quantidade;

    public Suco(){
        this.tamanho = "300 ML";
        this.gelo = "";
        this.viagem = "";
    }


    public Suco(String item){

        this.item = item;
        this.tamanho = "300 ML";
        this.gelo = "";
        this.viagem = "";
    }




    public void setItem(String item) {
        this.item = item;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public void setSugarTipo(String sugarTipo) {
        this.sugarTipo = sugarTipo;
    }

    public void setViagem(String viagem) {
        this.viagem = viagem;
    }

    public void setGelo(String gelo) {
        this.gelo = gelo;
    }

    public void setQuantidade(String quantidade) {

        this.quantidade = quantidade;
    }




    public String getItem() {
        return item;
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getSugarTipo() {
        return sugarTipo;
    }

    public String getViagem() {
        return viagem;
    }

    public String getGelo() {
        return gelo;
    }
    public String getQuantidade() {
        return quantidade;
    }



}
