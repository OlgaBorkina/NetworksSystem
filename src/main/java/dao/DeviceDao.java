package dao;

import model.Device;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DeviceDao {
    private final DatabaseManager dbManager = new DatabaseManager();


    public Device save(Device device) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into networks.device (name,ip_address,mac_address,type,status, network_id) values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, device.getName());
                preparedStatement.setString(2, device.getIpAddress());
                preparedStatement.setString(3, device.getMacAddress());
                preparedStatement.setString(4, device.getType());
                preparedStatement.setString(5, device.getStatus());
                preparedStatement.setLong(6, device.getNetworkId());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                device.setId(generatedKeys.getLong("id"));
                device.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return device;
            }
        }
    }
    public void remove(long id) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from networks.device where id =?")) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
            }
        }
    }

    public List<Device> getAllDevices() throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from networks.device");

                var devices = new ArrayList<Device>();
                while (result.next()) {
                    devices.add(toDeviceModel(result));
                }

                return devices;
            }
        }
    }

    private Device toDeviceModel(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var ip = resultSet.getString("ip_address");
        var mac = resultSet.getString("mac_address");
        var type = resultSet.getString("type");
        var status = resultSet.getString("status");
        var networkId = resultSet.getLong("network_id");
        var createdAt = resultSet.getTimestamp("created_at");
        return new Device(id, networkId, name, ip, mac, type, status, createdAt);
    }


}
