package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class Main extends Application {

    @FXML
    TextField regLoginField, authLoginField, addLoginField, nameSystemField;
    @FXML
    PasswordField regPassField, regPassControlField, authPassField, addPassField;

    static ArrayList<User> users = new ArrayList<>();
    static HashMap<HashMap<User,String>, HashMap<String, String>> passwords = new HashMap<>();

    RSA rsa;

    User authUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            loadList();
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
            saveString = saveString + user.getLogin() + " " + user.getPassword() + "\n";
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
                users.add(new User(tempArray[0], tempArray[1]));
            }
        }
        reader.close();

    }

    public void printList(List list){
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
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
            HashMap<String, String> info = new HashMap<>();
            HashMap<User, String> name = new HashMap<>();
            info.put(login, password);
            name.put(authUser, systemName);
            passwords.put(name, info);
            try {
                savePasswords();
            } catch (IOException e){}
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
        Iterator<Map.Entry<HashMap<User, String>, HashMap<String, String>>> passIterator = passwords.entrySet().iterator();
        while (passIterator.hasNext()){
            Map.Entry<HashMap<User, String>, HashMap<String, String>> pair = passIterator.next();
            HashMap<User,String> name = pair.getKey();
            Iterator<Map.Entry<User, String>> nameIterator = name.entrySet().iterator();
            while (nameIterator.hasNext()){
                Map.Entry<User, String> namePair = nameIterator.next();
                User user = namePair.getKey();
                String systemName = namePair.getValue();
                resString += user.getLogin() + " " + systemName + " ";
            }
            HashMap<String, String> info = pair.getValue();
            Iterator<Map.Entry<String, String>> infoIterator = info.entrySet().iterator();
            while (infoIterator.hasNext()){
                Map.Entry<String, String> infoPair = infoIterator.next();
                String login = infoPair.getKey();
                String password = infoPair.getValue();
                resString += login + " " + password + "\n";
            }
        }
        writer.write(resString);
        writer.close();
    }
}
