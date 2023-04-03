package com.fstt.projectjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private Button add_liv;

    @FXML
    private TextField addadress_liv;

    @FXML
    private AnchorPane addlivreur_form;

    @FXML
    private TextField addnom_liv;

    @FXML
    private TextField addphone_liv;

    @FXML
    private TextField addprenom_liv;

    @FXML
    private TextField adress_client;

    @FXML
    private TableColumn<livData, String> adress_col;

    @FXML
    private TableColumn<livData, String> adress_liv_col;

    @FXML
    private Button clear_cmd;

    @FXML
    private Button clear_liv;

    @FXML
    private Button client;

    @FXML
    private Label cmd_done;

    @FXML
    private AnchorPane cmd_form;

    @FXML
    private TableColumn<livData, Integer> cmd_id_col;

    @FXML
    private TextField cmd_ref;

    @FXML
    private Button delete_liv;

    @FXML
    private Button home;

    @FXML
    private LineChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label hometotal_liv;

    @FXML
    private Button livreur;

    @FXML
    private Button logout;

    @FXML
    private TextField nom_client;

    @FXML
    private TableColumn<livData, String> nom_col;

    @FXML
    private TableColumn<livData, String> nom_liv_col;

    @FXML
    private TableColumn<livData, String> phone_liv_col;

    @FXML
    private TextField prenom_client;

    @FXML
    private TableColumn<livData, String> prenom_col;

    @FXML
    private TableColumn<livData, String> prenom_liv_col;

    @FXML
    private TextField search_liv;

    @FXML
    private TableView<livData> table_cmd;

    @FXML
    private TableView<livData> tableview_liv;
    @FXML
    private AnchorPane main_form;
    @FXML
    private Label total_cmd;

    @FXML
    private Button update_cmd;

    @FXML
    private Button update_liv;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    public void homeTotalLiv() {

        String sql = "SELECT COUNT(id) FROM livreur";

        connect = database.connectDb();
        int countData = 0;
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }

            hometotal_liv.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void totalCmd() {

        String sql = "SELECT COUNT(id) FROM liv_info";

        connect = database.connectDb();
        int countData = 0;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            total_cmd.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //**********************************************************************
    public void addLivreurAdd(){
        String sql = "INSERT INTO livreur " + "(nom , prénom, tel, adress)" + "VALUES(? , ? , ? , ?)";
        connect = database.connectDb();
        try{
            Alert alert;
            if (addnom_liv.getText().isEmpty() || addprenom_liv.getText().isEmpty() || addphone_liv.getText().isEmpty() ||addadress_liv.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            }
            else{
                prepare= connect.prepareStatement(sql);
                prepare.setString(1,addnom_liv.getText());
                prepare.setString(2,addprenom_liv.getText());
                prepare.setString(3,addphone_liv.getText());
                prepare.setString(4,addadress_liv.getText());
                prepare.executeUpdate();

                String insertInfo = "INSERT INTO liv_info "
                        + "(nom,prénom,adress,commande_ref) "
                        + "VALUES(?,?,?,?)";
                prepare= connect.prepareStatement(sql);
                prepare = connect.prepareStatement(insertInfo);

                prepare.setString(1,addnom_liv.getText());
                prepare.setString(2,addprenom_liv.getText());
                prepare.setString(3,addadress_liv.getText());
                prepare.setInt(4, Integer.parseInt("000"));

                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();
                addLivShowListData();
                addLivreurReset();
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //**********************************************************************
    public void addLivreurUpdate(){
        String sql = "UPDATE livreur SET nom ='"+addnom_liv.getText()+"'";
        connect = database.connectDb();
        try{
            Alert alert;
            if (addnom_liv.getText().isEmpty() || addprenom_liv.getText().isEmpty() || addphone_liv.getText().isEmpty() ||addadress_liv.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            }
            else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE This Livreur: " +addnom_liv .getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    int commande_ref ;

                    String checkData = "SELECT * FROM liv_info WHERE nom = '"
                            + addnom_liv.getText() + "'";

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    while (result.next()) {
                        commande_ref = result.getInt("commande_ref");
                    }


                    String updateInfo = "UPDATE liv_info SET nom = '"
                            + addnom_liv.getText() + "', prénom = '"
                            + addprenom_liv.getText() + "', adress = '"
                            + addadress_liv.getText()
                            + "' WHERE nom = '"
                            + addnom_liv.getText() + "'";

                    prepare = connect.prepareStatement(updateInfo);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();
                }

                addLivShowListData();
                addLivreurReset();


            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //**********************************************************************
    public void addEmployeeSearch() {

        FilteredList<livData> filter = new FilteredList<>(addLivList, e -> true);

        search_liv.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateLivData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateLivData.getNom().toString().contains(searchKey)) {
                    return true;
                } else if (predicateLivData.getPrénom().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateLivData.getTel().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateLivData.getAdress().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<livData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(tableview_liv.comparatorProperty());
        tableview_liv.setItems(sortList);
    }
    //**********************************************************************
    public void addLivreurDelete(){
        String sql="DELETE FROM livreur WHERE nom = '"+addnom_liv.getText()+"'";
        connect = database.connectDb();
        try {
            Alert alert;
            if (addnom_liv.getText().isEmpty() || addprenom_liv.getText().isEmpty() || addphone_liv.getText().isEmpty() ||addadress_liv.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            }
            else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE This Livreur: " +addnom_liv .getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    String deleteInfo = "DELETE FROM liv_info WHERE nom = '"
                            + addnom_liv.getText() + "'";

                    prepare = connect.prepareStatement(deleteInfo);
                    prepare.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                }
                addLivShowListData();
                addLivreurReset();
                addEmployeeSearch();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //**********************************************************************
    public void addLivreurReset(){
        addnom_liv.setText("");
        addprenom_liv.setText("");
        addphone_liv.setText("");
        addadress_liv.setText("");

    }
    //**********************************************************************
    public void cmdUpdate() {

        String sql = "UPDATE liv_info SET commande_ref = '" + cmd_ref.getText()
                + "' WHERE nom = '" + nom_client.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (cmd_ref.getText().isEmpty()
                    || nom_client.getText().isEmpty()
                    || prenom_client.getText().isEmpty()
                    || adress_client.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select item first");
                alert.showAndWait();
            } else {
                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                commandeShowListData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cmdReset() {
        nom_client.setText("");
        prenom_client.setText("");
        adress_client.setText("");
        cmd_ref.setText("");

    }
    //**********************************************************************
    public void cmdSelect() {

        livData livD = table_cmd.getSelectionModel().getSelectedItem();
        int num = table_cmd.getSelectionModel().getSelectedIndex();


        nom_client.setText(String.valueOf(livD.getNom()));
        prenom_client.setText(livD.getPrénom());
        adress_client.setText(livD.getAdress());
        cmd_ref.setText(String.valueOf(livD.getCommande_ref()));

    }
    //**********************************************************************
    public ObservableList<livData> addLivreurData(){
        ObservableList<livData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM livreur";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            livData livD;
            while (result.next()){
                livD= new livData(result.getString("nom"), result.getString("prénom"), result.getString("tel"),result.getString("adress"));
                listData.add(livD);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    return listData;
    }
    private ObservableList<livData>addLivList;
    //**********************************************************************
    public void addLivShowListData(){
        addLivList = addLivreurData();
        nom_liv_col.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_liv_col.setCellValueFactory(new PropertyValueFactory<>("prénom"));
        phone_liv_col.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adress_liv_col.setCellValueFactory(new PropertyValueFactory<>("adress"));
        tableview_liv.setItems(addLivList);
    }
    //**********************************************************************



   public void addLivreurSelect(){
        livData livD = tableview_liv.getSelectionModel().getSelectedItem();
        int num = tableview_liv.getSelectionModel().getSelectedIndex();

       addnom_liv.setText(String.valueOf(livD.getNom()));
       addprenom_liv.setText(livD.getPrénom());
       addphone_liv.setText(livD.getTel());
       addadress_liv.setText(livD.getAdress());


   }
    //**********************************************************************
    private ObservableList<livData> commandeListData(){
        ObservableList<livData> listData = FXCollections.observableArrayList();
        String sql="SELECT * FROM livreur";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            livData livD;

            while (result.next()) {
                livD = new livData(result.getString("nom"),
                        result.getString("prénom"),
                        result.getString("adress"),
                        result.getInt("commande_ref"));

                listData.add(livD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;

    }
    //**********************************************************************
    private ObservableList<livData> commandeList;

    public void commandeShowListData() {
        commandeList = commandeListData();

        cmd_id_col.setCellValueFactory(new PropertyValueFactory<>("commande_ref"));
        nom_col.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_col.setCellValueFactory(new PropertyValueFactory<>("prénom"));
        adress_col.setCellValueFactory(new PropertyValueFactory<>("adress"));



        table_cmd.setItems(commandeList);

    }

    //**********************************************************************

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home) {
            home_form.setVisible(true);
            addlivreur_form.setVisible(false);
            cmd_form.setVisible(false);

            homeTotalLiv();
            totalCmd();





        } else if (event.getSource() == livreur) {
            home_form.setVisible(false);
            addlivreur_form.setVisible(true);
            cmd_form.setVisible(false);





        } else if (event.getSource() == client) {
            home_form.setVisible(false);
            addlivreur_form.setVisible(false);
            cmd_form.setVisible(true);
            commandeShowListData();




        }

    }
    //**********************************************************************
 public void logout(){
     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
     alert.setTitle("Confirmation Message");
     alert.setHeaderText(null);
     alert.setContentText("Are you sure you want to logout?");
     Optional<ButtonType> option = alert.showAndWait();
     try {
         if (option.get().equals(ButtonType.OK)) {

             logout.getScene().getWindow().hide();
             Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
             Stage stage = new Stage();
             Scene scene = new Scene(root);

             root.setOnMousePressed((MouseEvent event) -> {
                 x = event.getSceneX();
                 y = event.getSceneY();
             });

             root.setOnMouseDragged((MouseEvent event) -> {
                 stage.setX(event.getScreenX() - x);
                 stage.setY(event.getScreenY() - y);

                 stage.setOpacity(.8);
             });

             root.setOnMouseReleased((MouseEvent event) -> {
                 stage.setOpacity(1);
             });

             stage.initStyle(StageStyle.TRANSPARENT);

             stage.setScene(scene);
             stage.show();
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
    //**********************************************************************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeTotalLiv();
        totalCmd();
        addLivShowListData();
        commandeShowListData();

    }
}
