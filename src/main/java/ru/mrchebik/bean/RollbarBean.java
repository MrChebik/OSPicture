package ru.mrchebik.bean;

import com.rollbar.Rollbar;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by mrchebik on 6/23/17.
 */
@Component("rollbar")
public class RollbarBean implements FactoryBean<Rollbar> {
    private Rollbar rollbar = new Rollbar("cfd8db9051aa4aa081a41704e248d632", "production");

    @Override
    public Rollbar getObject() throws Exception {
        return rollbar;
    }

    @Override
    public Class<? extends Rollbar> getObjectType() {
        return rollbar.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}