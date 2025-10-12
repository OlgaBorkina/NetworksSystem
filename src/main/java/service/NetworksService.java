package service;

import dao.NetworkDao;
import ui.UiController;

import java.sql.SQLException;

public class NetworksService {
    private final UiController uiController;
    private final NetworkDao networkDao;

    public NetworksService(UiController uiController, NetworkDao networkDao) {
        this.uiController = uiController;
        this.networkDao = networkDao;
    }


    public void process() {
        while (true) {
            int userCode = uiController.getUserChoice();
            var action = UserChoice.valueOf(userCode);
            if (action.isPresent()) {
                var userChoice = action.get();
                if (userChoice == UserChoice.EXIT)
                    break;

                try {
                    processChoice(userChoice);
                } catch (Exception e) {
                    uiController.printError(e.getMessage());
                }

            } else {
                uiController.printError("Code was incorrect; try again");
            }
        }
    }

    public void processChoice(UserChoice userChoice) throws SQLException {
        switch (userChoice) {

            case UserChoice.ADD_NETWORK -> {
                var network = uiController.readNetwork();
                network = networkDao.save(network);
                uiController.print(network);
            }
            case ADD_DEVICE -> {
                var device = uiController.readDevice();

                var networks = networkDao.getAllNetworks();
                if (networks.isEmpty()) {
                    uiController.printError("No networks found; add network first");
                    return;
                }

                var network = uiController.selectOf(networks, "network");
                device.setNetworkId(network.getId());
                device = networkDao.save(device);
                uiController.print(device);
            }
            case ADD_CONNECTION -> {
                var allDevices = networkDao.getAllDevices();
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
                connection = networkDao.save(connection);
                uiController.print(connection);
            }
            case EDIT_NETWORK -> {
                var networks = networkDao.getAllNetworks();
                var networkToEdit = uiController.selectOf(networks, "network to edit");
                networkToEdit = uiController.updateNetwork(networkToEdit);
                networkToEdit = networkDao.update(networkToEdit);
                uiController.print(networkToEdit);
            }
            case REMOVE_CONNECTION -> {
                var connections = networkDao.getAllConnections();
                var connectionToRemove = uiController.selectOf(connections, "connection to remove");
                networkDao.remove(connectionToRemove, "connection");
                uiController.printError("Connection " + connectionToRemove.getId() + " was removed");
            }
            case REMOVE_NETWORK -> {
                var networks = networkDao.getEmptyNetworks();

                if (networks.isEmpty()) {
                    uiController.printError("All networks have related devices. Remove them first");
                    return;
                }

                var networkToRemove = uiController.selectOf(networks, "network to remove");
                networkDao.remove(networkToRemove, "network");
                uiController.printError("Network " + networkToRemove.getId() + " was removed");
            }
            default -> uiController.printError("Something went wrong");
        }
    }
}