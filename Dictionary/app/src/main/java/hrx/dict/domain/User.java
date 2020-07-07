package hrx.dict.domain;


public class User {

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private  int _id;

    private String loginname;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//getter setter 方法

    public String getLoginpsw() {
        return loginpsw;
    }

    public void setLoginpsw(String loginpsw) {
        this.loginpsw = loginpsw;
    }

    private String loginpsw;

    private String email;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    private String tel;


}
