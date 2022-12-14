package controller;

import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import database.DBAppointments;
import database.DBContacts;
import database.DBCountries;
import database.DBCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Controller class for the reports screen.
 */

/**
 *
 * @author Brandon Mukum
 */
public class reportsController implements Initializable {
    public TableView monthTable;
    public TableColumn mMonthCol;
    public TableColumn mTotalCol;
    public TableView typeTable;
    public TableColumn tTypeCol;
    public TableColumn tTotalColumn;
    public Label myReport;
    public TableView<typeReport> tytmTable;
    public TableColumn ContactIdCol;
    public TableColumn cTitleCol;
    public TableColumn cDescCol;
    public TableColumn cStartCol;
    public TableColumn cEndCol;
    public TableColumn cCIdCol;
    public ComboBox contactCB;
    public TableView contactTableView;
    public TableColumn cTypeCol;
    public TableColumn CEndCol;
    public ToggleGroup tg;
    public TableView monthTableView;
    public TableColumn monthCol;
    public TableColumn contactEndCol;
    public TableColumn CustomerIdCol;
    public TableView customerTableView;
    public TableColumn apptIdCol;
    public ComboBox customerCB;
    public TableColumn contactCol;
    public TableColumn startDateCol;

    /**
     * This method takes the user back to the main menu when the back button is clicked.
     */
    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main.fxml"));
            Parent root = loader.load();

            Stage addPartStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene addPartScene = new Scene(root, 1100, 700);
            addPartStage.setTitle("Main Screen");
            addPartStage.setScene(addPartScene);
            addPartStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize method
     * It contains a lambda function that gets input from a stream of different months of the year.
     * The lambda function 4 uses a stream-map combination that returns the months from the appoiments list and adds it to an allMonths list.
     * It continues to get input from the allMonths list, filters it, and adds the distinct month to another list.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
            ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();
            ObservableList<typeReport> typeReport = DBAppointments.getTypeMonthReport();

            contactCB.setItems(allContacts);
            contactCB.setPromptText("Select the contact");
            customerCB.setItems(allCustomers);
            customerCB.setPromptText("Select a customer");

            tytmTable.setItems(typeReport);
            tTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            tTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //total appointments by month
        ObservableList<Month> allMonths = FXCollections.observableArrayList();
        ObservableList<Month> apptMonth = FXCollections.observableArrayList();
        ObservableList<monthReport> reportByMonth = FXCollections.observableArrayList();

        //Lambda 4
        DBAppointments.getAllAppointments().stream().map(a -> {
            return a.getStart().toLocalDateTime().getMonth();
        }).forEach(allMonths::add);

        allMonths.stream().filter(m -> {
            return !apptMonth.contains(m);
        }).forEach(apptMonth::add);

        for (Month m : allMonths) {
            int total = Collections.frequency(allMonths, m);
            String month = m.name();
            monthReport mr = new monthReport(month, total);
            reportByMonth.add(mr);
        }
    }


    /**
     * It contains a lambda function that gets input from a stream of different months of the year.
     * The lambda function 3 helps to check if a list of appointments have contact IDs that match a selected contact object. It returns true of there's a match and false if there is not.
     * The resulting list is then set to the table.
     */
    public void contactSelection(ActionEvent actionEvent) throws SQLException {
        ObservableList<Contacts> allContacts = DBContacts.getAllContacts();
        Contacts c = (Contacts) contactCB.getSelectionModel().getSelectedItem();
        int contactId = c.getId();

        ObservableList<Appointments> allAppts = DBAppointments.getAllAppointments();

        //Lambda 3
        ObservableList<Appointments> contactAppts = allAppts.filtered(a->{
            if (a.getContactId() == contactId)
                return true;
            return false;
        });

        if (contactAppts.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("This contact has nothing scheduled yet!");
            alert.showAndWait();
        }
        contactTableView.setItems(contactAppts);
        ContactIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        cTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        cDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        cStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * This method is controlled by the customer comboBox. When a customer is selected, the tables displays all the customer's future appointments, the start data and who the contact for each appointment is.
     */
    public void onCustomerCB(ActionEvent actionEvent) {
        Customers c = (Customers) customerCB.getSelectionModel().getSelectedItem();
        int customerId = c.getId();

        ObservableList<Appointments> ca = FXCollections.observableArrayList();
        ObservableList<Appointments> allAppts = DBAppointments.getAllAppointments();
        ObservableList<Appointments> myReport = FXCollections.observableArrayList();

        for (Appointments a : allAppts){
            if ((a.getCustomerId() == customerId) && a.getStart().toLocalDateTime().isAfter(LocalDateTime.now())){
                ca.add(a);
            }
        }

        customerTableView.setItems(ca);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        if (ca.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("The selected customer has no future appointments.");
            alert.showAndWait();
        }
    }
}
