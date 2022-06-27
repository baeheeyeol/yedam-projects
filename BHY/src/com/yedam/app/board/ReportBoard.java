package com.yedam.app.board;

public class ReportBoard {
	private String memberId;
	private int boardNum;
	private int reportBoardNum;
	private String content;
	private String boardTitle;

	public String getBoardTitle() {
		return boardTitle;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getBoardNum() {
		return boardNum;
	}

	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}

	public int getReportBoardNum() {
		return reportBoardNum;
	}

	public void setReportBoardNum(int reportBoardNum) {
		this.reportBoardNum = reportBoardNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
