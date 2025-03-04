package com.example.tlccoordinator.controllers;

import com.example.tlccoordinator.CErrorHolder;
import com.example.tlccoordinator.CExcelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.tlccoordinator.CDatabaseHandler.*;

public class CControllerImportazioni extends CBaseController{

    @FXML
    private Label labelResultTitle;
    @FXML
    private Label labelResultDetails;
    @FXML
    private TextField numEventTextField;

    private final int MONTAGGIO_COLUMN_NUMBER_CORSISTI = 19;
    private final int MONTAGGIO_COLUMN_NUMBER_RESPONSABILI = 18;
    private final int MONTAGGIO_COLUMN_NUMBER_CUCINA = 14;
    private final int MONTAGGIO_COLUMN_NUMBER_CAMBIO = 13;

    //fields data

    public static class CustomMap<K, V> extends HashMap<K, V> {     //because I need it only for the indexes, and if is 0, then I can handle it easier
        @Override
        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                return (V) Integer.valueOf(0); // Return 0 if the value is null
            }
            return value;
        }
    }

    private static final Map<String, Integer> columnIndices = new CustomMap<>();

    // Initialize the map with column names and their default indices
    private void initializeColumnIndices() {
        columnIndices.put(colonnaNome, 0);
        columnIndices.put(colonnaCognome, 0);
        columnIndices.put(colonnaDataDiNascita, 0);
        columnIndices.put(colonnaCitta, 0);
        columnIndices.put(colonnaIndirizzo, 0);
        columnIndices.put(colonnaParrocchia1, 0);
        columnIndices.put(colonnaParrocchia2, 0);
        columnIndices.put(colonnaGruppo1, 0);
        columnIndices.put(colonnaGruppo2, 0);
        columnIndices.put(colonnaUomo, 0);
        columnIndices.put(colonnaCellulare, 0);
        columnIndices.put(colonnaEmail, 0);
        columnIndices.put(colonnaAnnotazioni, 0);
        columnIndices.put(colonnaSuonatore, 0);
        columnIndices.put(colonnaCuoco, 0);
        columnIndices.put(colonnaSacerdote, 0);
        columnIndices.put(colonnaConsacrato, 0);
        columnIndices.put(colonnaFrateOSuora, 0);
        columnIndices.put(colonnaSposato, 0);
        columnIndices.put(colonnaProfessore, 0);
        columnIndices.put(colonnaPresentatore, 0);
        columnIndices.put(colonnaCapo, 0);
        columnIndices.put(colonnaTestimonianzaDiCoppia, 0);
        columnIndices.put(colonnaTestimonianzaCarita, 0);
        columnIndices.put(colonnaSpallaSacerdote, 0);
        columnIndices.put(colonnaVice, 0);
        columnIndices.put(colonnaGestoreCappellina, 0);
    }

    private static final String colonnaNome = "nome";
    private static final String colonnaCognome = "cognome";
    private static final String colonnaDataDiNascita = "datadinascita";
    private static final String colonnaCitta = "cittadiresidenza";
    private static final String colonnaIndirizzo = "indirizzo";
    private static final String colonnaParrocchia1 = "parrocchia1";
    private static final String colonnaParrocchia2 = "parrocchia2";
    private static final String colonnaGruppo1 = "gruppo1";
    private static final String colonnaGruppo2 = "gruppo2";
    private static final String colonnaUomo = "sesso";
    private static final String colonnaCellulare = "cellulare";
    private static final String colonnaEmail = "mail";
    private static final String colonnaAnnotazioni = "annotazioni";
    private static final String colonnaSuonatore = "suonatore";
    private static final String colonnaCuoco = "cuoco";
    private static final String colonnaSacerdote = "sacerdote";
    private static final String colonnaConsacrato = "consacrato";
    private static final String colonnaFrateOSuora = "frateosuora";
    private static final String colonnaSposato = "sposato";
    private static final String colonnaProfessore = "professore";
    private static final String colonnaPresentatore = "presentatore";

    private static final String colonnaCapo = "capo";
    private static final String colonnaTestimonianzaDiCoppia = "coppia";
    private static final String colonnaTestimonianzaCarita = "carita";
    private static final String colonnaSpallaSacerdote = "spallasacerdote";
    private static final String colonnaVice = "vice";
    private static final String colonnaGestoreCappellina = "gestorecappelletta";

