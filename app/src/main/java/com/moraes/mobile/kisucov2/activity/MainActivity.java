package com.moraes.mobile.kisucov2.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Log;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.moraes.mobile.kisucov2.R;
import com.moraes.mobile.kisucov2.model.Pedido;
import com.moraes.mobile.kisucov2.model.ShowMsg;
import com.moraes.mobile.kisucov2.model.Suco;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;

    private Context mContext = null;
    private FilterOption mFilterOption = null;
    public static Printer mPrinter = null;
    String ptn = "";
    String tgn = "";
    String mac = "";
    String device ="";

    private ListView sucosMenu;
    private String[] listaMenu = { "SUCO SIMPLES","SUCO MIX","2 COMBINAÇÕES","3 COMBINAÇÕES","SUCO DETOX","VITAMINA"};
    private ArrayAdapter<String> adaptaMenu;
    final String LOCAL_IP = "TCP:192.168.0.10";
    TextView comandaView;
    TextView clienteView;
    TextView lblContadorDeItens;
    Button btnImprimirPedido;
    Button btnListarPedido;

    String comanda = "";
    String cliente = "";
    ArrayList<Suco> listaDeSuco = new ArrayList<Suco>();
    Pedido pedido = new Pedido(listaDeSuco);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // para remover ACTION BAR
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         //       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestRuntimePermission();
        mContext = this;

        btnImprimirPedido = findViewById(R.id.btnImprimir);
        btnListarPedido = findViewById(R.id.btnListItem);
        comandaView = findViewById(R.id.txtNumComandaMenu);
        clienteView = findViewById(R.id.txtClienteNomeMenu);
        lblContadorDeItens  = findViewById(R.id.lblContador);

        sucosMenu = findViewById(R.id.listViewMenu);
        adaptaMenu = new ArrayAdapter<String>( getApplicationContext(),android.R.layout.simple_list_item_activated_1,android.R.id.text1,listaMenu);
        sucosMenu.setAdapter(adaptaMenu);
        sucosMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    comanda = comandaView.getText().toString();
                    cliente = clienteView.getText().toString();
                    Intent intentSuco = new Intent(getApplicationContext(),SucoSimplesActivity.class);
                    intentSuco.putExtra("comandaGo",comanda);
                    intentSuco.putExtra("clienteGo",cliente);
                    intentSuco.putExtra("menu",position);
                    startActivityForResult(intentSuco, 0);
            }
        });


        try {
            Log.setLogSettings(mContext, Log.PERIOD_TEMPORARY, Log.OUTPUT_STORAGE, null, 0, 1, Log.LOGLEVEL_LOW);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "setLogSettings", mContext);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        while (true) {
            try {
                Discovery.stop();
                break;
            } catch (Epos2Exception e) {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                    break;
                }
            }
        }

        mFilterOption = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent dataSuco) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Suco suco = (Suco) dataSuco.getExtras().getSerializable("pedido");
            pedido.adicionaItem(suco);
            this.atualizaContador();


        }
        if ( requestCode == 3 && resultCode == RESULT_OK ){
            int x = dataSuco.getExtras().getInt("itemParaDeletar");
                pedido.removeSucoItem(x);
                this.atualizaContador();
        }
    }

    public void ImprimirPedido(View v){
        if( listaDeSuco.isEmpty() ){
            Toast.makeText(getApplicationContext(),"NÃO HA PEDIDOS PARA IMPRIMIR", Toast.LENGTH_LONG).show();


        }else {
            btnImprimirPedido.setEnabled(false);
            cliente = clienteView.getText().toString();
            comanda = comandaView.getText().toString();
            String auxName, auxNum;
            if ( cliente.length() > 21 ){
                auxName = cliente.substring(0,20);
            } else auxName = cliente;
            if ( comanda.length() > 5 ){
                auxNum = comanda.substring(0,4);
            } else auxNum = comanda;
            pedido.setCliente(auxName);
            pedido.setComanda(auxNum);
            if ( runPrintReceiptSequence() ){
                listaDeSuco = new ArrayList<Suco>();
                pedido = new Pedido(listaDeSuco);
                atualizaContador();
                lblContadorDeItens.setText("ITENS NA LISTA = 0");
                cliente = "";
                comanda = "";
                comandaView.setText(comanda);
                clienteView.setText(cliente);
                btnImprimirPedido.setEnabled(true);
            }
        }
    }


    public void atualizaContador(){
        int contador = 0;
        if ( pedido.getListaSuco().isEmpty() == false ){
            contador = pedido.getListaSuco().size();
        }else {
            contador = 0;
        }
        if ( contador > 0 ){
            String cont = String.valueOf(contador);
            StringBuilder builder = new StringBuilder();
            String temp = "ITENS NA LISTA = ";
            builder.append(temp);
            builder.append(cont);
            String novoContador = builder.toString();
            lblContadorDeItens  = findViewById(R.id.lblContador);
            lblContadorDeItens.setText(novoContador);
        }

    }

    public void listarPedidosDialog( View v){
        Intent deletaSuco = new Intent(getApplicationContext(),DeletarSuco.class);
        deletaSuco.putExtra("listaDelete",pedido.getListaSuco());

        startActivityForResult(deletaSuco, 3);

    }

    public void sair(View v){
        finish();
    }

    private boolean runPrintReceiptSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!createReceiptData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData() {
        String method = "";
        StringBuilder textData = new StringBuilder();
        ArrayList<Suco> listaAux = pedido.getListaSuco();
        int i = 0;


        if (mPrinter == null) {
            return false;
        }
        try {
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addFeedLine(1);
            mPrinter.addTextFont(Printer.FONT_A);
            mPrinter.addTextSize(2, 2);
            // Em Fonte A e tamanho 2 x 2 a linha suporta 21 caracteres
            // Em Fonte B e tamanho 2 x 2 a linha suporta 28 caracteres
            // Em Fonte B e tamanho 2 x 1 a linha suporta 28 caracteres
            // Em Fonte A e tamanho 1 x 1 a linha suporta 42 caracteres
            textData.append("CARTAO:   " + pedido.getComanda() + "\n");
            textData.append(pedido.getCliente() + "\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());

            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            mPrinter.addTextFont(Printer.FONT_A);
            mPrinter.addTextSize(2, 1);
            mPrinter.addTextStyle(0, 0, 0, 0);
            while (i < listaAux.size()) {
                Suco itemAux = listaAux.get(i);
                textData.append("---------------------\n");
                textData.append(itemAux.getQuantidade()+" X 300 ML  "+itemAux.getViagem()+"\n");
                textData.append(itemAux.getItem()+"\n");
                textData.append(itemAux.getSugarTipo()+"\n");
                textData.append(itemAux.getGelo()+"\n");
                i++;
            }
            textData.append("---------------------");

            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());

            mPrinter.addFeedLine(1);
            mPrinter.addCut(Printer.CUT_FEED);
        }
        catch (Exception e) {
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        textData = null;

        return true;
    }


    private DiscoveryListener mDiscoveryListener = new DiscoveryListener() {
        @Override
        public void onDiscovery(final DeviceInfo deviceInfo) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ptn = deviceInfo.getDeviceName();
                    tgn = deviceInfo.getTarget();
                    mac = deviceInfo.getMacAddress();
                    device = String.valueOf(deviceInfo.getDeviceType());

                }
            });
        }
    };



    private void startDiscovery() {

        mFilterOption = new FilterOption();
        mFilterOption.getDeviceType();
        mFilterOption.getPortType();
        mFilterOption.getEpsonFilter();

        try {
            Discovery.start(mContext, mFilterOption, mDiscoveryListener);
            btnImprimirPedido.setEnabled(false);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "start", mContext);
        }
    }

    private void stopDiscovery() {
        try {
            Discovery.stop();
        }
        catch (Epos2Exception e) {
            if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                ShowMsg.showException(e, "stop", mContext);
            }
        }
    }




    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Printer.TM_T88,Printer.LANG_EN,mContext);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(new ReceiveListener() {
            @Override
            public void onPtrReceive(Printer printer, final int code, final PrinterStatusInfo status, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        //ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                        dispPrinterWarnings(status);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                disconnectPrinter();
                            }
                        }).start();
                    }
                });
            }
        });

        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), mContext);
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect(LOCAL_IP, Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        }
        else if (status.getOnline() == Printer.FALSE) {
            return false;
        }
        else {
            //print available
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        TextView edtWarnings = findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        edtWarnings.setText(warningsMsg);
    }


    private void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> requestPermissions = new ArrayList<>();

        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation == PackageManager.PERMISSION_DENIED) {
            requestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION || grantResults.length == 0) {
            return;
        }

        List<String> requestPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permissions[i]);
            }
        }

        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_PERMISSION);
        }
    }


}
