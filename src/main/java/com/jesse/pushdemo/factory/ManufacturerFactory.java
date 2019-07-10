package com.jesse.pushdemo.factory;

import com.jesse.pushdemo.repository.Manufacturer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jessehsj
 */
public class ManufacturerFactory {
    private static Logger log = (Logger) LoggerFactory.getLogger(ManufacturerFactory.class);

    public static Manufacturer getManufacturer(String manufacturerCode) {
        Manufacturer manufacturer = null;
        try {
            Object o = Class.forName("com.jesse.pushdemo.repository.impl."+manufacturerCode).newInstance();
            manufacturer = (Manufacturer) o;
        } catch (Exception e) {
            log.info("ManufacturerFactory.getManufacturer " + " 不存在该实例:" + manufacturerCode);
        }
        return manufacturer;
    }
}
