package edu.uci.ics.githubuserskills.model.db;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author shriti Model objects for Commits
 */
public class Commit {

	private String author;
	private String commit_message;
	private String comments_url;
	private String time;

	private Collection<PatchedFile> patches;

	public Commit() {
		patches = new LinkedList<PatchedFile>();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCommit_message() {
		return commit_message;
	}

	public void setCommit_message(String commit_message) {
		this.commit_message = commit_message;
	}

	public String getComments_url() {
		return comments_url;
	}

	public void setComments_url(String comments_url) {
		this.comments_url = comments_url;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Collection<PatchedFile> getPatches() {
		return patches;
	}

	public void setPatches(Collection<PatchedFile> patches) {
		this.patches = patches;
	}
}