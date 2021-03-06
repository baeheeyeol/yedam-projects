package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeBoardDAO;
import com.yedam.app.board.NoticeComment;
import com.yedam.app.board.NoticeCommentDAO;
import com.yedam.app.member.Member;

public class NoticeManagement {
	NoticeBoardDAO ntDAO = NoticeBoardDAO.getInstance();
	NoticeCommentDAO ntcDAO = NoticeCommentDAO.getInstance();
	Scanner sc = new Scanner(System.in);

	public NoticeManagement() {
	};

	public NoticeManagement(NoticeBoard noticeboard, Member member) throws InterruptedException {

		while (true) {
			clear();
			selectedBoard(noticeboard);
			menuPrint(noticeboard, member);

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				Read(noticeboard, member);
			} else if (menuNo == 2) {
				// 수정
				Update(noticeboard, member);
			} else if (menuNo == 3) {
				// 삭제
				Delete(noticeboard);
				break;
			} else if (menuNo == 9) {
				// 뒤로가기
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void selectedBoard(NoticeBoard noticeboard) {
		System.out.println("========================");
		System.out.print("선택한 게시판 : ");
		System.out.println(noticeboard.getBoardNum() + ". " + noticeboard.getBoardTitle());
		System.out.println("========================");
	}

	private void Delete(NoticeBoard noticeboard) throws InterruptedException {
		while (true) {
			NoticeBoard NoticeBoard = ntDAO.selectOneTitle(noticeboard.getBoardNum());
			System.out.println("--" + NoticeBoard.getBoardNum() + ". " + NoticeBoard.getBoardTitle() + "--");
			System.out.println("해당 게시글을 삭제하시겠습니까");
			System.out.println("1.삭제 2.뒤로가기");
			int menuNo = menuSelect();
			if (menuNo == 1) {
				ntDAO.delete(noticeboard.getBoardNum());
				break;
			} else if (menuNo == 2) {
				break;
			}
		}
	}

	private void Update(NoticeBoard noticeboard, Member member) throws InterruptedException {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 제목 수정
				ntDAO.updateTitle(inputTitle(), noticeboard);
				Read(noticeboard, member);
				return;
			} else if (menuNo == 2) {
				// 내용 수정
				ntDAO.updateContent(inputContent(), noticeboard);
				Read(noticeboard, member);
				return;
			} else if (menuNo == 9) {
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	// 읽기
	private void Read(NoticeBoard noticeboard, Member member) throws InterruptedException {
		clear();
		readBoard(noticeboard);
		readComment(noticeboard);
		new NoticeCommentManagement().NoticeCommentManagementRun(member, noticeboard);
	}

	protected void Read(NoticeBoard noticeboard) {
		clear();
		readBoard(noticeboard);
		readComment(noticeboard);
	}

	protected void readBoard(NoticeBoard noticeboard) {
		NoticeBoard noticeBoard = ntDAO.selectOneAll(noticeboard.getBoardNum());
		System.out.println("=========================");
		System.out.println(noticeBoard.getBoardNum() + ". " + noticeBoard.getBoardTitle());
		System.out.println("=========================");
		System.out.println(noticeBoard.getBoardContent());
		System.out.println("=========================");
		System.out.println("작성자 : " + noticeBoard.getMemberId());
		System.out.println("=========================");
	};

	protected void readComment(NoticeBoard noticeboard) {
		List<NoticeComment> list = ntcDAO.selectAll(noticeboard.getBoardNum());
		System.out.println("댓글");
		System.out.println("=========================");
		for (NoticeComment noticeComment : list) {
			if (noticeComment.getCommentInvisible() == 1) {
				System.out.println("삭제된 댓글입니다.");
			} else {
				System.out.println(noticeComment.getCommentNum() + "." + noticeComment.getCommentContent() + " /id : "
						+ noticeComment.getMemberId());
			}
		}
		System.out.println("=========================");
	}

	protected void menuPrint(NoticeBoard noticeboard, Member member) {
		String sql = "1.읽기";
		NoticeBoard temp = ntDAO.selectOneTitle(noticeboard.getBoardNum());
		if (temp.getMemberId().equals(member.getMemberId())) {
			sql += " 2.수정 3.삭제";
		}
		sql += " 9.뒤로가기";
		System.out.println(sql);
		System.out.print("번호>");
	};

	protected String inputTitle() {
		System.out.printf("수정 제목>");
		return sc.nextLine();
	}

	protected String inputContent() {
		System.out.printf("수정 내용>");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.printf("1.제목 수정 2.내용 수정 9. 뒤로가기");
	}

	protected int menuSelect() throws InterruptedException {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.err.println("숫자를 입력해주시기 바랍니다.");
			Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() {
		System.out.println("뒤로가기");
	}

	protected void showInputError() throws InterruptedException {
		System.err.println("메뉴에서 입력해주시기 바랍니다.");
		Thread.sleep(1000);
	}

	public void clear() {
		for (int i = 0; i < 57; ++i)
			System.out.println();
	}
}
