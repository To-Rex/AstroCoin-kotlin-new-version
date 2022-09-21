package app.app.astrocoin.models;

public class Getdata {

    String id;
    String balance;
    String email;
    String last_name;
    String name;
    String number;
    String photo;
    String qwasar;
    String role;
    String stack;
    String status;
    String wallet;

    public Getdata(String id, String balance, String email, String last_name, String name, String number, String photo, String qwasar, String role, String stack, String status, String wallet) {
        this.id = id;
        this.balance = balance;
        this.email = email;
        this.last_name = last_name;
        this.name = name;
        this.number = number;
        this.photo = photo;
        this.qwasar = qwasar;
        this.role = role;
        this.stack = stack;
        this.status = status;
        this.wallet = wallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getQwasar() {
        return qwasar;
    }

    public void setQwasar(String qwasar) {
        this.qwasar = qwasar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
