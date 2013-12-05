package edu.uci.ics.githubuserskills.model.db;



/**
 * @author shriti
 * Model objects for Commits
 */
public class Commit{

	Author author;
	String commit_message;
	String html_url;
	String comments_url;
	String time;

	String filename;
	int changes;
	int additions;
	int deletions;
	String raw_url;
	String contents_url;
	String patch;

	public Commit(){

	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getCommit_message() {
		return commit_message;
	}

	public void setCommit_message(String commit_message) {
		this.commit_message = commit_message;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	public String getComments_url() {
		return comments_url;
	}

	public void setComments_url(String comments_url) {
		this.comments_url = comments_url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getChanges() {
		return changes;
	}

	public void setChanges(int changes) {
		this.changes = changes;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public String getRaw_url() {
		return raw_url;
	}

	public void setRaw_url(String raw_url) {
		this.raw_url = raw_url;
	}

	public String getContents_url() {
		return contents_url;
	}

	public void setContents_url(String contents_url) {
		this.contents_url = contents_url;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}




}