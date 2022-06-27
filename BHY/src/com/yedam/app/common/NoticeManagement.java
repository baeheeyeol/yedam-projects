package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Comment;
import com.yedam.app.board.CommentDAO;
import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeBoardDAO;
import com.yedam.app.board.NoticeComment;
import com.yedam.app.board.NoticeCommentDAO;
import com.yedam.app.member.Member;

public class NoticeManagement  {
	NoticeBoardDAO ntDAO = NoticeBoardDAO.getInstance();
	NoticeCommentDAO ntcDAO =NoticeCommentDAO.getInstance();
	Scanner sc = new Scanner(System.in);

	public NoticeManagement(NoticeBoard noticeboard, Member member) {
		
		while (true) {
			selectedBoard(noticeboard);
			menuPrint(noticeboard, member.getMemberId());

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				Read(noticeboard, member.getMemberId());
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

	private void selectedBoard(NoticeBoard noticeboard) {
		System.out.print("선택한 게시판 : ");
		System.out.println(noticeboard.getBoardNum() + ". " + noticeboard.getBoardTitle());
	}

	private void Delete(NoticeBoard noticeboard) {
		while (true) {
			NoticeBoard NoticeBoard  = ntDAO.selectOneTitle(noticeboard.getBoardNum());
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

	private void Update(NoticeBoard noticeboard, Member member) {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 제목 수정
				ntDAO.updateTitle(inputTitle(), noticeboard);
				Read(noticeboard, member.getMemberId());
			} else if (menuNo == 2) {
				// 내용 수정
				ntDAO.updateContent(inputContent(), noticeboard);
				Read(noticeboard, member.getMemberId());
			} else if (menuNo == 9) {
				exit();
				break;
			}
		}
	}

	// 읽기
	private void Read(NoticeBoard noticeboard, String memberId) {
		NoticeBoard noticeBoard = ntDAO.selectOneAll(noticeboard.getBoardNum());
		System.out.println("=========================");
		System.out.println(noticeBoard.getBoardNum() + ". " + noticeBoard.getBoardTitle());
		System.out.println("=========================");
		System.out.println(noticeBoard.getBoardContent());
		System.out.println("=========================");
		System.out.println("작성자 : "+ noticeBoard.getMemberId());
		System.out.println("=========================");
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
		new NoticeCommentManagement(memberId, noticeboard);
	}

	protected void menuPrint(NoticeBoard noticeboard, String memberId) {
		String sql = "1.읽기";
		NoticeBoard temp = ntDAO.selectOneTitle(noticeboard.getBoardNum());
		if (temp.getMemberId().equals(memberId)) {
			sql += " 2.수정 3.삭제";
		}
		sql += " 9.뒤로가기";
		System.out.println(sql);
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

	protected int menuSelect() {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return menu;
	}

	protected void exit() {
		System.out.println("뒤로가기");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}

}
