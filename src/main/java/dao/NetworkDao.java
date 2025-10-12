package dao;

import model.Device;
import model.DeviceConnection;
import model.Model;
import model.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkDao {

    public NetworkDao() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }


    public Network save(Network network) throws SQLException {
        try (Connection connection = openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into networks.network (name,description) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, network.getName());
                preparedStatement.setString(2, network.getDescription());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                network.setId(generatedKeys.getLong("id"));
                network.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return network;
            }
        }
    }

    public Network update(Network network) throws SQLException {
        try (Connection connection = openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("update networks.network set name=?,description=? where id=? returning id, name, description, created_at")) {
                preparedStatement.setString(1, network.getName());
                preparedStatement.setString(2, network.getDescription());
                preparedStatement.setLong(3, network.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                return toModel(resultSet, network);
            }
        }
    }


    public Device save(Device device) throws SQLException {
        try (Connection connection = openConnection()) {
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


    public DeviceConnection save(DeviceConnection deviceConnection) throws SQLException {
        try (Connection connection = openConnection()) {
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


    public List<Network> getAllNetworks() throws SQLException {
        try (Connection connection = openConnection()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from networks.network");

                var networks = new ArrayList<Network>();
                while (result.next()) {
                    networks.add(toModel(result));
                }

                return networks;
            }
        }
    }

    public List<Network> getEmptyNetworks() throws SQLException{
        try (Connection connection = openConnection()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from networks.network ns left join networks.device devices on ns.id = devices.network_id where devices.id IS NULL");

                var networks = new ArrayList<Network>();
                while (result.next()) {
                    networks.add(toModel(result));
                }

                return networks;
            }
        }
    }


    public List<Device> getAllDevices() throws SQLException {
        try (Connection connection = openConnection()) {
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

    public void remove(Model model, String table) throws SQLException {
        try (Connection connection = openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from networks."+table+" where id =?")) {
                preparedStatement.setLong(1, model.getId());
                preparedStatement.execute();
            }
        }
    }


    public List<DeviceConnection> getAllConnections() throws SQLException {
        try (Connection connection = openConnection()) {
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

    private Network toModel(ResultSet resultSet) throws SQLException {
        return toModel(resultSet, new Network());
    }

    private Network toModel(ResultSet resultSet, Network network) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var description = resultSet.getString("description");
        var createdAt = resultSet.getTimestamp("created_at");
        network.setId(id);
        network.setName(name);
        network.setDescription(description);
        network.setCreatedAt(createdAt);
        return network;
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

    private DeviceConnection toDeviceConnectionModel(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var type = resultSet.getString("type");
        var status = resultSet.getString("status");
        var deviceFromId = resultSet.getLong("device_from_id");
        var deviceToId = resultSet.getLong("device_to_id");
        var createdAt = resultSet.getTimestamp("created_at");
        return new DeviceConnection(id, deviceFromId, deviceToId, type, status, createdAt);
    }


    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/networks_db", "admin", "admin");
    }

}