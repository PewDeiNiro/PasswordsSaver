package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class Main extends Application {

    @FXML
    TextField regLoginField, authLoginField, addLoginField, nameSystemField, showLoginField, showPassField;
    @FXML
    PasswordField regPassField, regPassControlField, authPassField, addPassField;
    @FXML
    ListView listView, removeList;

    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<AccountInfo> passwords = new ArrayList<>();

    static RSA rsa;

    User authUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            loadList();
            loadPasswords();
        } catch (IOException e){

        }
        rsa = new RSA();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("PasswordSaver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void registration(){
        if (regPassField.getText().equals(regPassControlField.getText()) && !(checkLogin(regLoginField.getText()))){
            users.add(new User(regLoginField.getText(), regPassField.getText()));
            try {
                saveList();
            } catch (IOException e){}
            JOptionPane.showMessageDialog(null, "Пользователь успешно зарегистрирован", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (!(regPassField.getText().equals(regPassControlField.getText()))){
            JOptionPane.showMessageDialog(null, "Поле пароль и поле подтверждение не совпадают", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (checkLogin(regLoginField.getText())){
            JOptionPane.showMessageDialog(null, "Данный пользователь уже существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }
    //true is have login, false isn't have login
    public boolean checkLogin(String login){
        for (User user : users){
            if (user.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public void saveList() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
        String saveString = "";
        for (User user : users){
            saveString += user.getLogin() + " " + user.getPassword() + "\n";
        }
        writer.write(saveString);
        writer.close();
    }

    public void loadList() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
        while (reader.ready()){
            String temp = reader.readLine();
            if (!(temp.trim().equals(""))){
                String[] tempArray = temp.split(" ");
                users.add(new User(rsa.decrypt(tempArray[0]), rsa.decrypt(tempArray[1])));
            }
        }
        reader.close();

    }

    public boolean checkUser(String login, String password){
        for (int i = 0; i < users.size(); i++){
            User user = users.get(i);
            if (user.getLogin().equals(login) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public void authorization(){
        String login = authLoginField.getText();
        String password = authPassField.getText();
        if (!(login.trim().equals("") || password.trim().equals("")) && checkUser(login, password)){
            User tempUser = getUserByLoginAndPassword(login, password);
            if (tempUser == null){
                JOptionPane.showMessageDialog(null, "Данного пользователя не существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            authUser = tempUser;
            showAccounts();
            JOptionPane.showMessageDialog(null, "Вы успешно вошли в аккаунт!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (login.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Не оставляйте поля пустыми", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (!checkUser(login, password)){
            JOptionPane.showMessageDialog(null, "Данного пользователя не существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    public User getUserByLoginAndPassword(String login, String password){
        for (User user : users){
            if (user.getLogin().equals(login) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public String getPasswordByLogin(String login){
        for (User user : users){
            if (user.getLogin().equals(login)){
                return user.getPassword();
            }
        }
        return "";
    }

    public boolean checkNotEmpty(String text){
        if (text.trim().equals("")){
            return false;
        }
        return true;
    }

    public void addAccount(){
        String systemName = nameSystemField.getText();
        String login = addLoginField.getText();
        String password = addPassField.getText();
        if (authUser != null && (checkNotEmpty(systemName) && checkNotEmpty(login) && checkNotEmpty(password))){
            AccountInfo info = new AccountInfo(authUser, systemName, login, password);
            passwords.add(info);
            showAccounts();
            try {
                savePasswords();
            }catch (IOException e){}
            JOptionPane.showMessageDialog(null, "Аккаунт успешно добавлен", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (authUser == null){
            JOptionPane.showMessageDialog(null, "Войдите в аккаунт", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (!(checkNotEmpty(systemName) && checkNotEmpty(login) && checkNotEmpty(password))){
            JOptionPane.showMessageDialog(null, "Не оставляйте поля пустыми(при надобности на поле логин поставьте \"-\")", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void savePasswords() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("passwords.txt"));
        String resString = "";
        for (int i = 0; i < passwords.size(); i++){
            AccountInfo accountInfo = passwords.get(i);
            resString += accountInfo.getUser().getLogin() + " " + accountInfo.getAccount() + " " + accountInfo.getLogin() + " " + accountInfo.getPassword() + "\n";
        }
        writer.write(resString);
        writer.close();
    }

    public void loadPasswords() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
        while (reader.ready()){
            String tempString = reader.readLine();
            String[] info = tempString.split(" ");
            User user = getUserByLogin(info[0]);
            int nameAccountLength = info.length - 3;
            String nameAccount = "";
            for (int i = 1; i <= nameAccountLength; i++){
                nameAccount += info[i] + " ";
            }
            String loginAccount = info[info.length - 2];
            String passAccount = info[info.length - 1];
            AccountInfo accountInfo = new AccountInfo(user, nameAccount, loginAccount, passAccount);
            passwords.add(accountInfo);
        }
        reader.close();
    }

    public User getUserByLogin(String login){
        return getUserByLoginAndPassword(login, getPasswordByLogin(login));
    }

    public void getEditableInfo(){
        AccountInfo selectedItem = (AccountInfo) listView.getSelectionModel().getSelectedItem();
        if (authUser != null && selectedItem != null){
            for (int i = 0; i < passwords.size(); i++){
                AccountInfo tempInfo = passwords.get(i);
                if (tempInfo.getUser().getLogin().equals(authUser.getLogin()) && selectedItem.getAccount().equals(passwords.get(i).getAccount())){
                    showLoginField.setText(tempInfo.getLogin());
                    showPassField.setText(tempInfo.getPassword());
                }
            }
        }
        else if (authUser == null){
            JOptionPane.showMessageDialog(null, "Войдите в аккаунт", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (selectedItem == null){
            JOptionPane.showMessageDialog(null, "Выберите элемент списка", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void showAccounts() {
        ObservableList<AccountInfo> list = FXCollections.observableArrayList();
        list.addAll(passwords);
        listView.setItems(list);
        removeList.setItems(list);
    }

    public void removeAccount(){
        AccountInfo accountInfo = (AccountInfo)removeList.getSelectionModel().getSelectedItem();
        for (int i = 0; i < passwords.size(); i++){
            if (accountInfo.getAccount().equals(passwords.get(i).getAccount()) &&
            accountInfo.getUser().getLogin().equals(passwords.get(i).getUser().getLogin())){
                passwords.remove(i);
                break;
            }
        }
        showAccounts();
        try {
            savePasswords();
        }catch (IOException e){}
    }
}
