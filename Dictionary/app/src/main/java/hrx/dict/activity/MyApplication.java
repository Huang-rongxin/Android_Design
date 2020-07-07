package hrx.dict.activity;

import android.app.Application;
import hrx.dict.domain.User;

public class MyApplication extends Application {

    private static MyApplication INSTANCE = null;
    private User _user;

    @Override
    public void onCreate() {

        super.onCreate();

        INSTANCE = this;

    }
    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }

    public static synchronized MyApplication getInstance() {
        return INSTANCE;
    }

}
