package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class Main extends Application implements Serializable{

    @FXML
    TextField regLoginField, authLoginField, addLoginField, nameSystemField, showLoginField, showPassField;
    @FXML
    PasswordField regPassField, regPassControlField, authPassField, addPassField;
    @FXML
    ListView listView, removeList;
    @FXML
    Hyperlink url;

    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<AccountInfo> passwords = new ArrayList<>();

    static RSA rsa;


    static Stage primaryStage;
    static Scene login, work;
    static User authUser;

    static String userLogin, userPassword;

    //TODO разделить на сцены, оформление

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        rsa = new RSA();

        try {
            loadList();
            loadPasswords();
        } catch (IOException e){}
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        login = new Scene(root);
        work = new Scene(FXMLLoader.load(getClass().getResource("work.fxml")));
        primaryStage.setTitle("PasswordSaver");
        primaryStage.setScene(login);
        primaryStage.show();
    }

    public void registration(){
        if (regPassField.getText().equals(regPassControlField.getText()) && !(checkLogin(regLoginField.getText())) && !(regLoginField.getText().trim().equals("") || regPassField.getText().trim().equals(""))){
            users.add(new User(regLoginField.getText(), regPassField.getText()));
            regLoginField.setText("");
            regPassField.setText("");
            regPassControlField.setText("");
            try {
                saveList();
            } catch (IOException e){}
            JOptionPane.showMessageDialog(null, "Пользователь успешно зарегистрирован", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (!(regPassField.getText().equals(regPassControlField.getText()))){
            regLoginField.setText("");
            regPassField.setText("");
            regPassControlField.setText("");
            JOptionPane.showMessageDialog(null, "Поле пароль и поле подтверждение не совпадают", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (checkLogin(regLoginField.getText())){
            regLoginField.setText("");
            regPassField.setText("");
            regPassControlField.setText("");
            JOptionPane.showMessageDialog(null, "Данный пользователь уже существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (regLoginField.getText().trim().equals("") || regPassField.getText().trim().equals("")){
            regLoginField.setText("");
            regPassField.setText("");
            regPassControlField.setText("");
            JOptionPane.showMessageDialog(null, "Не оставляйте поля пустыми", "Ошибка", JOptionPane.WARNING_MESSAGE);
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
        RSA rsa = new RSA();
        BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
        String saveString = "";
        for (User user : users){
            saveString += rsa.encrypt(user.getLogin()) + " " + rsa.encrypt(user.getPassword()) + "\n";
        }
        writer.write(saveString);
        writer.close();
    }

    public void loadList() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
        while (reader.ready()){
            String temp = reader.readLine();
            if (temp.trim().equals("")){
                continue;
            }
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
        authLoginField.setText("");
        authPassField.setText("");
        if (!(login.trim().equals("") || password.trim().equals("")) && checkUser(login, password)){

            userLogin = login;
            userPassword = password;
            primaryStage.setScene(work);
            authorize(userLogin, userPassword);
            JOptionPane.showMessageDialog(null, "Вы успешно вошли в аккаунт!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (login.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Не оставляйте поля пустыми", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
        else if (!checkUser(login, password)){
            JOptionPane.showMessageDialog(null, "Данного пользователя не существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void authorize(String login, String password){
        authUser = getUserByLoginAndPassword(login, password);
    }

    public void showAccountsTab(){
        authUser = getUserByLoginAndPassword(userLogin, userPassword);
        try {
            showAccounts();
        } catch (NullPointerException e){}
    }

    public void showRemoveTab(){
        authUser = getUserByLoginAndPassword(userLogin, userPassword);
        try {
            showAccounts();
        } catch (NullPointerException e){}
    }

    public void showAddTab(){
        authUser = getUserByLoginAndPassword(userLogin, userPassword);
        try {
            showAccounts();
        } catch (NullPointerException e){}
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
        nameSystemField.setText("");
        addLoginField.setText("");
        addPassField.setText("");
        if (authUser != null && (checkNotEmpty(systemName) && checkNotEmpty(login) && checkNotEmpty(password)) && !isAccountCreated(systemName, authUser)){
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
        else if (isAccountCreated(systemName, authUser)){
            JOptionPane.showMessageDialog(null, "Данный аккаунт уже существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isAccountCreated(String account, User user){
        for (AccountInfo accountInfo : passwords){
            if (accountInfo.getAccount().equals(account) && accountInfo.getUser().getLogin().equals(user.getLogin())){
                return true;
            }
        }
        return false;
    }

    public void savePasswords() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("passwords.txt"));
        String resString = "";
        for (int i = 0; i < passwords.size(); i++){
            AccountInfo accountInfo = passwords.get(i);
            resString += rsa.encrypt(accountInfo.getUser().getLogin()) + " " + rsa.encrypt(accountInfo.getAccount()) + " " + rsa.encrypt(accountInfo.getLogin()) + " " + rsa.encrypt(accountInfo.getPassword()) + "\n";
        }
        writer.write(resString);
        writer.close();
    }

    public void loadPasswords() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
        while (reader.ready()){
            String tempString = reader.readLine();
            if (tempString.trim().equals("")){
                continue;
            }
            String[] info = tempString.split(" ");
            User user = getUserByLogin(rsa.decrypt(info[0]));
            int nameAccountLength = info.length - 3;
            String nameAccount = "";
            for (int i = 1; i <= nameAccountLength; i++){
                if (i != nameAccountLength) {
                    nameAccount += rsa.decrypt(info[i]) + " ";
                }
                else{
                    nameAccount += rsa.decrypt(info[i]);
                }
            }
            String loginAccount = rsa.decrypt(info[info.length - 2]);
            String passAccount = rsa.decrypt(info[info.length - 1]);
            AccountInfo accountInfo = new AccountInfo(user, nameAccount, loginAccount, passAccount);
            accountInfo.url = accountInfo.getURL();
            passwords.add(accountInfo);
        }
        reader.close();
    }

    public User getUserByLogin(String login){
        return getUserByLoginAndPassword(login, getPasswordByLogin(login));
    }

    public void getEditableInfo(){
        authUser = getUserByLoginAndPassword(userLogin, userPassword);
        AccountInfo selectedItem = (AccountInfo) listView.getSelectionModel().getSelectedItem();
        if (authUser != null && selectedItem != null){
            for (int i = 0; i < passwords.size(); i++){
                AccountInfo tempInfo = passwords.get(i);
                if (tempInfo.getUser().getLogin().equals(authUser.getLogin()) && selectedItem.getAccount().equals(passwords.get(i).getAccount())){
                    showLoginField.setText(tempInfo.getLogin());
                    showPassField.setText(tempInfo.getPassword());
                    url.setText(tempInfo.getURL());
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
        for (AccountInfo accountInfo : passwords){
            if (accountInfo.getUser().getLogin().equals(authUser.getLogin())){
                list.add(accountInfo);
            }
        }
        listView.setItems(list);
        removeList.setItems(list);
    }

    public void removeAccount(){
        authUser = getUserByLoginAndPassword(userLogin, userPassword);
        AccountInfo accountInfo = (AccountInfo)removeList.getSelectionModel().getSelectedItem();
        int code = JOptionPane.showConfirmDialog(null, "Вы точно хотите удалить аккаунт?", "Удаление аккаунта", JOptionPane.YES_NO_OPTION);
        if (code == JOptionPane.NO_OPTION){
            return;
        }
        for (int i = 0; i < passwords.size(); i++){
            if (accountInfo.getAccount().equals(passwords.get(i).getAccount()) &&
                accountInfo.getUser().getLogin().equals(passwords.get(i).getUser().getLogin())){
                passwords.remove(i);
                showLoginField.setText("");
                showPassField.setText("");
                url.setText("");
                break;
            }
        }
        showAccounts();
        try {
            savePasswords();
        }catch (IOException e){}
    }

    public void openLink(){
        AccountInfo accountInfo = (AccountInfo)listView.getSelectionModel().getSelectedItem();
        getHostServices().showDocument("https://" + accountInfo.getURL());
    }

    public void changeAccount(){
        primaryStage.setScene(login);
    }



}
