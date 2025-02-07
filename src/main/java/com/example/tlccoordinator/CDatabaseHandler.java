package com.example.tlccoordinator;

import com.example.tlccoordinator.controllers.CBaseController;

import java.sql.Connection;
import java.sql.DriverManager;


public class CDatabaseHandler {
    public static Connection connection;

    private static String url = "jdbc:mysql://localhost:3306/coordinamento_tlc";
    //String url = "jdbc:ucanaccess://C://Users//matimatu//Fede//TLC//Coordinamento//Database//TLCdati.accdb"; //OLD ACCESS DB
    private static String user = "root";
    private static String password = "Mendilip98";
    // <editor-fold desc="db name fields">
    // <editor-fold desc="persone">
    public static String persone_TABLE ="Persone";
    public static String persone_ID ="IDPersona";
    public static String persone_nome ="Nome";
    public static String persone_cognome ="Cognome";
    public static String persone_id_citta ="ID_Citta";
    public static String persone_dataDiNascita ="DataDiNascita";
    public static String persone_indirizzo ="Indirizzo";
    public static String persone_id_parrocchia1 ="ID_Parrocchia1";
    public static String persone_id_parrocchia2 ="ID_Parrocchia2";
    public static String persone_uomo ="Uomo";
    public static String persone_telefono ="Telefono";
    public static String persone_id_gruppo1 ="ID_Gruppo1";
    public static String persone_id_gruppo2 ="ID_Gruppo2";
    public static String persone_cellulare ="Cellulare";
    public static String persone_email ="Email";
    public static String persone_suonatore ="Suonatore";
    public static String persone_sacerdote ="Sacerdote";
    public static String persone_cuoco ="Cuoco";
    public static String persone_deceduto ="Deceduto";
    public static String persone_annotazioni ="Annotazioni";
    public static String persone_criticita ="Criticita";
    public static String persone_presentatore ="presentatore";

    //</editor-fold>

    // <editor-fold desc="eventiperpersone">
    public static String eventiperpersone_TABLE ="EventiPerPersone";
    public static String eventiperpersone_ID ="IDEventoPerPersone";
    public static String eventiperpersone_id_persona ="ID_Persona";
    public static String eventiperpersone_id_evento ="ID_Evento";
    public static String eventiperpersone_id_ruolo ="ID_Ruolo";
    public static String eventiperpersone_id_ruoloSpecifico ="ID_RuoloSpecifico";

    //</editor-fold>

    // <editor-fold desc="eventi">
    public static String eventi_TABLE ="Eventi";
    public static String eventi_ID ="IDEvento";
    public static String eventi_nome ="Nome";
    public static String eventi_dataInizio ="Datainizio";
    public static String eventi_dataFine ="DataFine";
    public static String eventi_id_luogo ="ID_Luogo";
    public static String eventiperpersone_numeroEdizione ="NumeroEdizione";

    //</editor-fold>

    // <editor-fold desc="ruoli">
    public static String ruoli_TABLE ="Ruoli";
    public static String ruoli_ID ="IDRuolo";
    public static String ruoli_nome ="Nome";
    //</editor-fold>

    // <editor-fold desc="ruoliSpecifici">
    public static String ruoliSpecifici_TABLE ="RuoliSpecifici";
    public static String ruoliSpecifici_ID ="IDRuoloSpecifico";
    public static String ruoliSpecifici_nome ="Nome";
    //</editor-fold>

    // <editor-fold desc="citta">
    public static String citta_TABLE ="Citta";
    public static String citta_ID ="IDCitta";
    public static String citta_id_provincia ="ID_Provincia";
    public static String citta_cap ="CAP";
    public static String citta_area ="Area";
    public static String citta_zona ="Zona";
    //</editor-fold>

    // <editor-fold desc="province">
    public static String province_TABLE ="Province";
    public static String province_ID ="IDProvincia";
    public static String province_id_regione ="ID_Regione";
    public static String province_sigla ="Sigla";
    public static String province_nome ="Nome";
    //</editor-fold>

    //</editor-fold>
    public static Connection getConnection() {

        try
        {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e){
            CBaseController.showAlert("Errore","Problema durante la connessione al database",e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
