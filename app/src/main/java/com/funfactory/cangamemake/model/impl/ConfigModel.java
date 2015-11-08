package com.funfactory.cangamemake.model.impl;

import java.util.List;

import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.dao.IConfigDAO;
import com.funfactory.cangamemake.model.dao.impl.ConfigDAO;
import com.funfactory.cangamemake.model.entity.Config;

public class ConfigModel implements IConfigModel {

    private IConfigDAO configDAO = new ConfigDAO();

    @Override
    public List<Config> listAll() {

        return configDAO.getAll();
    }

    @Override
    public void saveOrUpdate(Config object) {
        configDAO.save(object);
    }

    @Override
    public void remove(Config object) {
        configDAO.delete(object);
    }

    @Override
    public Config getConfiguration() {

        Config configuration = null;
        
        List<Config> configs = listAll();
        
        if (configs != null && configs.size() > 0) {
            configuration = configs.get(0);
        } else {
            configuration = new Config();
        }

        return configuration;
    }

}
