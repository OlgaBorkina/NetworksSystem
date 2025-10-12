import dao.NetworkDao;
import service.NetworksService;
import ui.UiController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        var uiController = new UiController(scanner, System.out);

        var networkDao = new NetworkDao();
        var networkService = new NetworksService(uiController, networkDao);
        networkService.process();
    }
}