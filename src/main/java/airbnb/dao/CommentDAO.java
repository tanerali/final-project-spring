package airbnb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import airbnb.manager.DBManager;
import airbnb.model.Comment;

public enum CommentDAO {
	INSTANCE;
	private Connection connection;
	private static final String DELETE_COMMENT = "DELETE FROM POST_COMMENTS WHERE ID = ?;";
	
	private static final String INSERT_COMMENT = 
			"INSERT INTO POST_COMMENTS(user_id,post_id,content,date) VALUES(?,?,?,?);";
	
	private static final String ALL_COMMENTS_FOR_POST = 
			"SELECT pc.ID, pc.user_id, CONCAT(u.first_name, \" \", u.last_name), pc.post_id, pc.content, pc.date " + 
			"FROM POST_COMMENTS pc " + 
			"JOIN USERS u " + 
			"ON pc.user_id = u.ID " + 
			"WHERE post_id =? ORDER BY ID;";
	
	private static final String ALL_COMMENTS_BY_USER = 
			"SELECT ID,user_id,post_id,content,date FROM POST_COMMENTS WHERE user_id =?;";

	private CommentDAO() {
		connection = DBManager.INSTANCE.getConnection();
	}

	public boolean deleteComment(int commentID) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(DELETE_COMMENT)) {
			ps.setInt(1, commentID);
			return ps.executeUpdate() > 0 ? true : false;
		}
		
	}

	public ArrayList<Comment> getCommentsByPostId(int postID) throws SQLException {
		try(PreparedStatement ps = connection.prepareStatement(ALL_COMMENTS_FOR_POST)) {
			ps.setInt(1, postID);
			ResultSet resultSet = ps.executeQuery();
			ArrayList<Comment> comments = new ArrayList<>();
			while (resultSet.next()) {
				Comment comment = new Comment(resultSet.getInt("ID"), 
											resultSet.getInt("user_id"),
											resultSet.getString(3),
											resultSet.getInt("post_id"), 
											resultSet.getString("content"),
											LocalDate.parse(resultSet.getString("date")));
				comments.add(comment);
			}
			return comments;
		}
	}

	public int addCommentToPost(Comment comment) throws SQLException {
		try(PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, comment.getUserID());
			ps.setInt(2, comment.getPostID());
			ps.setString(3, comment.getContent());
			ps.setObject(4, comment.getDate());
			ps.executeUpdate();

			int commentID = 0;
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				commentID = resultSet.getInt(1);
			}
			
//			return ps.executeUpdate() > 0 ? true : false;
			return commentID;
		}
	}
}
