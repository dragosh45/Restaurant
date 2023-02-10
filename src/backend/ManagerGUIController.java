package backend;

import java.awt.Button;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import kitchen.KitchenController;

public class ManagerGUIController implements Initializable {

    @FXML
    private ResourceBundle resources;
    
    
    ObservableList<Orders> list;

    @FXML
    private URL location;
    
    @FXML
    private TabPane TapPane;

    @FXML
    private TableView<Tables> tablesView;
    
    @FXML
    private TableView<Orders> searchOrdersView;
    
    @FXML
    private Button search;
    
    @FXML
    private Button reset;
    
    @FXML
    private Button clear;
    
    @FXML
    private TableView<Orders> ordersView ;

    @FXML
    private TableColumn<Tables, String> tableNo1;
    

    @FXML
    private TableColumn<Tables, String> staffId;

    @FXML
    private TableColumn<Tables, String> assist;
    
    @FXML
    private TableColumn<Orders, String> tableNo2;
    
    @FXML
    private TableColumn<Orders, String> staffId2;
    
    @FXML  
    private TableColumn<Orders, String> orderNo;

    @FXML
    private TableColumn<Orders, String> orderSt;

    @FXML
    private TableColumn<Orders, String> orderTime;

    @FXML
    private TableColumn<Orders, String> payment;
    
    @FXML
    private TableColumn<Orders, String> searchTableNo;
    
    @FXML
    private TableColumn<Orders, String> searchStaffId;
    
    @FXML  
    private TableColumn<Orders, String> searchOrderNo;

    @FXML
    private TableColumn<Orders, String> searchOrderSt;

    @FXML
    private TableColumn<Orders, String> searchOrderTime;

    @FXML
    private TableColumn<Orders, String> searchPayment;

    @FXML
    private TextField tableNoSearch;

    @FXML
    private TextField staffIDSearch;

    @FXML
    private ComboBox<Status> orderStatus;

    @FXML
    private ComboBox<String> paymentOption;
    
    
	private ArrayList<Orders> ordersList;
	private ArrayList<Tables> tablesList;
	private ArrayList<Orders> searchList;

    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
        
        orderStatus.getItems().removeAll(orderStatus.getItems());
        orderStatus.getItems().addAll(Status.UNCONFIRMED,Status.KITCHEN, Status.READY, Status.SENT);
        paymentOption.getItems().removeAll(paymentOption.getItems());
        paymentOption.getItems().addAll("CONFIRMED", "UNCONFIRMED");
        
        orderNo.setCellValueFactory(new PropertyValueFactory<Orders,String>("OrderNo"));
    	tableNo2.setCellValueFactory(new PropertyValueFactory<Orders,String>("tableNo"));
    	staffId2.setCellValueFactory(new PropertyValueFactory<Orders,String>("staffId"));
    	orderSt.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));
    	orderTime.setCellValueFactory(new PropertyValueFactory<Orders,String>("time"));
    	payment.setCellValueFactory(new PropertyValueFactory<Orders,String>("paid"));
    	
        searchOrderNo.setCellValueFactory(new PropertyValueFactory<Orders,String>("OrderNo"));
    	searchTableNo.setCellValueFactory(new PropertyValueFactory<Orders,String>("tableNo"));
    	searchStaffId.setCellValueFactory(new PropertyValueFactory<Orders,String>("staffId"));
    	searchOrderSt.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));
    	searchOrderTime.setCellValueFactory(new PropertyValueFactory<Orders,String>("time"));
    	searchPayment.setCellValueFactory(new PropertyValueFactory<Orders,String>("paid"));
    	
        new AnimationTimer() {
            @Override
            public void handle(long now ) {
            	try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	update();
            }
          }.stop();
          
          // the timer is inactive
          update();
    }
    
    void update() {
        setOrdersData();
        setSearchData("1");
    }
    void clearTableViews() {
    	ordersView.getItems().clear();
    	searchOrdersView.getItems().clear();
    }
    
    void setOrdersData() 
    {
    	list = null;
    	ordersList = ResturantData.getOrdersList();
    	list = FXCollections.observableArrayList(ordersList);
    	ordersView.setItems(list);
    }
    
    void setSearchData(String searchItem) {
    	list = null;
    	searchList = ResturantData.getSearhDataList(searchItem);
    	list = FXCollections.observableArrayList(searchList);
    	System.out.println(list.size());
    	searchOrdersView.setItems(list);
    	
    }
    void search() {
    	if (tableNoSearch.getText()==null) {
    		if(staffIDSearch.getText()!=null) {
    			if(orderStatus.getSelectionModel().isEmpty()) {
    				if(!paymentOption.getSelectionModel().isEmpty()) {
    					setSearchData(paymentOption.getSelectionModel().toString());
    				}
    				else{
    					setSearchData(orderStatus.getSelectionModel().toString());
    				}
    			}
    			else {
    				setSearchData(staffIDSearch.getText());
    			}
    		}
    		else {
    			setSearchData(tableNoSearch.getText());
    		}
    	}
    }
    
    void resetFields() {
    	tableNoSearch.clear();
    	staffIDSearch.clear();
    	orderStatus.setValue(null);
    	paymentOption.setValue(null);
    }
}
