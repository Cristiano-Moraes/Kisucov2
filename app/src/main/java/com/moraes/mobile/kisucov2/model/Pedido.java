package com.moraes.mobile.kisucov2.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {
    private String comanda;
    private String cliente;
    ArrayList<Suco> listaSuco;

    public Pedido(ArrayList<Suco> lista ){
        this.cliente = "";
        this.comanda = "";
        this.listaSuco = lista;
    }

    public String getComanda() {
        return comanda;
    }

    public void setComanda(String comanda) {
        this.comanda = comanda;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Suco> getListaSuco() {
        return listaSuco;
    }

    public void setListaSuco(ArrayList<Suco> listaSuco) {
        this.listaSuco = listaSuco;
    }

    public void adicionaItem( Suco s){
        this.listaSuco.add(s);
    }

    public void removeSucoItem( int i){
        this.listaSuco.remove(i);
    }

    public void removeSucos (ArrayList<Integer> listaD){
        int i = 0;
        if (listaD.isEmpty() == false ){
            while ( i < listaD.size() ){
                this.listaSuco.remove(listaD.get(i));
                i++;
            }

        }
    }

}
