package ui;

import model.Device;
import model.DeviceConnection;
import model.Model;
import model.Network;
import service.UserChoice;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class UiController {
    private final Scanner scanner;
    private final PrintStream printStream;

    public UiController(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.printStream = printStream;
    }

    public int getUserChoice() {
        printStream.println("Enter: ");
        for (var action : UserChoice.values()) {
            System.out.println(action.getCode() + " " + action.getDescription());
        }

        int code = scanner.nextInt();
        scanner.nextLine();
        return code;
    }

    public void print(Network network) {
        printStream.println(network.toString());
    }

    public void print(DeviceConnection connection) {
        printStream.println(connection.toString());
    }

    public void print(Device device) {
        printStream.println(device.toString());
    }

    public Network readNetwork() {
        return updateNetwork(new Network());
    }

    public Network updateNetwork(Network network) {
        network.setName(readStringField("name"));
        network.setDescription(readStringField("description"));
        return network;
    }

    public DeviceConnection readConnection() {
        String status = readStringField("status");
        String type = readStringField("type");
        return new DeviceConnection(type, status);
    }


    public <T extends Model> T selectOf(List<T> models, String name) {
        while (true) {
            printStream.println("Enter id of " + name+":");
            models.forEach(printStream::println);
            var id = scanner.nextInt();
            scanner.nextLine();

            for(var model: models) {
                if(model.getId() == id)
                    return model;
            }

            printStream.println("Id was incorrect; try again");
        }
    }


    public Device readDevice() {
        String name = readStringField("name");
        String ipAddress = readStringField("ipAddress");
        String macAddress = readStringField("macAddress");
        String type = readStringField("type");
        String status = readStringField("status");
        return new Device(name, ipAddress, macAddress, type, status);
    }

    public String readStringField(String fieldName) {
        System.out.println("Enter " + fieldName + ":");
        return scanner.nextLine();
    }

    public void printError(String message) {
        printStream.println(message);
    }
}