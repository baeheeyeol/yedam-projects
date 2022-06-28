package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;
import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Comment;
import com.yedam.app.board.CommentDAO;
import com.yedam.app.member.Member;

public class BoardManagement {

	CommentDAO cDAO = CommentDAO.getInstance();
	BoardDAO bDAO = BoardDAO.getInstance();
	Scanner sc = new Scanner(System.in);

	public BoardManagement() {
	};

	public void BoardManagementRun(Board board, Member member) throws InterruptedException {

		while (true) {
			clear();
			selectedBoard(board);
			menuPrint(board, member);
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				read(board, member);
			} else if (menuNo == 2 && (member.getMemberId().equals(board.getMemberId()) || member.getRole() == 0)) {
				// 수정
				update(board, member);
			} else if (menuNo == 3 && (member.getMemberId().equals(board.getMemberId()) || member.getRole() == 0)) {
				// 삭제
				delete(board);
				break;
			} else if (menuNo == 4) {
				// 신고
				report(board, member);
			} else if (menuNo == 9) {
				// 뒤로가기
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	private void report(Board board, Member member) {
		new ReportBoardManagement().insertReport(member, board);
	}

	private void selectedBoard(Board board) {
		System.out.println("==================================================");
		System.out.print("선택한 게시판 : ");
		System.out.println("NO." + board.getBoardNum() + "\t" + board.getBoardTitle());
		System.out.println("==================================================");
	}

	private void delete(Board board) throws InterruptedException {
		while (true) {
			Board list = bDAO.selectOne(board.getBoardNum());
			System.out.println("--" + list.getBoardNum() + ". " + list.getBoardTitle() + "--");
			System.out.println("해당 게시글을 삭제하시겠습니까");
			System.out.println("1.삭제 \t2.뒤로가기");
			int menuNo = menuSelect();
			if (menuNo == 1) {
				bDAO.delete(board.getBoardNum());
				break;
			} else if (menuNo == 2) {
				break;
			}
		}
	}

	private void update(Board board, Member member) throws InterruptedException {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 제목 수정
				bDAO.updateTitle(title(), board);
				read(board, member);
				return;
			} else if (menuNo == 2) {
				// 내용 수정
				bDAO.updateContent(content(), board);
				read(board, member);
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
	protected void read(Board board, Member member) throws InterruptedException {
		clear();
		readBoard(board);
		readComment(board);
		new CommentManagement().CommentManagementRun(member, board);
	}

	protected void Read(Board board) {
		clear();
		readBoard(board);
		readComment(board);
	}

	protected void readBoard(Board board) {
		Board Board = bDAO.selectOneAll(board.getBoardNum());
		System.out.println("==================================================");
		System.out.println("No." + Board.getBoardNum() + " " + Board.getBoardTitle());
		System.out.println("==================================================");
		System.out.println(Board.getBoardContent());
		System.out.println("==================================================");
		System.out.println("작성자 : " + Board.getMemberId());
		System.out.println("==================================================");
	};

	protected void readComment(Board board) {
		List<Comment> list = cDAO.selectAll(board.getBoardNum());
		System.out.println("댓글");
		for (Comment Comment : list) {
			if (Comment.getCommentInvisible() == 1) {
				System.out.println("no" + Comment.getCommentNum() + " |" + " 삭제된 댓글입니다.");

			} else {
				System.out.println("no" + Comment.getCommentNum() + " | " + Comment.getCommentContent() + " /id : "
						+ Comment.getMemberId());
			}
		}
		System.out.println("==================================================");
	}

	protected void menuPrint(Board board, Member member) {
		String sql = "1.읽기";
		Board temp = bDAO.selectOne(board.getBoardNum());
		if (temp.getMemberId().equals(member.getMemberId()) || member.getRole() == 0) {
			sql += " 2.수정 3.삭제";
		}
		sql += " 4.신고하기 9.뒤로가기";
		System.out.println("--------------------------------------------------");
		System.out.println(sql);
		System.out.println("--------------------------------------------------");
		System.out.print("번호>");
	};

	protected String title() {
		System.out.printf("수정 제목>");
		return sc.nextLine();
	}

	protected String content() {
		System.out.printf("수정 내용>");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.println("--------------------------------------------------");
		System.out.printf("1.제목 수정 2.내용 수정 9. 뒤로가기");
		System.out.println("--------------------------------------------------");
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

	protected void exit() throws InterruptedException {
		System.out.println("뒤로가기");
		Thread.sleep(1000);
	}

	protected void showInputError() throws InterruptedException {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
		Thread.sleep(1000);
	}

	public void clear() {
		for (int i = 0; i < 57; ++i)
			System.out.println();
	}
}
