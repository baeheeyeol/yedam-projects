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

	public void BoardManagementRun(Board board, Member member) {

		while (true) {
			selectedBoard(board);
			menuPrint(board, member);
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				Read(board, member);
			} else if (menuNo == 2 && (member.getMemberId().equals(board.getMemberId()) || member.getRole() == 0)) {
				// 수정
				Update(board, member);
			} else if (menuNo == 3 && (member.getMemberId().equals(board.getMemberId()) || member.getRole() == 0)) {
				// 삭제
				Delete(board);
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

	private void selectedBoard(Board board) {
		System.out.println("====================");
		System.out.print("선택한 게시판 : ");
		System.out.println(board.getBoardNum() + ". " + board.getBoardTitle());
		System.out.println("====================");
	}

	private void Delete(Board board) {
		while (true) {
			Board list = bDAO.selectOne(board.getBoardNum());
			System.out.println("--" + list.getBoardNum() + ". " + list.getBoardTitle() + "--");
			System.out.println("해당 게시글을 삭제하시겠습니까");
			System.out.println("1.삭제 2.뒤로가기");
			int menuNo = menuSelect();
			if (menuNo == 1) {
				bDAO.delete(board.getBoardNum());
				break;
			} else if (menuNo == 2) {
				break;
			}
		}
	}

	private void Update(Board board, Member member) {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			String input = null;
			if (menuNo == 1) {
				// 제목 수정
				input = title();
				bDAO.updateTitle(input, board);
				Read(board, member);
			} else if (menuNo == 2) {
				// 내용 수정
				input = content();
				bDAO.updateContent(input, board);
				Read(board, member);
			} else if (menuNo == 9) {
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	// 읽기
	private void Read(Board board, Member member) {
		Board board1 = bDAO.selectOneAll(board.getBoardNum());
		System.out.println("=========================");
		System.out.println(board1.getBoardNum() + ". " + board1.getBoardTitle());
		System.out.println("=========================");
		System.out.println(board1.getBoardContent());
		System.out.println("=========================");
		if (board.getBoardInvisible() == 1 || member.getRole() == 0) {
			System.out.println("작성자 : " + board1.getMemberId());
		} else {
			System.out.println("작성자 : ****");
		}
		System.out.println("=========================");
		List<Comment> list = cDAO.selectAll(board1.getBoardNum());
		System.out.println("댓글");
		for (Comment comment : list) {
			if (comment.getCommentInvisible() == 1) {
				System.out.println(comment.getCommentNum() + ". 삭제된 댓글입니다.");
			} else {
				System.out.println(comment.getCommentNum() + "." + comment.getCommentContent() + " /id : "
						+ comment.getMemberId());
			}
		}
		
		System.out.println("=========================");
		System.out.println("1.댓글 2.신고하기 9.뒤로가기");
		System.out.print("번호>");
		int num = menuSelect();
		if (num == 1) {
			new ComentManagement(member, board);
			return;
		} else if (num == 2) {
			new ReportBoardManagement().insertReport(member, board1);
			return;
		} else if (num == 9) {
			exit();
			return;
		}
	}

	protected void menuPrint(Board board, Member member) {
		String sql = "1.읽기";
		Board temp = bDAO.selectOne(board.getBoardNum());
		if (temp.getMemberId().equals(member.getMemberId()) || member.getRole() == 0) {
			sql += " 2.수정 3.삭제";
		}
		sql += " 9.뒤로가기";
		System.out.println(sql);
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
