package service;

import ui.UiController;

public class ApplicationService {
    private final UiController uiController;
    private final NetworksService networksService;
    private final DeviceService deviceService;
    private final ConnectionService connectionService;

    public ApplicationService(UiController uiController, NetworksService networksService, DeviceService deviceService, ConnectionService connectionService) {
        this.uiController = uiController;
        this.networksService = networksService;
        this.deviceService = deviceService;
        this.connectionService = connectionService;
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
                    switch (userChoice) {
                        case ADD_NETWORK -> networksService.addNetwork();
                        case EDIT_NETWORK -> networksService.editNetwork();
                        case REMOVE_NETWORK -> networksService.removeNetwork();

                        case ADD_DEVICE -> deviceService.addDevice();
                        case ADD_CONNECTION -> connectionService.addConnection();
                        case REMOVE_CONNECTION -> connectionService.removeConnection();
                        default -> uiController.printError("Unknown action");


                    }
                } catch (Exception e) {
                    uiController.printError(e.getMessage());
                }

            } else {
                uiController.printError("Code was incorrect; try again");
            }
        }
    }
}
