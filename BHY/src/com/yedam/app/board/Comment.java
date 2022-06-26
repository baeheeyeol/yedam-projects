package com.yedam.app.board;

public class Comment {
	private int boardNum;
	private String memberId;
	private String commentContent;
	private int commentNum;
	private int commentNumParent;
	private int commentInvisible;

	public int getCommentInvisible() {
		return commentInvisible;
	}

	public void setCommentInvisible(int commentInvisible) {
		this.commentInvisible = commentInvisible;
	}

	public int getBoardNum() {
		return boardNum;
	}

	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getCommentNumParent() {
		return commentNumParent;
	}

	public void setCommentNumParent(int commentNumParent) {
		this.commentNumParent = commentNumParent;
	}

}
