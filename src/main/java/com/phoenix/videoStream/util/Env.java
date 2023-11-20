package com.phoenix.videoStream.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class Env {
    private static Properties properties=new Properties();
    static{
        try{
            InputStream is=Env.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(is);
        }catch(Exception e){

        }
    }
    public static String get(String key){
        return properties.getProperty(key);
    }
    public static Properties getProperties(){
        return properties;
    }
}
