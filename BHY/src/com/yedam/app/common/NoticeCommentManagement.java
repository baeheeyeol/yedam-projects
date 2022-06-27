package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeComment;
import com.yedam.app.board.NoticeCommentDAO;
import com.yedam.app.member.Member;

public class NoticeCommentManagement extends NoticeManagement {
	public NoticeCommentManagement(NoticeBoard noticeboard, Member member) throws InterruptedException {
		super(noticeboard, member);
		// TODO Auto-generated constructor stub
	}

	public NoticeCommentManagement() {
	};

	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	NoticeCommentDAO ntDAO = NoticeCommentDAO.getInstance();

	public void NoticeCommentManagementRun(Member member, NoticeBoard noticeboard) throws InterruptedException {
		while (true) {
			clear();
			Read(noticeboard);
			menuPrint();

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 입력
				insertComent(member, noticeboard);
			} else if (menuNo == 2) {
				// 수정
				updateComent(member,noticeboard);
			} else if (menuNo == 3) {
				// 삭제
				deleteComent(member,noticeboard);
			} else if (menuNo == 9) {
				exit();
				break;
				// 뒤로가기
			} else {
				showInputError();
			}
		}
	}

	void deleteComent(Member member,NoticeBoard noticeboard) throws InterruptedException {
		clear();
		readComment(noticeboard);
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(member.getMemberId())) {
				if (checkComentParent(commentNum)) {
					ntDAO.update(commentNum);
				} else {
					ntDAO.delete(commentNum);
				}
			}
		}
	}

	boolean checkComentParent(int commentNum) {
		List<NoticeComment> list = ntDAO.selectAll();
		for (NoticeComment noticeComment : list) {
			if (commentNum == noticeComment.getCommentNumParent()) {
				return true;
			}
		}
		return false;
	}

	protected void updateComent(Member member,NoticeBoard noticeboard) throws InterruptedException {
		clear();
		readComment(noticeboard);
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(member.getMemberId())) {
				ntDAO.update(commentNum, inputContent());
			}
		}
	}

	// 등록
	protected void insertComent(Member member, NoticeBoard noticeBoard) throws InterruptedException {
		clear();
		Read(noticeBoard);

		System.out.println("1.댓글 작성 2.대댓글 작성");
		System.out.print("번호>");
		int num = menuSelect();
		if (num == 1) {
			// 댓글 작성
			ntDAO.insert(noticeBoard.getBoardNum(), member.getMemberId(), inputContent(noticeBoard));
		} else if (num == 2) {
			// 대댓글 작성
			int commentNum = selectComment();
			if (checkComentNum(commentNum)) {
				ntDAO.insertToInsert(noticeBoard.getBoardNum(), member.getMemberId(), inputContent(), commentNum);
			}
		}
	}

	int selectComment() throws InterruptedException {
		int num = 0;
		try {
			System.out.print("댓글번호>");
			num = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
			Thread.sleep(1000);
		}
		return num;
	}

	protected String inputContent(NoticeBoard noticeBoard) {
		clear();
		Read(noticeBoard);
		System.out.print("내용입력>");
		return sc.nextLine();
	}

	// 본인 댓글 여부 확인
	boolean checkMemberId(String memberId) throws InterruptedException {
		List<NoticeComment> list = ntDAO.selectAll();
		for (NoticeComment noticeComment : list) {
			if (noticeComment.getMemberId().equals(memberId)) {
				return true;
			}
		}
		System.out.println("본인의 댓글을 선택해주세요.");
		Thread.sleep(1000);
		return false;
	}

	// 댓글 존재 확인
	boolean checkComentNum(int commentNum) throws InterruptedException {
		List<NoticeComment> list = ntDAO.selectAll();
		for (NoticeComment noticeComment : list) {
			if (noticeComment.getCommentNum() == commentNum) {
				return true;
			}
		}
		System.out.println("선택한 번호의 댓글이 존재하지 않습니다.");
		Thread.sleep(1000);
		return false;
	}

	void ComentUpdate(String memberId, int boardNum) {
		ntDAO.insert(boardNum, memberId, insertContent());
	}

	protected String insertContent() {
		System.out.println("내용을 입력하세요");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.println("1.댓글작성 2.댓글수정 3.댓글삭제 9.뒤로가기");
		System.out.print("번호>");
	}

	protected int menuSelect() throws InterruptedException {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
			Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() {
		System.out.println("뒤로가기");
	}

	protected void showInputError() throws InterruptedException {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
		Thread.sleep(1000);
	}

	public void clear() {
		for (int i = 0; i < 45; ++i)
			System.out.println();
	}
}
