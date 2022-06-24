package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.member.Member;

public class BoardChoiceManagement {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();

	public BoardChoiceManagement(Member member) {
		String memberId = member.getMemberId();
		while (true) {

			menuPrint(memberId);
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 번호로 선택
				choiceBoardNum(memberId);
			} else if (menuNo == 2) {
				// 제목으로 선택
				choiceBoardTitle(memberId);
			} else if (menuNo == 3) {
				// 작성한글 선택
				choiceBoardMy(memberId);
			} else if (menuNo == 9) {
				// 뒤로가기
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void choiceBoardMy(String memberId) {
		System.out.println("내가 작성한게시글");
		List<Board> list = bDAO.selectAll(memberId);
		for (Board board : list) {
			System.out.println(board.getBoard_num() + ". " + board.getBoard_title());
		}
		System.out.println("================");
		choiceWay(memberId);
	}

	protected void choiceWay(String memberId) {
		menuPrint();
		int menuNo = menuSelect();
		while (true) {
			if (menuNo == 1) {
				// 번호로 선택
				choiceBoardNum(memberId);
			} else if (menuNo == 2) {
				// 제목으로 선택
				choiceBoardTitle(memberId);
			} else if (menuNo == 9) {
				// 뒤로가기
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void choiceBoardNum(String memberId) {
		System.out.printf("번호>");
		int num = inputNum();
		Board board = bDAO.selectOne(num);
		if (checkTitle(num)) {
			System.out.println("선택한 게시물 : " + board.getBoard_num() + ". " + board.getBoard_title());
			 new BoardUpdate(board,memberId);
		} else {
			System.out.println("입력한 번호의 게시판이 없습니다.");
		}
	}

	protected void choiceBoardTitle(String memberId) {
		System.out.printf("제목>");
		String title = inputTitle();
		Board board = bDAO.selectOne(title);
		if (checkTitle(title)) {
			System.out.println("선택한 게시물 : " + board.getBoard_num() + ". " + board.getBoard_title());
			 new BoardUpdate(board,memberId);
		} else {
			System.out.println("입력한 제목의 게시판이 없습니다.");
		}
	}

	private boolean checkTitle(String boardTitle) {

		List<String> list = bDAO.selectBoardTitle();
		for (String title : list) {
			if (boardTitle.equals(title)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkTitle(int boardNum) {

		List<Integer> list = bDAO.selectBoardNum();
		for (int num : list) {
			if (num == boardNum) {
				return true;
			}
		}
		return false;
	}

	protected void menuPrint() {
		System.out.println("선택방법>");
		System.out.println("1.번호 2.제목 9.뒤로가기");
	}

	protected void menuPrint(String memberId) {
		System.out.println("선택방법>");
		System.out.println("1.번호 2.제목 3.작성한글 9.뒤로가기");
	};

	private String inputTitle() {
		return sc.nextLine();
	}

	private int inputNum() {
		return Integer.parseInt(sc.nextLine());
	}

	protected String inputmenu() {
		return sc.nextLine();
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
