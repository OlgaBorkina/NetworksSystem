package dao;

import model.Model;
import model.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class NetworkDao {
    private final DatabaseManager dbManager = new DatabaseManager();


    public NetworkDao() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }


    public Network save(Network network) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
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
        try (Connection connection = dbManager.openConnection()) {
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

    public void remove(long id) throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("delete from networks.network where id =?")) {
                preparedStatement.setLong(1, id);
                preparedStatement.execute();
            }
        }
    }

    public List<Network> getAllNetworks() throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
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

    public List<Network> getEmptyNetworks() throws SQLException {
        try (Connection connection = dbManager.openConnection()) {
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
}