package com.yedam.app.board;

public class Coment {
	private int boardNum;
	private String memberId;
	private int comentNum;
	private String comentContent;
	private int comentcomentNum;

	public int getComentcomentNum() {
		return comentcomentNum;
	}

	public void setComentcomentNum(int comentcomentNum) {
		this.comentcomentNum = comentcomentNum;
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

	public int getComentNum() {
		return comentNum;
	}

	public void setComentNum(int comentNum) {
		this.comentNum = comentNum;
	}

	public String getComentContent() {
		return comentContent;
	}

	public void setComentContent(String comentContent) {
		this.comentContent = comentContent;
	}

}
