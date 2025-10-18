package dao;

import model.DeviceConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDao {
    private final DatabaseManager dbManager = new DatabaseManager();

    public DeviceConnection save(DeviceConnection deviceConnection) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into networks.connection (device_from_id, device_to_id,type,status) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, deviceConnection.getDeviceFromId());
                preparedStatement.setLong(2, deviceConnection.getDeviceToId());
                preparedStatement.setString(3, deviceConnection.getType());
                preparedStatement.setString(4, deviceConnection.getStatus());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                deviceConnection.setId(generatedKeys.getLong("id"));
                deviceConnection.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return deviceConnection;
            }
        }
    }
    public void remove(long id) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from networks.connection where id =?")) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
            }
        }
    }


    public List<DeviceConnection> getAllConnections() throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from networks.connection");

                var devices = new ArrayList<DeviceConnection>();
                while (result.next()) {
                    devices.add(toDeviceConnectionModel(result));
                }

                return devices;
            }
        }
    }

    private DeviceConnection toDeviceConnectionModel(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var type = resultSet.getString("type");
        var status = resultSet.getString("status");
        var deviceFromId = resultSet.getLong("device_from_id");
        var deviceToId = resultSet.getLong("device_to_id");
        var createdAt = resultSet.getTimestamp("created_at");
        return new DeviceConnection(id, deviceFromId, deviceToId, type, status, createdAt);
    }

}
