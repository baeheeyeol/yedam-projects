package com.yedam.app.board;

public class Board {

	private int boardNum;
	private String boardTitle;
	private String boardContent;
	private String memberId;
	private int boardInvisible;// 0이면 아이디가 보인다. 1이면 다른유저는 아이디가 보이지 않는다.
	private int rowNum;
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
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
