package service;

import dao.NetworkDao;
import model.Network;
import ui.UiController;

import java.sql.SQLException;
import java.util.List;

public class NetworksService {
    private final UiController uiController;
    private final NetworkDao networkDao;

    public NetworksService(UiController uiController, NetworkDao networkDao) {
        this.uiController = uiController;
        this.networkDao = networkDao;
    }

    public void addNetwork() throws SQLException {
        Network network = uiController.readNetwork();
        network = networkDao.save(network);
        uiController.print(network);
    }

    public void editNetwork() throws SQLException {
        List<Network> networks = networkDao.getAllNetworks();
        var networkToEdit = uiController.selectOf(networks, "network to edit");
        networkToEdit = uiController.updateNetwork(networkToEdit);
        networkToEdit = networkDao.update(networkToEdit);
        uiController.print(networkToEdit);
    }

    public void removeNetwork() throws SQLException {
        var networks = networkDao.getEmptyNetworks();
        var networkToRemove = uiController.selectOf(networks, "connection to remove");
        networkDao.remove(networkToRemove.getId());
        uiController.printError("Connection " + networkToRemove.getId() + " was removed");
    }

}