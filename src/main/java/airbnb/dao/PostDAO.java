package airbnb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.manager.DBManager;
import airbnb.model.Post;
import airbnb.model.User;

public enum PostDAO {
	INSTANCE;
	// Connection variable
	private Connection connection;

	// Const. SQL statements
	private static final String insertPost = "INSERT INTO POSTS(type_id,title,price,host_id,date_of_posting, city_id, description) "
			+ "VALUES(?,?,?,?,?, (SELECT ID FROM CITIES WHERE city_name=?), ?);";
	private static final String deletePost = "DELETE FROM POSTS where ID=?;";

	private static final String getAllPosts = "SELECT p.ID, p.type_id, p.title, p.description, p.host_id, p.price, p.date_of_posting, co.country_name, ci.city_name "
			+ "FROM POSTS p " + "JOIN CITIES ci " + "ON p.city_id = ci.ID " + "JOIN COUNTRIES co "
			+ "ON ci.country_code = co.code";

	private PostDAO() {
		connection = DBManager.INSTANCE.getConnection();
	}

	public List<Post> getAllPosts() throws SQLException, InvalidPostDataExcepetion {
		List<Post> posts = new ArrayList<Post>();
		Statement st = connection.createStatement();
		try {
			ResultSet result = st.executeQuery(getAllPosts);
			posts = this.getResult(result);
		} finally {
			st.close();
		}
		return posts;
	}

	public int insertPost(Post newPost) throws InvalidPostDataExcepetion, SQLException {
		PreparedStatement statement = connection.prepareStatement(insertPost, Statement.RETURN_GENERATED_KEYS);
		int postId = 0;
		try {
			statement.setInt(1, newPost.getTypeLikeID());
			statement.setString(2, newPost.getTitle());
			statement.setInt(3, newPost.getPrice());
			statement.setInt(4, newPost.getHostID());
			statement.setDate(5, Date.valueOf(newPost.getDateOfPosting()));
			statement.setString(6, newPost.getCity());
			statement.setString(7, newPost.getDescription());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				postId = rs.getInt(1);
			}
		} finally {
			statement.close();
		}
		return postId;
	}

	private List<Post> getResult(ResultSet result) throws InvalidPostDataExcepetion, SQLException {
		List<Post> posts = new ArrayList<Post>();
		while (result.next()) {
			Post newPost = new Post(result.getInt("ID"), result.getString("title"), result.getString("description"),
					result.getInt("price"), result.getDate("date_of_posting").toLocalDate(),
					Post.Type.getType(result.getInt("type_id")), result.getInt("host_id"),
					result.getString("country_name"), result.getString("city_name"));
			posts.add(newPost);
		}
		return posts;
	}

	public void insertImageToPost(String path, int postID) throws SQLException {

		String insertImage = "INSERT INTO POSTS_PHOTOS(post_id,photo) " + "VALUES(?,?);";
		PreparedStatement statement = connection.prepareStatement(insertImage);
		statement.setInt(1, postID);
		statement.setString(2, path);
		statement.executeUpdate();

	}

	public void removePost(int id) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(deletePost);
		try {
			ps.setInt(1, id);
			ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	public String getThumbnailPath(int postID) throws SQLException {
		String sql = "SELECT photo " + "FROM POSTS_PHOTOS " + "WHERE post_id=? LIMIT 1";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, postID);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("photo");
			}
			return null;
		}
	}

	public boolean editPost(Post post) throws SQLException {
		String sql = "UPDATE POSTS SET type_id=?, title=?, description=?, price=?  WHERE ID = ?;";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, post.getTypeLikeID());
			ps.setString(2, post.getTitle());
			ps.setString(3, post.getDescription());
			ps.setInt(4, post.getPrice());
			ps.setInt(5, post.getPostID());
			return ps.executeUpdate() > 0 ? true : false;
		}
	}

	public double getPostRating(int postID) throws SQLException {
		String sql = "SELECT ROUND(AVG(rating), 1) FROM POSTS_RATING " + "WHERE post_id = ?;";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, postID);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getDouble(1);
			} else {
				return 0;
			}
		}
	}

	public ArrayList<String> getAllPhotos(int postID) throws SQLException {
		ArrayList<String> allPhotos = new ArrayList<>();
		String sql = "SELECT photo " + "FROM POSTS_PHOTOS " + "WHERE post_id=?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, postID);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				allPhotos.add(resultSet.getString("photo"));
			}
			// Remove first photo !
			if (allPhotos.size() > 1) {
				allPhotos.remove(0);
			}
			return allPhotos;
		}
	}
}
