package edu.uci.ics.githubuserskills.model.db;

/**
 * @author shriti Model object for comments
 */
public class Comments {

	String author;
	String type;
	String comment;
	String time;

	public Comments() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