//    private int indexNome = 0;
//    private int indexCognome= 0;
//    private int indexDataDiNascita = 0;
//    private int indexCitta= 0;
//    private int indexIndirizzo= 0;
//    private int indexParrocchia1 = 0;
//    private int indexParrocchia2 = 0;
//    private int indexUomo= 0;
//    private int indexCellulare = 0;
//    private int indexEmail= 0;
//    private int indexAnnotazioni = 0;
//    private static int indexSuonatore = 0;         //vale sia come dato per corsisti, sia come dato equipe (ruolo specifico dell'evento)
//    private static int indexCuoco = 0;             //vale sia come dato per corsisti, sia come dato equipe (ruolo specifico dell'evento)
//    private static int indexSacerdote = 0;         //vale sia come dato per corsisti, sia come dato equipe (ruolo specifico dell'evento)
//    private static int indexConsacrato = 0;        //vale sia come dato per corsisti, sia come dato equipe (ruolo specifico dell'evento)
//    private static int indexFrateSuora = 0;
//    private int indexSposato = 0;
//    private int indexProfessore = 0;
//    private int indexPresentatore= 0;
//    private int indexGruppo1 = 0;
//    private int indexGruppo2 = 0;
//
//    private static int indexCapo =0;                       //utilizzato solo per le 3 equipe
//    private static int indexTestimonianzaDiCoppia = 0;     //utilizzato solo per responsabili
//    private static int indexTestimonianzaCarita = 0;       //utilizzato solo per responsabili
//    private static int indexSpallaSacerdote = 0;           //utilizzato solo per responsabili
//    private static int indexVice = 0;                      //utilizzato solo per responsabili
//    private static int indexGestoreCappellina = 0;           //utilizzato solo per il cambio



    private enum ImportationType {
        Corsisti,
        Responsabili,
        Cucina,
        Cambio
    }

    private enum RuoloSpecifico
    {
        CAPO(columnIndices.get(colonnaCapo), ruoliSpecifici_nome__capo),
        VICE(columnIndices.get(colonnaVice), ruoliSpecifici_nome__vice),
        CUOCO(columnIndices.get(colonnaCuoco), ruoliSpecifici_nome__cuoco),
        CONSACRATO(columnIndices.get(colonnaConsacrato), ruoliSpecifici_nome__consacrato),
        SACERDOTE(columnIndices.get(colonnaSacerdote), ruoliSpecifici_nome__sacerdote),
        FRATE_SUORA(columnIndices.get(colonnaFrateOSuora), ruoliSpecifici_nome__frateOSuora),
        TESTIMONIANZA_CARITA(columnIndices.get(colonnaTestimonianzaCarita), ruoliSpecifici_nome__testimonianzaCarita),
        TESTIMONIANZA_COPPIA(columnIndices.get(colonnaTestimonianzaDiCoppia), ruoliSpecifici_nome__testimonianzaCoppia),
        SPALLA_SACERDOTE(columnIndices.get(colonnaSpallaSacerdote), ruoliSpecifici_nome__spallaSacerdote),
        SUONATORE(columnIndices.get(colonnaSuonatore), ruoliSpecifici_nome__suonatore),
        GESTORE_CAPPELLINA(columnIndices.get(colonnaGestoreCappellina), ruoliSpecifici_nome__gestoreCappellina);

        private final int index;
        private final String nome;

        RuoloSpecifico(int index, String nome) {
            this.index = index;
            this.nome = nome;
        }
        public int getIndex() {
            return index;
        }

        public String getNome() {
            return nome;
        }
    }
    /**
     * Handles the file import button action.
     * @param event          The action event.
     * @param importationType The type of data to import (Corsisti, Responsabili,Cucina,Cambio).
     */
    public void importFileButtonOnAction(ActionEvent event,ImportationType importationType) {
        int eventNumber = 0;
        if(numEventTextField.getText().isEmpty())
        {
            CBaseController.showAlert("Errore","Numero TLC mancante","Manca il numero del tlc! Inseriscilo nell'apposito spazio");
            return;
        }
        try {
            eventNumber = Integer.parseInt(numEventTextField.getText());
        }catch (NumberFormatException e) {
            CBaseController.showAlert("Errore","Numero tlc non numerico",e.getMessage());
            return;
        }
        String error = checkNumeroEdizioneEvento(eventNumber,EventType.TLC);
        if(!error.isEmpty()) {
            CBaseController.showAlert("Errore","Numero edizione evento non valido",error);
            return;
        }
        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory("initialPath");
        fileChooser.setTitle("Scegli il file excel per l'importazione!");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls","*.xlsx"));

        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        if(selectedFile == null)
        {
            labelResultTitle.setText("File non esistente,riprovare");
            Color red = Color.rgb(234, 36,36);
            labelResultTitle.setTextFill(red);
            return;
        }
//        String path = "C://Users//matimatu//Fede//TLC//Coordinamento//testDatiMontaggioTLC.xlsx";
        String path = selectedFile.getAbsolutePath();
        CErrorHolder errorHolder = new CErrorHolder();

        Pair<List<Row>, HashMap<String, Integer>> infoPair;
        //TODO gestire il tutto dentro uno switch (currEventType)
        switch (importationType)
        {
            case Corsisti -> {
                infoPair = CExcelHandler.getRowListFromExcel(path,importationType.toString(),MONTAGGIO_COLUMN_NUMBER_CORSISTI,errorHolder);
            }
            case Responsabili -> {
                infoPair = CExcelHandler.getRowListFromExcel(path,importationType.toString(),MONTAGGIO_COLUMN_NUMBER_RESPONSABILI,errorHolder);
            }
            case Cucina -> {
                infoPair = CExcelHandler.getRowListFromExcel(path,importationType.toString(),MONTAGGIO_COLUMN_NUMBER_CUCINA,errorHolder);
            }
            case Cambio -> {
                infoPair = CExcelHandler.getRowListFromExcel(path,importationType.toString(),MONTAGGIO_COLUMN_NUMBER_CAMBIO,errorHolder);
            }
            default -> throw new IllegalStateException("Unexpected value: " + importationType);
        }
        if(!errorHolder.getErrorString().isEmpty())
        {
            CBaseController.showAlert("Errore","Problema durante la lettura del file excel selezionato",
                    errorHolder.getErrorString());
            return;
        }
        System.out.println("readExcel eseguito");

        int totalNumOfInsertionsPersone = 0;
        int totalNumOfInsertionsEventiPerPersone = 0;
        int totalNumOfQueriesToDo = 0;              //total number of queries in general (to be executed)
        StringBuilder sbErrors = new StringBuilder();
        StringBuilder sbNotify = new StringBuilder();
        List<Row> listRows = infoPair.getKey();
        HashMap<String, Integer> mapColumnsOrdered = infoPair.getValue();

        String errorHeaderString_colonne = "Problema durante l'analisi delle colonne";
        String errorTitleString = "Importazione annullata";

        //correcting column indexes
        for (Map.Entry<String,Integer> entry : mapColumnsOrdered.entrySet()) {
            String columnName = entry.getKey();
            int columnIndex = entry.getValue();

            setColumnIndex(columnName, columnIndex,importationType,errorTitleString,errorHeaderString_colonne);
        }



        //correcting column indexes
//        for(Map.Entry<String,Integer> entry : mapColumnsOrdered.entrySet())
//        {
//            String columnName = entry.getKey();
//            int columnIndex = entry.getValue();
//            String errorHeaderString_colonne = "Problema durante l'analisi delle colonne";
//            String errorTitleString = "Importazione annullata";
//            String errorBodyString_colonne = "Titolo colonna "+ columnName + " non valido per " + importationType;
//
//
//            switch (columnName.toLowerCase().replace(" ", ""))
//            {
//                //<editor-fold desc="columns in common">"
//                case colonnaNome:
//                    indexNome = columnIndex;
//                    break;
//                case colonnaCognome:
//                    indexCognome = columnIndex;
//                    break;
//                case colonnaDataDiNascita:
//                    indexDataDiNascita = columnIndex;
//                    break;
//                case colonnaCitta:
//                    indexCitta = columnIndex;
//                    break;
//                case colonnaIndirizzo:
//                    indexIndirizzo = columnIndex;
//                    break;
//                case colonnaParrocchia1:
//                    indexParrocchia1 = columnIndex;
//                    break;
//                case colonnaParrocchia2:
//                    indexParrocchia2 = columnIndex;
//                    break;
//                case colonnaGruppo1:
//                    indexGruppo1 = columnIndex;
//                    break;
//                case colonnaGruppo2:
//                    indexGruppo2 = columnIndex;
//                    break;
//                case colonnaCellulare:
//                    indexCellulare = columnIndex;
//                    break;
//                case colonnaEmail:
//                    indexEmail = columnIndex;
//                    break;
//                case colonnaAnnotazioni:
//                    indexAnnotazioni = columnIndex;
//                    break;
//                //</editor-fold>
//                default:
//                    //<editor-fold desc="specific columns">"
//                    switch (importationType)
//                    {
//                        case Corsisti:
//                            switch (columnName.toLowerCase().replace(" ", ""))
//                            {
//                                case colonnaUomo:
//                                    indexUomo = columnIndex;
//                                    break;
//                                case colonnaPresentatore:
//                                    indexPresentatore = columnIndex;
//                                    break;
//                                case colonnaSposato:
//                                    indexSposato = columnIndex;
//                                    break;
//                                case colonnaFrateOSuora:
//                                    indexFrateSuora = columnIndex;
//                                    break;
//                                case colonnaCuoco:
//                                    indexCuoco = columnIndex;
//                                    break;
//                                case colonnaConsacrato:
//                                    indexConsacrato = columnIndex;
//                                    break;
//                                case colonnaSacerdote:
//                                    indexSacerdote = columnIndex;
//                                    break;
//                                case colonnaProfessore:
//                                    indexProfessore = columnIndex;
//                                    break;
//                                case colonnaSuonatore:
//                                    indexSuonatore = columnIndex;
//                                    break;
//                                default:
//                                    CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                    return;
//                            }
//                            break;
//                        case Responsabili:
//                        case Cucina:
//                        case Cambio:
//                            switch (columnName.toLowerCase().replace(" ", ""))
//                            {
//                                //<editor-fold defaultstate="collapsed" desc="columns in common">"
//                                case colonnaCapo:
//                                    indexCapo = columnIndex;
//                                    break;
//                                //</editor-fold>
//                                default:
//                                    switch (importationType) {
//                                        case Responsabili -> {
//                                            switch (columnName.toLowerCase().replace(" ", ""))
//                                            {
//                                                case colonnaSacerdote:
//                                                    indexSacerdote= columnIndex;
//                                                    break;
//                                                case colonnaSpallaSacerdote:
//                                                    indexSpallaSacerdote = columnIndex;
//                                                    break;
//                                                case colonnaConsacrato:
//                                                    indexConsacrato = columnIndex;
//                                                    break;
//                                                case colonnaVice:
//                                                    indexVice = columnIndex;
//                                                    break;
//                                                case colonnaTestimonianzaDiCoppia:
//                                                    indexTestimonianzaDiCoppia = columnIndex;
//                                                    break;
//                                                case colonnaTestimonianzaCarita:
//                                                    indexTestimonianzaCarita = columnIndex;
//                                                    break;
//                                                case colonnaSuonatore:
//                                                    indexSuonatore = columnIndex;
//                                                    break;
//                                                default:
//                                                    CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                                    return;
//                                            }
//                                        }
//                                        case Cucina -> {
//                                            switch (columnName.toLowerCase().replace(" ", ""))
//                                            {
//                                                case colonnaSacerdote:
//                                                    indexSacerdote= columnIndex;
//                                                    break;
//                                                case colonnaCuoco:
//                                                    indexCuoco = columnIndex;
//                                                    break;
//                                                case colonnaSuonatore:
//                                                    indexSuonatore = columnIndex;
//                                                    break;
//                                                default:
//                                                    CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                                    return;
//                                            }
//                                            //se si è fermato prima vuol dire che c'è qualche colonna che non c'entra!
//                                            CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                            return;
//                                        }
//                                        case Cambio -> {
//                                            switch (columnName.toLowerCase().replace(" ", ""))
//                                            {
//                                                case colonnaGestoreCappellina:
//                                                    indexGestoreCappellina = columnIndex;
//                                                    break;
//                                                default:
//                                                    CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                                    return;
//                                            }
//                                            //se si è fermato prima vuol dire che c'è qualche colonna che non c'entra!
//                                            CBaseController.showAlert(errorTitleString,errorHeaderString_colonne,errorBodyString_colonne);
//                                            return;
//                                        }
//                                    }
//                            }
//                            break;
//                        default:
//                            CBaseController.showAlert(errorTitleString,"Problema tipo d'importazione","Tipo di importazione" + importationType + " non valido");
//                            return;
//                    }
//                    //</editor-fold>
//            }
//        }

        try
        {
            boolean dontExecutePs = false;  //      //TODO gestirlo per poter dare un elenco dettagliato di tutti gli errori senza bloccarsi al primo errore

            for(Row row : listRows)
            {
                StringBuilder sbQuery = new StringBuilder();
                String genericError = "\nInserimento non riuscito per riga excel " + (row.getRowNum() +1) + ": ";
                //validate data
                //basics checks
                switch (importationType)
                {
                    case Corsisti ,Responsabili, Cucina, Cambio-> {
                        String cityName = row.getCell(columnIndices.get(colonnaCitta),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if(cityName.isEmpty())
                        {
                            sbErrors.append(genericError + "Città non specificata");
                            continue;       //TODO gestirlo per poter dare un elenco dettagliato di tutti gli errori senza bloccarsi al primo errore
                        }
                        int id_citta = getIDCittaFromName(cityName);
                        if(id_citta < 0)
                        {
                            sbErrors.append(genericError + "Città '" + cityName + "' non trovata nel database");
                            continue;
                        }
                        row.getCell(columnIndices.get(colonnaCitta)).setCellValue(id_citta);
                        //gestione id_parrocchie
                        String parishNameAndCityName = row.getCell(columnIndices.get(colonnaParrocchia1),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        int id_parrocchia1 =getIDParrocchiaFromName(parishNameAndCityName);
                        parishNameAndCityName = row.getCell(columnIndices.get(colonnaParrocchia2),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        int id_parrocchia2 =getIDParrocchiaFromName(parishNameAndCityName);
                        if(id_parrocchia1 < 0)
                        {
                            sbErrors.append(genericError).append("Parrocchia '").append(parishNameAndCityName).append("' non trovata nel database");
                            continue;
                        }
                        if(id_parrocchia2 < 0)
                        {
                            sbErrors.append(genericError).append("Parrocchia '").append(parishNameAndCityName).append("' non trovata nel database");
                            continue;
                        }
                        row.getCell(columnIndices.get(colonnaParrocchia1)).setCellValue(id_parrocchia1);
                        row.getCell(columnIndices.get(colonnaParrocchia2)).setCellValue(id_parrocchia2);
                        //gestione id_gruppi
                        String groupName = row.getCell((columnIndices.get(colonnaGruppo1)),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        int id_gruppo1 =getIDGruppoFromName(groupName);
                        groupName = row.getCell((columnIndices.get(colonnaGruppo1)),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        int id_gruppo2 =getIDGruppoFromName(groupName);
                        if(id_gruppo1 < 0)
                        {
                            sbErrors.append(genericError).append("Gruppo '").append(groupName).append("' non trovato nel database");
                            continue;
                        }
                        if(id_gruppo2 < 0)
                        {
                            sbErrors.append(genericError).append("Gruppo '").append(groupName).append("' non trovato nel database");
                            continue;
                        }
                        row.getCell(columnIndices.get(colonnaGruppo1)).setCellValue(id_gruppo1);
                        row.getCell(columnIndices.get(colonnaGruppo2)).setCellValue(id_gruppo2);
                    }
                }
                String currErrors = sbErrors.toString();
                String currNotify = sbNotify.toString();
                HashMap<String,PreparedStatement> preparedStatementDict = validateAndGeneratePreparedStatements(row,importationType,eventNumber,sbErrors,sbNotify);
                if(!currErrors.contentEquals(sbErrors))     //sono stati aggiunti errori
                    continue;
                if(!currNotify.contentEquals(sbNotify))     //sono stati aggiunti avvisi
                    continue;
                if(preparedStatementDict == null)
                {
                    sbErrors.append("\nlista di ps nulla,impossibile per riga ").append(row.getRowNum()).append(1);
                    continue;
                }

                if(preparedStatementDict.isEmpty()){
                    sbErrors.append("\nlista di ps vuota per riga ").append(row.getRowNum()).append(1);
                    continue;
                }
                if(preparedStatementDict.size() > 2)
                {
                    sbErrors.append("\nlista di ps maggiore di 2, impossibile per riga ").append(row.getRowNum()).append(1);
                    continue;
                }
                PreparedStatement ps;
                int numOfInsertionPersone = 0;
                int numOfInsertionEventiPerPersone = 0;
                //controls for first ps
                if(preparedStatementDict.containsKey("PersoneCorsisti"))
                {
                    totalNumOfQueriesToDo++;
                    ps = preparedStatementDict.get("PersoneCorsisti");
                    System.out.println(getQueryString(ps));     //debug
                    numOfInsertionPersone += ps.executeUpdate();
                    if(numOfInsertionPersone >0)
                    {
                        totalNumOfInsertionsPersone += numOfInsertionPersone;
                    }
                    else
                    {
                        sbErrors.append("inserimento non riuscito per riga excel").append(row.getRowNum()).append(1);
                    }
                }
                else if(preparedStatementDict.containsKey("Persone"))
                {
                    totalNumOfQueriesToDo++;
                    //TODO 222
                    throw new UnsupportedOperationException("non valido");
                }
//                else      TODO riabilitare poi quando ho la possibilita di aggiornare le persone  responsabili
//                {
//                    CBaseController.showAlert("Errore","Problema per i prepared statements","non trovato nessun prepared statement che aggiorni/inserisce in persone");
//                    return;
//                }
                //controls for second ps
                if(preparedStatementDict.containsKey("EventiPerPersoneCorsisti"))
                {
                    totalNumOfQueriesToDo++;
                    ps = preparedStatementDict.get("EventiPerPersoneCorsisti");
                    ps.setInt(1,getMaxIDFromTable(persone_TABLE,persone_ID));
                    System.out.println(getQueryString(ps));     //debug
                    numOfInsertionEventiPerPersone += ps.executeUpdate();
                    if(numOfInsertionEventiPerPersone >0)
                    {
                        totalNumOfInsertionsEventiPerPersone += numOfInsertionEventiPerPersone;
                    }
                    else
                    {
                        sbErrors.append("inserimento non riuscito per riga excel" + row.getRowNum() +1);
                        return;
                    }
                }
                else if(preparedStatementDict.containsKey("EventiPerPersone"))
                {
                    totalNumOfQueriesToDo++;
                    ps = preparedStatementDict.get("EventiPerPersone");
                    System.out.println(getQueryString(ps));     //debug
                    numOfInsertionEventiPerPersone += ps.executeUpdate();
                    if(numOfInsertionEventiPerPersone >0)
                    {
                        totalNumOfInsertionsEventiPerPersone += numOfInsertionEventiPerPersone;
                    }
                    else
                    {
                        sbErrors.append("inserimento non riuscito per riga excel" + row.getRowNum() +1);
                        return;
                    }
                }
                else
                {
                    CBaseController.showAlert("Errore","Problema per i prepared statements","non trovato nessun prepared statement che inserisca in eventiperPersone");
                    return;
                }
            }
            if(totalNumOfInsertionsPersone >0 || totalNumOfInsertionsEventiPerPersone > 0)
            {
                labelResultTitle.setText("Inserimento avvenuto!");
                Color green = Color.rgb(97, 234,97);
                labelResultTitle.setTextFill(green);
                String label ="";
                switch (importationType)
                {
                    case Corsisti:
                        label= "Tuple inserite: " + totalNumOfInsertionsPersone;
                        break;
                    case Responsabili:
                    case Cucina:
                    case Cambio:
                        label= "Tuple aggiornate: " + totalNumOfInsertionsEventiPerPersone;
                        break;
                    default:
                        errorHolder.setErrorString("Tipo di importazione "+ importationType + " non valido");
                        return;
                }
                labelResultDetails.setText(label + " / " + listRows.size()
                        +(!sbNotify.isEmpty() ? "\nAvvisi: \n" + sbNotify :"")
                        +(!sbErrors.isEmpty() ? "\nErrori: \n" + sbErrors :""));
            }
            else if(sbErrors.toString().isEmpty())
            {
                labelResultTitle.setText("Operazione finita, non ci sono stati nuovi inserimenti");
                Color red = Color.rgb(245, 154,36);
                labelResultTitle.setTextFill(red);
                labelResultDetails.setText(sbErrors.toString());
            }
            else
            {
                labelResultTitle.setText("Inserimento fallito!");
                Color red = Color.rgb(234, 36,36);
                labelResultTitle.setTextFill(red);
                labelResultDetails.setText(sbErrors.toString());
            }
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante l'esecuzione della query SQL",e.getMessage());
        }
    }

    private HashMap<String,PreparedStatement> validateAndGeneratePreparedStatements(Row row, ImportationType importationType, int tlcNumber, StringBuilder sbErrors, StringBuilder sbNotify)
    {
        HashMap<String,PreparedStatement>  preparedStatementDict= new HashMap<String, PreparedStatement>();

        StringBuilder sbQuery = new StringBuilder();
        StringBuilder sbInsertEventiPerPersoneCorsisti = new StringBuilder();
        //to avoid this two controls
        //if(row.getCell(indexNome) == null || (row.getCell(indexNome).getStringCellValue()).isEmpty())
        String name,surname,cellphone,email,parish,address,notes,presenter,group;
        int city_id,parish1_id,parish2_id,group1_id,group2_id;
        name = row.getCell(columnIndices.get(colonnaNome),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        surname = row.getCell(columnIndices.get(colonnaCognome),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        // <editor-fold desc="generic controls">
        if(name.isEmpty())
        {
            sbErrors.append("Per la riga "+ row.getRowNum()+1 + " Nome non presente");
            return null;
        }
        if(surname.isEmpty())
        {
            sbErrors.append("Per la riga "+ row.getRowNum()+1 + " Cognome non presente");
            return null;
        }
        //</editor-fold>
        // <editor-fold desc="values to insert into the ps">
        city_id = (int) row.getCell(columnIndices.get(colonnaCitta), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();

        cellphone = getCellphone(row);
        email =  row.getCell(columnIndices.get(colonnaEmail),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        parish1_id = (int)(row.getCell(columnIndices.get(colonnaParrocchia1), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());
        parish2_id = (int)(row.getCell(columnIndices.get(colonnaParrocchia2), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());
        address =  row.getCell(columnIndices.get(colonnaIndirizzo),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        boolean isMale = row.getCell(columnIndices.get(colonnaUomo),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("M");
        boolean isGuitarPlayer = row.getCell(columnIndices.get(colonnaSuonatore),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isMarried = row.getCell(columnIndices.get(colonnaSposato),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isChef = row.getCell(columnIndices.get(colonnaCuoco),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isConsecrated = row.getCell(columnIndices.get(colonnaConsacrato),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isPriest = row.getCell(columnIndices.get(colonnaSacerdote),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isFriarOrNun = row.getCell(columnIndices.get(colonnaFrateOSuora),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        boolean isProfessor = row.getCell(columnIndices.get(colonnaProfessore),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase("SI");
        notes = row.getCell(columnIndices.get(colonnaAnnotazioni),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        presenter = row.getCell(columnIndices.get(colonnaPresentatore),Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
        group1_id = (int)(row.getCell(columnIndices.get(colonnaGruppo1), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());
        group2_id = (int)(row.getCell(columnIndices.get(colonnaGruppo2), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());

        int newPersonID =-1,idCurrPerson= -1,idSpecificRole1= -1,idSpecificRole2 = -1;

        Timestamp dataSql = null;

        int idEvento = getIDEvento(tlcNumber,EventType.TLC);
        if(idEvento == -1) {
            sbErrors.append("Evento non trovato per il TLC numero "+tlcNumber);
            return null;
        }
        int idRuolo = getIDRuoloFromImportationType(importationType);
        if(idRuolo == -1) {
            sbErrors.append("Ruolo non trovato per l'importazione "+importationType);
            return null;
        }

        //</editor-fold>
        List<List<String>> listSamePersonData = null;
        switch (importationType)
        {
            case Corsisti ->
            {
                listSamePersonData = getListOfSamePersons(row,true);
                AtomicBoolean personNotToInsert = new AtomicBoolean(false);
                if(!listSamePersonData.isEmpty())
                {
                    createConfirmationPopup(listSamePersonData, personNotToInsert,sbNotify);
                }
                if(personNotToInsert.get())
                {
                    preparedStatementDict.clear();
                    return preparedStatementDict;
                }

                dataSql= CExcelHandler.getSQLTimestampFromCell(row.getCell(columnIndices.get(colonnaDataDiNascita)),"dd/MM/yyyy"); //TODO rivedere il formato,se corretto per MYSQL
                sbQuery.append("INSERT INTO " + persone_TABLE +
                        "(" +
                            persone_nome                                        +                                   //0
                        ","+persone_cognome                                     +                                   //1
                        ","+persone_dataDiNascita                               +                                   //2
                        ","+persone_id_citta                                    +                                   //3
                        ","+persone_indirizzo                                   +                                   //4
                        ","+persone_id_parrocchia1                              +                                   //5
                        ","+persone_id_parrocchia2                              +                                   //6
                        ","+persone_id_gruppo1                                  +                                   //7
                        ","+persone_id_gruppo2                                  +                                   //8
                        ","+persone_uomo                                        +                                   //9
                        ","+persone_cellulare                                   +                                   //10
                        ","+persone_email                                       +                                   //11
                        ","+persone_suonatore                                   +                                   //12
                        ","+persone_cuoco                                       +                                   //13
                        ","+persone_professore                                  +                                   //14
                        ","+persone_sacerdote                                   +                                   //15
                        ","+persone_consacrato                                  +                                   //16
                        ","+persone_frateOSuora                                 +                                   //17
                        ","+persone_sposato                                     +                                   //18
                        ","+persone_annotazioni                                 +                                   //19
                        ","+persone_presentatore                                +                                   //20
                        ") "+
                        "VALUES " +
                        "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?);");
                sbInsertEventiPerPersoneCorsisti.append("INSERT INTO " + eventiperpersone_TABLE + " (" +
                        eventiperpersone_id_persona +","+
                        eventiperpersone_id_evento  +","+
                        eventiperpersone_id_ruolo   +","+
                        eventiperpersone_id_ruoloSpecifico1   +","+
                        eventiperpersone_id_ruoloSpecifico2 +","+
                        eventiperpersone_id_citta + ")" + " " +
                        "VALUES (?,?,?,?,?  ,?)");
            }
            case Responsabili, Cucina ,Cambio ->
            {
                listSamePersonData = getListOfSamePersons(row,true);
                if(listSamePersonData.size() > 1)
                {
                    AtomicInteger personToUpdateIndex = new AtomicInteger();
                    createConfirmationPopupEquipe(listSamePersonData,personToUpdateIndex,sbNotify);
                    if(personToUpdateIndex.equals(-1))
                    {
                        sbNotify.append("\nPer la riga " + (row.getRowNum()+1) + " Persona con " +
                                "nome '" +row.getCell(columnIndices.get(colonnaNome)).getStringCellValue() +"'"
                                +"e cognome '" +row.getCell(columnIndices.get(colonnaCognome)).getStringCellValue()+"'"
                                + "non aggiornata poichè sono state trovate " + listSamePersonData.size() + " persone!"
                        );
                    }
                    else
                    {
                        idCurrPerson = Integer.parseInt(listSamePersonData.get(personToUpdateIndex.get()).get(0));
                        name = listSamePersonData.get(personToUpdateIndex.get()).get(1);
                        surname = listSamePersonData.get(personToUpdateIndex.get()).get(2);
                        city_id = Integer.parseInt(listSamePersonData.get(personToUpdateIndex.get()).get(3));
                    }
                }
                else if(listSamePersonData.isEmpty())
                {
                    sbErrors.append("\nPer la riga " + (row.getRowNum()+1) + " Persona con " +
                            "nome '" +row.getCell(columnIndices.get(colonnaNome)).getStringCellValue() +"'"
                            +"e cognome '" +row.getCell(columnIndices.get(colonnaCognome)).getStringCellValue()+"'"
                            + " non trovata!"
                    );
                    return null;
                }
                idCurrPerson = Integer.parseInt(listSamePersonData.get(0).get(0));          //for the person to update
                List<Integer> elencoIDRuoliSpecifici =  getIDRuoliSpecificiByColumns(row,sbErrors);
                if(elencoIDRuoliSpecifici.size() == 1)
                {
                    idSpecificRole1 = elencoIDRuoliSpecifici.get(0);
                }
                else if(elencoIDRuoliSpecifici.size() == 2)
                {
                    idSpecificRole1 = elencoIDRuoliSpecifici.get(0);
                    idSpecificRole2 = elencoIDRuoliSpecifici.get(1);

                }
                //TODO successivamente andrebbe fatta anche una update delle persone che stanno in equipe, per esempio potrebbero cambiare casa/gruppo/parrocchia... se si fa seguire 222
                sbQuery.append("" +
                        "INSERT INTO " + eventiperpersone_TABLE + " (" +
                                        eventiperpersone_id_persona             +","+
                                        eventiperpersone_id_evento              +","+
                                        eventiperpersone_id_ruolo               +","+
                                        eventiperpersone_id_ruoloSpecifico1     +","+
                                        eventiperpersone_id_ruoloSpecifico2     +","+
                                        eventiperpersone_id_citta               + ")"
                        + " " +
                            "VALUES (?,?,?,?,?  ,?) ");

            }
            default -> throw new IllegalStateException("Unexpected value: " + importationType);
        }
        PreparedStatement psPersone = null;
        PreparedStatement psEventiPerPersone = null;
        try {
            System.out.println(sbQuery);
            int parameterIndexPersone = 1;
            int parameterIndexEventiPerPersone = 1;
            Connection conn = connector.getConnection();
            psPersone = conn.prepareStatement(sbQuery.toString());
            psEventiPerPersone = conn.prepareStatement(sbQuery.toString());

            switch (importationType) {

                case Corsisti -> {
                    if(psPersone == null)
                    {
                        throw new SQLException("PreparedStatement per la query di inserimento non è stato inizializzato");
                    }
                    psPersone.setString(parameterIndexPersone++, name);                                         //0
                    psPersone.setString(parameterIndexPersone++, surname);                                      //1
                    psPersone.setTimestamp(parameterIndexPersone++, dataSql);                                   //2
                    psPersone.setInt(parameterIndexPersone++, city_id);                                         //3
                    psPersone.setString(parameterIndexPersone++, address);                                      //4
                    if (parish1_id <= 0) {                                                                      //5
                        psPersone.setNull(parameterIndexPersone++, Types.INTEGER);
                    } else {
                        psPersone.setInt(parameterIndexPersone++, parish1_id);
                    }
                    if (parish2_id <= 0) {                                                                      //6
                        psPersone.setNull(parameterIndexPersone++, Types.INTEGER);
                    } else {
                        psPersone.setInt(parameterIndexPersone++, parish2_id);
                    }
                    if (group1_id <= 0) {                                                                       //7
                        psPersone.setNull(parameterIndexPersone++, Types.INTEGER);
                    } else {
                        psPersone.setInt(parameterIndexPersone++, group1_id);
                    }
                    if (group2_id <= 0) {                                                                       //8
                        psPersone.setNull(parameterIndexPersone++, Types.INTEGER);
                    } else {
                        psPersone.setInt(parameterIndexPersone++, group2_id);
                    }
                    psPersone.setBoolean(parameterIndexPersone++, isMale);              //9
                    psPersone.setString(parameterIndexPersone++, cellphone);            //10
                    psPersone.setString(parameterIndexPersone++, email);                //11
                    psPersone.setBoolean(parameterIndexPersone++, isGuitarPlayer);      //12
                    psPersone.setBoolean(parameterIndexPersone++, isChef);              //13
                    psPersone.setBoolean(parameterIndexPersone++, isProfessor);         //14
                    psPersone.setBoolean(parameterIndexPersone++, isPriest);            //15
                    psPersone.setBoolean(parameterIndexPersone++, isConsecrated);       //16
                    psPersone.setBoolean(parameterIndexPersone++, isFriarOrNun);        //17
                    psPersone.setBoolean(parameterIndexPersone++, isMarried);           //18
                    psPersone.setString(parameterIndexPersone++, notes);                //19
                    psPersone.setString(parameterIndexPersone++, presenter);            //20

                    if(!sbInsertEventiPerPersoneCorsisti.toString().isEmpty())
                        psEventiPerPersone = conn.prepareStatement(sbInsertEventiPerPersoneCorsisti.toString());
                    else
                    {
                        CBaseController.showAlert("Errore","Problema durante l'inserimento di una nuova tupla in eventiperpersone", "query vuota!");
                        return null;
                    }
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,12345678);              //0
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,idEvento);                 //1
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,idRuolo);                  //2
                    psEventiPerPersone.setNull(parameterIndexEventiPerPersone++,Types.INTEGER);           //3
                    psEventiPerPersone.setNull(parameterIndexEventiPerPersone++,Types.INTEGER);           //4
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,city_id);                  //5

                    preparedStatementDict.put("PersoneCorsisti",psPersone);
                    preparedStatementDict.put("EventiPerPersoneCorsisti",psEventiPerPersone);
                }
                case Responsabili, Cucina, Cambio -> {

                    if(psEventiPerPersone == null)
                    {
                        throw new SQLException("PreparedStatement per la query di inserimento non è stato inizializzato");
                    }
                    //TODO scommentare e integrare 222
//                    ps.setString(parameterIndex1++, tlcNumber +",");
//                    if (!city.isEmpty())
//                        ps.setString(parameterIndex1++, city);
//                    if (!address.isEmpty())
//                        ps.setString(parameterIndex1++, address);
//                    if(!parish.isEmpty())
//                        ps.setString(parameterIndex1++, parish);
//                    if (!cellphone.isEmpty())
//                        ps.setString(parameterIndex1++, cellphone);
//                    if (!row.getCell(indexSuonatore, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().isEmpty())
//                        ps.setBoolean(parameterIndex1++, isGuitarPlayer);
//                    //where parameters
//                    ps.setString(parameterIndex1++, name);
//                    ps.setString(parameterIndex1++, surname);
//                    if(listSamePersonData.size() > 1)   //if there is more than one person with the same name and surname, it means we need the city also to leave the person that doesn't need to get updated
//                        ps.setString(parameterIndex1++, city);

                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,idCurrPerson);                                           //0
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,idEvento);                                               //1
                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,idRuolo);                                                //2
                    if (idSpecificRole1 <= 0)                                                                                           //3
                        psEventiPerPersone.setNull(parameterIndexEventiPerPersone++, Types.INTEGER);
                    else
                        psEventiPerPersone.setInt(parameterIndexEventiPerPersone++, idSpecificRole1);

                    if (idSpecificRole2 <= 0)                                                                                           //4
                        psEventiPerPersone.setNull(parameterIndexEventiPerPersone++, Types.INTEGER);
                    else
                        psEventiPerPersone.setInt(parameterIndexEventiPerPersone++, idSpecificRole2);

                    psEventiPerPersone.setInt(parameterIndexEventiPerPersone++,city_id);                                                //5

                    preparedStatementDict.put("EventiPerPersone",psEventiPerPersone);
                }
            }
        }
        catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la preparazione dello statement SQL",e.getMessage());
        }
        return preparedStatementDict;
    }


    private void setColumnIndex(String columnName, int columnIndex, ImportationType importationType,String errorTitle, String errorHeader) {
        String errorDescription = "Titolo colonna "+ columnName + " non valido per " + importationType;
        columnName = columnName.toLowerCase().replace(" ", "");
        switch(columnName)
        {
            case colonnaNome:
            case colonnaCognome:
            case colonnaDataDiNascita:
            case colonnaCitta:
            case colonnaIndirizzo:
            case colonnaParrocchia1:
            case colonnaParrocchia2:
            case colonnaGruppo1:
            case colonnaGruppo2:
            case colonnaCellulare:
            case colonnaEmail:
            case colonnaAnnotazioni:
                columnIndices.put(columnName, columnIndex);
                break;
            default:
                switch (importationType)
                {
                    case Corsisti:
                        setColumnIndexForCorsisti(columnName, columnIndex,errorTitle, errorHeader, errorDescription);
                        break;
                    case Responsabili:
                        setColumnIndexForResponsabili(columnName, columnIndex,errorTitle, errorHeader, errorDescription);
                        break;
                    case Cucina:
                        setColumnIndexForCucina(columnName, columnIndex,errorTitle, errorHeader, errorDescription);
                        break;
                    case Cambio:
                        setColumnIndexForCambio(columnName, columnIndex,errorTitle, errorHeader, errorDescription);
                        break;
                    default:
                        CBaseController.showAlert(errorTitle,"Problema tipo d'importazione","Tipo di importazione" + importationType + " non valido");
                        break;
                }
                break;
        }

    }


    private void setColumnIndexForCorsisti(String columnName, int columnIndex,String errorTitle, String errorHeader, String errorDescription) {
        switch (columnName) {
            case colonnaUomo:
            case colonnaPresentatore:
            case colonnaSposato:
            case colonnaFrateOSuora:
            case colonnaCuoco:
            case colonnaConsacrato:
            case colonnaSacerdote:
            case colonnaProfessore:
            case colonnaSuonatore:

                columnIndices.put(columnName, columnIndex);
                break;
            default:
                CBaseController.showAlert(errorTitle, errorHeader, errorDescription);
                break;
        }
    }


    private void setColumnIndexForResponsabili(String columnName, int columnIndex,String errorTitle, String errorHeader, String errorDescription) {
        switch (columnName) {
            case colonnaCapo:
            case colonnaSacerdote:
            case colonnaSpallaSacerdote:
            case colonnaConsacrato:
            case colonnaVice:
            case colonnaTestimonianzaDiCoppia:
            case colonnaTestimonianzaCarita:
            case colonnaSuonatore:
                columnIndices.put(columnName, columnIndex);
                break;
            default:
                CBaseController.showAlert(errorTitle, errorHeader, errorDescription);
                break;
        }
    }

    private void setColumnIndexForCucina(String columnName, int columnIndex,String errorTitle, String errorHeader, String errorDescription) {
        switch (columnName) {
            case colonnaSacerdote:
            case colonnaCuoco:
            case colonnaSuonatore:
                columnIndices.put(columnName, columnIndex);
                break;
            default:
                CBaseController.showAlert(errorTitle, errorHeader, errorDescription);
                break;
        }
    }

    private void setColumnIndexForCambio(String columnName, int columnIndex,String errorTitle, String errorHeader, String errorDescription) {
        switch (columnName) {
            case colonnaGestoreCappellina:
                columnIndices.put(columnName, columnIndex);
                break;
            default:
                CBaseController.showAlert(errorTitle, errorHeader, errorDescription);
                break;
        }
    }

    private void createConfirmationPopup(List<List<String>> listSamePersonData, AtomicBoolean personNotToInsert, StringBuilder sbNotify) {
        if(listSamePersonData.size() > 1)
        {
            //TODO choose which tuple update with GUI
            throw new UnsupportedOperationException ("Not implemented");
        }
        VBox popupRoot = new VBox(10);
        popupRoot.setPadding(new Insets(10));
        Label popupLabel = new Label("C'è una persona con lo stesso " +
                "nome(" + listSamePersonData.getFirst().get(1) + ")" +
                ",cognome( "+ listSamePersonData.getFirst().get(2) + ")" +
                "e citta'(" + listSamePersonData.getFirst().get(3)+ ")" +
                "...vuoi inserirla lo stesso?");
        Button yesButton = new Button("Si");
        Button noButton = new Button("No");
        popupRoot.getChildren().addAll(popupLabel, yesButton, noButton);
        Scene popupScene = new Scene(popupRoot, 900, 150);

        // Show the popup window
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Make the popup modal
        popupStage.setScene(popupScene);
        popupStage.setTitle("Confirmation");

        yesButton.setOnAction(e -> {
            System.out.println("Yes button clicked");
            popupStage.close();

        });
        noButton.setOnAction(e -> {
            System.out.println("No button clicked");
            sbNotify.append("\nPersona con " +
                    "nome(" + listSamePersonData.getFirst().get(1) + ")" +
                    ",cognome( "+ listSamePersonData.getFirst().get(2) + ")" +
                    "e citta'(" + listSamePersonData.getFirst().get(3)+ ")" + "non inserita poichè già presente");
            personNotToInsert.set(true);
            popupStage.close();
        });

        popupStage.showAndWait(); // Wait for the popup to close
    }

    private void createConfirmationPopupEquipe(List<List<String>> listSamePersonData, AtomicInteger personUpdateIndex, StringBuilder sbNotify) {
        if(listSamePersonData.size() <= 1)
        {
            throw new UnsupportedOperationException ("Non dovrebbe succedere!!");
        }
        personUpdateIndex.set(-1);
        String queryUpdate ="TODO";
        Stage popupStage = new Stage();
        VBox popupRoot = new VBox(10);
        popupRoot.setPadding(new Insets(10));
        Label popupLabel = new Label("Ci sono" + listSamePersonData.size() + "persone con lo stesso " +
                "nome(" + listSamePersonData.getFirst().get(0) + ")" +
                ",cognome( "+ listSamePersonData.getFirst().get(1) + ")" +
                "e citta'(" + listSamePersonData.getFirst().get(2)+ ")" +
                "...Scegli quella da aggiornare, oppure clicca 'non aggiornare'");
        popupRoot.getChildren().add(popupLabel);
        for(int i = 0;i< listSamePersonData.size();i++) {
            List<String> person = listSamePersonData.get(i);
            Label currLabel = new Label(person.get(0) + "," +
                    person.get(1) + "," +
                    person.get(2) + ",");
            Button thisPersonButton = new Button("Questa");
            popupRoot.getChildren().add(currLabel);
            popupRoot.getChildren().add(thisPersonButton);

            int finalI = i;
            thisPersonButton.setOnAction(e -> {
                System.out.println("Person selected");
                sbNotify.append("\nPersona con " +
                        "nome(" + listSamePersonData.getFirst().get(0) + ")" +
                        ",cognome( " + listSamePersonData.getFirst().get(1) + ")" +
                        "e citta'(" + listSamePersonData.getFirst().get(2) + ")" + "aggiornata");
                personUpdateIndex.set(finalI);
                popupStage.close();
            });
        }
        Button noUpdateButton = new Button("Non aggiornare");
        popupRoot.getChildren().add(noUpdateButton);
        Scene popupScene = new Scene(popupRoot, 900, 200);

        // Show the popup window
        popupStage.initModality(Modality.APPLICATION_MODAL); // Make the popup modal
        popupStage.setScene(popupScene);
        popupStage.setTitle("Choice");

        noUpdateButton.setOnAction(e -> {
            System.out.println("No update button clicked");
            popupStage.close();
        });

        popupStage.showAndWait(); // Wait for the popup to close
    }


    private int getIDCittaFromName(String cityName){
        int idCitta = -1;
        String query = "SELECT "
                + citta_ID + " "
                +"FROM " + citta_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" +citta_nome + "))" + " = TRIM(LOWER(?)) ";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, cityName);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idCitta = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_citta",e.getMessage());
        }
        return idCitta;
    }
    private int getIDParrocchiaFromName(String parishNameAndCity){
        if(parishNameAndCity.isEmpty())
            return 0;
        int idParrocchia = -1;
        //string cleaning and splitting
        String[] stringParts = parishNameAndCity.split(" / ");
        String parishName = stringParts[0];
        String parishCityName = stringParts[1];
        int cityId = getIDCittaFromName(parishCityName);
        String query = "SELECT "
                + parrocchie_ID + " "
                +"FROM " + parrocchie_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" +parrocchie_nome + "))" + " = TRIM(LOWER(?)) " +
                "AND " + parrocchie_id_citta + " = ?";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, parishName);
            ps.setInt(2, cityId);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idParrocchia = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_parrocchia",e.getMessage());
        }
        return idParrocchia;
    }
    private int getIDGruppoFromName(String groupName){
        if(groupName.isEmpty())
            return 0;
        int idGruppo = -1;
        String query = "SELECT "
                + gruppi_ID + " "
                +"FROM " + gruppi_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" + gruppi_nome + "))" + " = TRIM(LOWER(?)) ";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, groupName);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idGruppo = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_gruppo",e.getMessage());
        }
        return idGruppo;
    }



    private int getIDEvento(int eventEditionNumber, EventType eventType){
        int idEvento = -1;
        int id_tipologiaEvento = -1;
        switch (eventType) {
            case TLC -> {
                id_tipologiaEvento = 1;
            }
            case Zikaron -> {
                id_tipologiaEvento = 3;

            }
            case Rinnovamento -> {
                id_tipologiaEvento = 2;
            }
            default -> throw new UnsupportedOperationException("valore non considerato");
        }
        String query = "SELECT "
                + eventi_ID + " "
                +"FROM " + eventi_TABLE + " "
                + "JOIN " + tipologieEventi_TABLE + " ON " + tipologieEventi_ID + "=" + eventi_id_tipologiaEvento + " "
                +"WHERE " + eventi_numeroEdizione  + " = ? AND " + eventi_id_tipologiaEvento + "= ?";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, eventEditionNumber);
            ps.setInt(2, id_tipologiaEvento);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idEvento = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_evento",e.getMessage());
        }
        return idEvento;
    }
    private int getIDTipologiaEventoFromName(String eventTypologyName){
        int idTipologiaEvento = -1;
        String query = "SELECT "
                + tipologieEventi_ID + " "
                +"FROM " + tipologieEventi_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" + tipologieEventi_nome + "))" + " = TRIM(LOWER(?)) ";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, eventTypologyName);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idTipologiaEvento = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_tipologiaEvento",e.getMessage());
        }
        return idTipologiaEvento;
    }

    private int getIDRuoloFromImportationType(ImportationType importationType){
        int idRuolo = -1;
        String query = "SELECT "
                + ruoli_ID + " "
                +"FROM " + ruoli_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" + ruoli_nome + "))" + " = TRIM(LOWER(?)) ";
        try
        {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            String roleName = "";
            switch (importationType) {
                case Corsisti -> {
                    roleName = "corsista";
                }
                case Responsabili -> {
                    roleName = "equipe responsabili";
                }
                case Cucina -> {
                    roleName = "equipe cucina";
                }
                case Cambio -> {
                    roleName = "equipe cambio";
                }
            }
            ps.setString(1, roleName);
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idRuolo = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_ruolo",e.getMessage());
        }
        return idRuolo;
    }
    private int getIDRuoloSpecificoFromName(String specificRoleName){
        int idRuoloSpecifico = -1;
        String query = "SELECT "
                + ruoliSpecifici_ID + " "
                +"FROM " + ruoliSpecifici_TABLE + " "
                +"WHERE " + "TRIM(LOWER(" + ruoliSpecifici_nome + "))" + " = TRIM(LOWER(?)) ";
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, specificRoleName.toString());
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                idRuoloSpecifico = rs.getInt(1);
        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL per id_ruoloSpecifico con nome " + specificRoleName,e.getMessage());
        }
        return idRuoloSpecifico;
    }

    private List<Integer> getIDRuoliSpecificiByColumns(Row row,StringBuilder sbErrors) {
        List<Integer> coppiadiID = new ArrayList<>();
        int countTrue = 0;

        for (RuoloSpecifico ruolo : RuoloSpecifico.values()) {
            boolean isTrue = row.getCell(ruolo.getIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    .getStringCellValue().equalsIgnoreCase("SI");

            if (isTrue)
            {
                countTrue++;
                if (countTrue > 2)
                    sbErrors.append("Trovati più di 2 ruoli specifici impostati a 'SI'.");

                int idRuolo = getIDRuoloSpecificoFromName(ruolo.getNome());
                if (idRuolo != -1) {
                    coppiadiID.add(idRuolo);
                }
            }
        }
        return coppiadiID;
    }
    private List<List<String>> getListOfSamePersons(Row row, boolean cityRelevant) {
        String query=
                "SELECT " +  persone_ID                 +                   //1
                        "," +persone_nome               +                   //2
                        "," +persone_cognome            +                   //3
                        (cityRelevant?  ","+persone_id_citta  :"")      +   //4
                        " FROM " + persone_TABLE +" "   +
                        " WHERE (" +
                        persone_nome + " = ? AND " + persone_cognome  +"= ?)";
        List<List<String>> samePersonList = new ArrayList<>();
        try
        {
            Connection conn = connector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,row.getCell(columnIndices.get(colonnaNome)).getStringCellValue());
            ps.setString(2,row.getCell(columnIndices.get(colonnaCognome)).getStringCellValue());
            ResultSet rs= ps.executeQuery();
            while (rs.next())
            {
                List<String> currPersonInfo = new ArrayList<>();
                currPersonInfo.add(String.valueOf(rs.getInt(1)));
                currPersonInfo.add(rs.getString(2));
                currPersonInfo.add(rs.getString(3));
                if(cityRelevant)
                    currPersonInfo.add(String.valueOf(rs.getInt(4)));
                samePersonList.add(currPersonInfo);
            }
            //TODO

        }catch (SQLException e)
        {
            CBaseController.showAlert("Errore","Problema durante la ricerca SQL",e.getMessage());
        }
        return samePersonList;
    }

    private String getCellphone(Row row) {
        String cellphone;
        // Leggi il valore numerico dalla cella
        double numericValue = row.getCell(columnIndices.get(colonnaCellulare), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();

        // Converti il valore numerico in BigDecimal per mantenere la precisione
        BigDecimal bigDecimalValue = new BigDecimal(numericValue);

        // Converti il BigDecimal in una stringa senza notazione esponenziale
        cellphone = bigDecimalValue.toPlainString();

        // Rimuovi eventuali punti decimali
        cellphone = cellphone.replace(".", "");
        return cellphone;
    }


    public void importCorsistiButtonOnAction(ActionEvent event) {
        importFileButtonOnAction(event,ImportationType.Corsisti);
    }

    public void importResponsabiliButtonOnAction(ActionEvent event) {
        importFileButtonOnAction(event,ImportationType.Responsabili);
    }

    public void importCucinaButtonOnAction(ActionEvent event) {
        importFileButtonOnAction(event,ImportationType.Cucina);
    }

    public void importCambioButtonOnAction(ActionEvent event) {
        importFileButtonOnAction(event,ImportationType.Cambio);
    }
}