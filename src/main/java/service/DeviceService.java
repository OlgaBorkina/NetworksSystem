package service;

import dao.DeviceDao;
import dao.NetworkDao;
import ui.UiController;
import util.InputValid;

import java.sql.SQLException;

public class DeviceService {
    private final UiController uiController;
    private final DeviceDao deviceDao;
    private final NetworkDao networkDao;

    public DeviceService(UiController uiController, DeviceDao deviceDao, NetworkDao networkDao) {
        this.uiController = uiController;
        this.deviceDao = deviceDao;
        this.networkDao = networkDao;
    }


    public void addDevice() throws SQLException {
            var device = uiController.readDevice();
            var networks = networkDao.getAllNetworks();
            if (networks.isEmpty()) {
                uiController.printError("No networks found; add network first");
                return;
            }
            if(!InputValid.isValidIp(device.getIpAddress())){
                uiController.printError("Invalid IP address");
                return;
            }

            var network = uiController.selectOf(networks, "network");
            device.setNetworkId(network.getId());
            device = deviceDao.save(device);
            uiController.print(device);
    }
}
