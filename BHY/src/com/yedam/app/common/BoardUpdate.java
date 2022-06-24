package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Coment;
import com.yedam.app.board.ComentDAO;

public class BoardUpdate {

	ComentDAO cDAO = ComentDAO.getInstance();
	BoardDAO bDAO = BoardDAO.getInstance();
	Scanner sc = new Scanner(System.in);

	public BoardUpdate(Board board, String memberId) {
		String Id = memberId;
		while (true) {

			menuPrint(board, Id);

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 읽기
				Read(board, memberId);
			} else if (menuNo == 2) {
				// 수정
				Update(board, memberId);
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

	private void Delete(Board board) {
		while (true) {
			Board list = bDAO.selectOne(board.getBoard_num());
			System.out.println("--" + list.getBoard_num() + ". " + list.getBoard_title() + "--");
			System.out.println("해당 게시글을 삭제하시겠습니까");
			System.out.println("1.삭제 2.뒤로가기");
			int menuNo = menuSelect();
			if (menuNo == 1) {
				bDAO.delete(board.getBoard_num());
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
		Board temp = bDAO.selectContent(board.getBoard_num());
		System.out.println(temp.getBoard_num() + ". " + temp.getBoard_title() + " : " + temp.getBoard_content() + " ");
		String str = "";
		List<Coment> list = cDAO.selectAll(board.getBoard_num());
		for (Coment coment : list) {
			if (coment.getComentcomentNum() == 0) {
				str += coment.getComentNum() + ". " + coment.getComentContent() + " /" + coment.getMemberId();
				System.out.println(str);
			} else {
				str += "->" + coment.getComentcomentNum() + ". " + coment.getComentContent() + "/"
						+ coment.getMemberId();
				System.out.println(str);
			}
		}
		new ComentManagement(memberId, board);
	}

	protected void menuPrint(Board board, String memberId) {
		String sql = "1.읽기";
		Board temp = bDAO.selectOne(board.getBoard_num());
		if (temp.getMember_id().equals(memberId)) {
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
