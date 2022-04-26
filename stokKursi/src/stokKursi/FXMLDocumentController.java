package stokKursi;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private DatePicker tfYear;
    @FXML
    private TextField tfPages;
    @FXML
    private TableView<stokKursi> tvBooks;
    @FXML
    private TableColumn<stokKursi, Integer> colId;
    @FXML
    private TableColumn<stokKursi, String> colTitle;
    @FXML
    private TableColumn<stokKursi, String> colAuthor;
    @FXML
    private TableColumn<stokKursi, String> colYear;
    @FXML
    private TableColumn<stokKursi, String> colPages;
    @FXML
    private Button btnInsert;
 
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnInsert){
            insertRecord();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         showBooks();
         
    }    
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kursi", "root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
    
    public ObservableList<stokKursi> getBooksList(){
        ObservableList<stokKursi> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM kursi";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            stokKursi kursi;
            while(rs.next()){
                kursi = new stokKursi(rs.getInt("id"), rs.getString("nama_kursi"), rs.getString("warna"), rs.getString("tanggal"),rs.getString("harga"));
                bookList.add(kursi);
            }
                
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return bookList;
    }
    
    public void showBooks(){
        ObservableList<stokKursi> list = getBooksList();
        
        colId.setCellValueFactory(new PropertyValueFactory<stokKursi, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<stokKursi, String>("nama_kursi"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<stokKursi, String>("warna"));
        colYear.setCellValueFactory(new PropertyValueFactory<stokKursi, String>("tanggal"));
        colPages.setCellValueFactory(new PropertyValueFactory<stokKursi, String>("harga"));
        
        tvBooks.setItems(list);
    }
    
    private void insertRecord(){
        String query = "INSERT INTO kursi VALUES (" + tfId.getText() + ",'" + tfTitle.getText() + "','" + tfAuthor.getText() + "',"
                + tfYear.getValue().toString() + "," + tfPages.getText() + ")";
        executeQuery(query);
        showBooks();
    }
    
    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
