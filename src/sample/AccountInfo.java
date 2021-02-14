package sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AccountInfo {

    private User user;
    private String account, login, password;
    public String url;
    static HashMap<String, String> urls = new HashMap<>();

    public AccountInfo(User user, String account, String login, String password){
        generateMap();
        this.user = user;
        this.account = account;
        this.login = login;
        this.password = password;
        url = getURL();
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

    public String getURL(){
        return checkKeyOfMap(urls, account) ? urls.get(account) : "";
    }

    public boolean checkKeyOfMap(HashMap<String, String> map, String key){
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> pair = iterator.next();
            if (pair.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    public void generateMap(){
        urls.put("vk.com", "vk.com");
        urls.put("vk", "vk.com");
        urls.put("вк", "vk.com");
        urls.put("вконтакте", "vk.com");

        urls.put("ok.ru", "ok.ru");
        urls.put("ok", "ok.ru");
        urls.put("одноклассники", "ok.ru");

        urls.put("mail.ru", "mail.ru");
        urls.put("mailru", "mail.ru");
        urls.put("мыло", "mail.ru");
        urls.put("mail", "mail.ru");

        urls.put("gmail.com", "gmail.com");
        urls.put("gmail", "gmail.com");

        urls.put("instagram", "instagram.com");
        urls.put("inst", "instagram.com");
        urls.put("инста", "instagram.com");
        urls.put("инст", "instagram.com");

        urls.put("рнкб", "rncb.ru");
        urls.put("rncb", "rncb.ru");
        urls.put("rncb.ru", "rncb.ru");

        urls.put("telegram", "web.telegram.org");
        urls.put("web.telegram.org", "web.telegram.org");
        urls.put("tg", "web.telegram.org");
        urls.put("тг", "web.telegram.org");
        urls.put("телега", "web.telegram.org");

        urls.put("viber", "viber.com");
        urls.put("вайбер", "viber.com");
        urls.put("viber.com", "viber.com");

        urls.put("whatsapp", "whatsapp.com");
        urls.put("whatsapp.com", "whatsapp.com");
        urls.put("ватсап", "whatsapp.com");
        urls.put("whatsup", "whatsapp.com");

        urls.put("youtube", "youtube.com");
        urls.put("yotube.com", "youtube.com");
        urls.put("ютуб", "youtube.com");
        urls.put("ютьюб", "youtube.com");

        urls.put("google", "google.com");
        urls.put("google.com", "google.com");
        urls.put("гугл", "google.com");

        urls.put("steam", "steam.com");
        urls.put("steam.com", "steam.com");
        urls.put("стим", "steam.com");

        urls.put("discord", "discord.com");
        urls.put("discord.com", "discord.com");
        urls.put("дс", "discord.com");
        urls.put("дискорд", "discord.com");

        urls.put("twitch", "twitch.tv");
        urls.put("twitch.tv", "twitch.tv");
        urls.put("твич", "twitch.tv");

        urls.put("tiktok", "tiktok.com");
        urls.put("tik tok", "tiktok.com");
        urls.put("тикток", "tiktok.com");
        urls.put("тик ток", "tiktok.com");
    }

}
