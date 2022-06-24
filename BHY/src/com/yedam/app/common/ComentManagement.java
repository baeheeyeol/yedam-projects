package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Coment;
import com.yedam.app.board.ComentDAO;

public class ComentManagement {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	ComentDAO cDAO = ComentDAO.getInstance();

	public ComentManagement(String memberId, Board board) {
		while (true) {
			menuPrint();

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 입력
				insertComent(memberId, board.getBoard_num());
			} else if (menuNo == 2) {
				// 수정
				updateComent(memberId, board.getBoard_num());
			} else if (menuNo == 3) {
				// 삭제
				// deleteComent();
			} else if (menuNo == 9) {
				exit();
				break;
				// 뒤로가기
			} else {
				showInputError();
			}
		}
	}

	private void Read(Board board, String memberId) {
		Board temp = bDAO.selectContent(board.getBoard_num());
		System.out.println(temp.getBoard_num() + ". " + temp.getBoard_title() + " : " + temp.getBoard_content() + " ");
		String str = "";
		List<Coment> list = cDAO.selectAll(board.getBoard_num());
		if (!list.isEmpty()) {
			for (Coment coment : list) {
				if (coment.getComentcomentNum() == 0) {
					str += coment.getComentNum() + ". " + coment.getComentContent() + " /" + coment.getMemberId();
				} else {
					str += "->" + coment.getComentcomentNum() + ". " + coment.getComentContent() + "/"
							+ coment.getMemberId();
				}
			}
		}
	}

	protected void updateComent(String memberId, int boardNum) {
		int comentNo = comentSelect();
		if (selectComent(comentNo)) {
			cDAO.update(boardNum, memberId, insertContent());
		}
	}

	protected void insertComent(String memberId, int boardNum) {

		if (bDAO.selectComent(boardNum) == 0) {
			cDAO.insert(boardNum, memberId, insertContent());
			bDAO.updateComent(boardNum);
		} else {
			int comentNo = comentSelect();
			if (selectComent(comentNo)) {
				if (!cDAO.selectComentComent(memberId, boardNum, comentNo)) {
					cDAO.insert(boardNum, memberId, insertContent());
				} else {
					cDAO.insertInsert(boardNum, memberId, comentNo, memberId);
				}
			}
		}

	}

	protected boolean selectComent(int comentNo) {
		List<Integer> list = cDAO.selectAllNum();
		for (int temp : list) {
			if (temp != comentNo) {
				return true;
			}
		}
		System.out.println("일치하는 댓글이 없습니다.");
		return false;
	}

	void ComentUpdate(String memberId, int boardNum) {
		cDAO.insert(boardNum, memberId, insertContent());
	}

	protected String insertContent() {
		System.out.println("내용을 입력하세요");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.println("1.댓글작성 2.댓글수정 9.뒤로가기");
	}

	protected int comentSelect() {
		int num = 0;

		try {
			System.out.println("댓글번호>");
			num = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return num;
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
