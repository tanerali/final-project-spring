package airbnb.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import airbnb.dao.PostDAO;
import airbnb.exceptions.InvalidPostDataExcepetion;
import airbnb.model.Post;

public enum PostManager {
	INSTANCE;

	// userID -> list of posts
	private Map<Integer, List<Post>> postsByUsers;

	// postID -> Post
	private Map<Integer, Post> postsByID;

	private PostManager() {
		postsByUsers = new ConcurrentHashMap<>();
		postsByID = new ConcurrentHashMap<>();

		// Load(cache) all posts
		try {
			for (Post p : PostDAO.INSTANCE.getAllPosts()) {
				if (!postsByUsers.containsKey(p.getHostID())) {
					postsByUsers.put(p.getHostID(), new ArrayList<>());
				}
				postsByUsers.get(p.getHostID()).add(p);
				postsByID.put(p.getPostID(), p);
			}
		} catch (SQLException | InvalidPostDataExcepetion e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, List<Post>> getPostsByUsers() {
		return Collections.unmodifiableMap(postsByUsers);
	}

	public Map<Integer, Post> getPostsByID() {
		return Collections.unmodifiableMap(postsByID);
	}
	
	public Post removePostFromCache(int postID) {
		Post postToRemove = postsByID.remove(postID);
		postsByUsers.get(postToRemove.getHostID()).remove(postToRemove);
		return postToRemove;
	}

	public void addPostToCache(Post post) {
		postsByID.put(post.getPostID(), post);
		if (!postsByUsers.containsKey(post.getHostID())) {
			postsByUsers.put(post.getHostID(), new ArrayList<>());
		}
		postsByUsers.get(post.getHostID()).add(post);
	}
	
	public void editPostInCache(Post post) {
		postsByID.put(post.getPostID(), post);
		List<Post> postsByThisUser = postsByUsers.get(post.getHostID());
		for (ListIterator<Post> iterator = postsByThisUser.listIterator(); iterator.hasNext();) {
			Post thisUsersPost = (Post) iterator.next();
			if (thisUsersPost.getPostID() == post.getPostID()) {
				iterator.remove();
			}
		}
		postsByThisUser.add(post);
	}

	public List<Post> searchPost(String search) {
		ArrayList<Post> posts = new ArrayList<Post>();
		for (int id : postsByID.keySet()) {
			if (postsByID.get(id).getTitle().toLowerCase().contains(search.toLowerCase())) {
				posts.add(postsByID.get(id));
			}
		}
		return posts;
	}

	public List<Post> getAllPosts() throws SQLException, InvalidPostDataExcepetion {
		return PostDAO.INSTANCE.getAllPosts();
	}

	public String getThumbnail(int postID) throws SQLException {
		return PostDAO.INSTANCE.getThumbnailPath(postID);
	}

	public double getPostRating(int postID) throws SQLException {
		return PostDAO.INSTANCE.getPostRating(postID);
	}

	public ArrayList<String> getAllPhotos(int postID) throws SQLException {
		return PostDAO.INSTANCE.getAllPhotos(postID);

	}

	public boolean editPost(Post post) throws SQLException {
		boolean edited = PostDAO.INSTANCE.editPost(post);
		if (edited) {
			editPostInCache(post);
			return true;
		}
		return false;
	}

	public int insertPost(Post newPost) throws InvalidPostDataExcepetion, SQLException {
		int postID = PostDAO.INSTANCE.insertPost(newPost);
		newPost.setPostID(postID);
		addPostToCache(newPost);
		return postID;
	}
	
	public Post removePost(int postID) throws SQLException {

		PostDAO.INSTANCE.removePost(postID);
		return removePostFromCache(postID);
	}
}
