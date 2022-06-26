package com.yedam.app.board;

public class NoticeBoard{

	private int boardNum;
	private String boardTitle;
	private String boardContent;
	private String memberId;
	private int boardInvisible;
	public int getBoardInvisible() {
		return boardInvisible;
	}
	public void setBoardInvisible(int boardInvisible) {
		this.boardInvisible = boardInvisible;
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getBoardContent() {
		return boardContent;
	}
	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
