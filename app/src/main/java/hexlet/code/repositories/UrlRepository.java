package hexlet.code.repositories;

import hexlet.code.models.Url;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


public class UrlRepository extends BaseRepository {

//    public static void save(Url url) throws SQLException {
//        var sql = "INSERT INTO urls (name) VALUES (?)";
//        try (var conn = dataSource.getConnection();
//             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            preparedStatement.setString(1, url.getName());
//            preparedStatement.executeUpdate();
//            var generatedKeys = preparedStatement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                url.setId(generatedKeys.getLong(1));
//            } else {
//                throw new SQLException("DB have not returned an id after saving an entity");
//            }
//        }
//    }
    public static void save(Url url) throws SQLException {

        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

//        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO table (timestamp_column) VALUES (?)");

        var conn = dataSource.getConnection();
        try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(name, createdAt);

                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Url findByName(String urlName) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlName);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var url = new Url(urlName);
                var createdAt = resultSet.getTimestamp("created_at");

                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
            return null;
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var createdAt = resultSet.getTimestamp("created_at");
                var name = resultSet.getString("name");
                var url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }
}