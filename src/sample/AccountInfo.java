package sample;

public class AccountInfo {

    private User user;
    private String account, login, password;

    public AccountInfo(User user, String account, String login, String password){
        this.user = user;
        this.account = account;
        this.login = login;
        this.password = password;
    }

    public User getUser(){
        return user;
    }
    public String getAccount(){
        return account;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public String toString(){
        return account;
    }

}
