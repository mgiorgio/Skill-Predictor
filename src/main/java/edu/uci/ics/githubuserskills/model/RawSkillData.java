package edu.uci.ics.githubuserskills.model;

/**
 * <p>
 * Instances of this class will contain raw data produced by a person. The data
 * contained in these objects will need to be processed in order to extract
 * skills related to the person. Examples of these objects are:
 * </p>
 * <ul>
 * <li>Commit source code</li>
 * <li>Commit message</li>
 * <li>Pull Request message</li>
 * <li>Issue message</li>
 * </ul>
 * <p>
 * Basically, it is any piece of information that contains technical text
 * produced by a person.
 * </p>
 * 
 * 
 * @author mgiorgio
 * 
 */
public class RawSkillData {

	/**
	 * Who has produced this piece of information.
	 */
	private String author;

	/**
	 * When was this piece of information created.
	 */
	private long timestamp;

	/**
	 * What kind of information is this.
	 */
	private SkillDataType type;

	private String contents;

	public RawSkillData() {
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public SkillDataType getType() {
		return type;
	}

	public void setType(SkillDataType type) {
		this.type = type;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
