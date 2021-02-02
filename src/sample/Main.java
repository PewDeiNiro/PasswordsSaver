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
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    @FXML
    TextField regLoginField, authLoginField;
    @FXML
    PasswordField regPassField, regPassControlField, authPassField;

    static ArrayList<User> users = new ArrayList<>();

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
}
