package service;

import dao.ConnectionDao;
import dao.DeviceDao;
import ui.UiController;

import java.sql.SQLException;

public class ConnectionService {
    private final UiController uiController;
    private final ConnectionDao connectionDao;
    private final DeviceDao deviceDao;

    public ConnectionService(UiController uiController, ConnectionDao connectionDao, DeviceDao deviceDao) {
        this.uiController = uiController;
        this.connectionDao = connectionDao;
        this.deviceDao = deviceDao;
    }


    public void addConnection() throws SQLException {
        var allDevices = deviceDao.getAllDevices();
        if (allDevices.size() < 2) {
            uiController.printError("Not enough devices");
            return;
        }

        var connection = uiController.readConnection();
        var deviceFrom = uiController.selectOf(allDevices, "device from");
        var remainedDevices = allDevices.stream()
                .filter(device -> !device.equals(deviceFrom))
                .toList();
        var deviceTo = uiController.selectOf(remainedDevices, "device to");

        connection.setDeviceFromId(deviceFrom.getId());
        connection.setDeviceToId(deviceTo.getId());
        connection = connectionDao.save(connection);
        uiController.print(connection);
    }

    public void removeConnection() throws SQLException {
        var connections = connectionDao.getAllConnections();
        var connectionToRemove = uiController.selectOf(connections, "connection to remove");
        connectionDao.remove(connectionToRemove.getId());
        uiController.printError("Connection " + connectionToRemove.getId() + " was removed");

    }
}
