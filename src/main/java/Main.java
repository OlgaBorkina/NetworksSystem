import dao.*;
import service.*;
import ui.UiController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
       UiController uiController = new UiController(new Scanner(System.in), System.out);

       NetworkDao networkDao = new NetworkDao();
        DeviceDao deviceDao = new DeviceDao();
        ConnectionDao connectionDao = new ConnectionDao();

        NetworksService networksService = new NetworksService(uiController, networkDao);
        DeviceService deviceService = new DeviceService(uiController, deviceDao, networkDao);
        ConnectionService connectionService = new ConnectionService(uiController, connectionDao, deviceDao);

        ApplicationService appService = new ApplicationService(
                uiController,
                networksService,
                deviceService,
                connectionService
        );

        appService.process();

    }
}
