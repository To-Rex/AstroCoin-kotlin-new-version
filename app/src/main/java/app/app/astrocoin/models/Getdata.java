package app.app.astrocoin.models;

public class Getdata {
    /*val id: String,
    val name: String,
    val last_name: String,
    val qwasar: String,
    val email: String,
    val number: String,
    val stack: String,
    val role: String,
    val status: String,
    val verify: String,
    val photo: String,
    val m_photo: String,
    val balance: String,
    val wallet: String*/
    private String id;
    private String name;
    private String last_name;
    private String qwasar;
    private String email;
    private String number;
    private String stack;
    private String role;
    private String status;
    private String verify;
    private String photo;
    private String m_photo;
    private String balance;
    private String wallet;

    public Getdata(String id, String name, String last_name, String qwasar, String email, String number, String stack, String role, String status, String verify, String photo, String m_photo, String balance, String wallet) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.qwasar = qwasar;
        this.email = email;
        this.number = number;
        this.stack = stack;
        this.role = role;
        this.status = status;
        this.verify = verify;
        this.photo = photo;
        this.m_photo = m_photo;
        this.balance = balance;
        this.wallet = wallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getQwasar() {
        return qwasar;
    }

    public void setQwasar(String qwasar) {
        this.qwasar = qwasar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getM_photo() {
        return m_photo;
    }

    public void setM_photo(String m_photo) {
        this.m_photo = m_photo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
