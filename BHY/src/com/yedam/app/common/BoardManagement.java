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

	public BoardManagement(Board board, Member member) {
		
		while (true) {
			selectedBoard(board);
			menuPrint(board, member.getMemberId());

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				Read(board, member.getMemberId());
			} else if (menuNo == 2) {
				// 수정
				Update(board, member.getMemberId());
			} else if (menuNo == 3) {
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
		System.out.println(board.getBoardNum() + ". " + board.getBoardTitle());
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

	private void Update(Board board, String memberId) {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			String input = null;
			if (menuNo == 1) {
				// 제목 수정
				input = title();
				bDAO.updateTitle(input, board);
				Read(board, memberId);
			} else if (menuNo == 2) {
				// 내용 수정
				input = content();
				bDAO.updateContent(input, board);
				Read(board, memberId);
			} else if (menuNo == 9) {
				exit();
				break;
			}
		}
	}

	// 읽기
	private void Read(Board board, String memberId) {
		Board temp = bDAO.selectContent(board.getBoardNum());
		System.out.println(temp.getBoardNum() + ". " + temp.getBoardTitle() + " : " + temp.getBoardContent() + " ");

		List<Comment> list = cDAO.selectAll(board.getBoardNum());
		System.out.println("댓글");
		for (Comment comment : list) {
			if (comment.getCommentInvisible() == 1) {
				System.out.println("삭제된 댓글입니다.");
			} else {
				System.out.println(comment.getCommentNum() + "." + comment.getCommentContent() + " /id : "
						+ comment.getMemberId());
			}
		}
		new ComentManagement(memberId, board);
	}

	protected void menuPrint(Board board, String memberId) {
		String sql = "1.읽기";
		Board temp = bDAO.selectOne(board.getBoardNum());
		if (temp.getMemberId().equals(memberId)) {
			sql += " 2.수정 3.삭제";
		}
		sql += " 9.뒤로가기";
		System.out.println(sql);
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
