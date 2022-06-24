package com.yedam.app.common;

import java.util.List;

import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeBoardDAO;
import com.yedam.app.member.Member;

public class Management {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	NoticeBoardDAO nbDAO = NoticeBoardDAO.getInstance();

	public Management(Member member) {
		int role = member.getRole();
		while (true) {
			readNotice();
			readBoard();
			menuPrint(role);
			int menuNo = menuSelect();
			if (menuNo == 0 && role == 0) {
				// 공지관리
				new NoticeManagement();
			} else if (menuNo == 1) {
				// 작성
				boardWrtie(member);
			} else if (menuNo == 2) {
				// 선택
				 new BoardChoiceManagement(member);
			} else if (menuNo == 9) {
				// 프로그램 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void readNotice() {
		List<NoticeBoard> list = nbDAO.selelctAll();
		System.out.println("-=+-=+-=+공지+=-+=-+=-+=-");
		for (NoticeBoard Ntboard : list) {
			System.out.println(Ntboard.getNtboard_num() + ". " + Ntboard.getNtboard_title());
		}
		System.out.println("=*=*=*=*=*=*=*=*=*=*=*=*");
	}

	protected void readBoard() {
		List<Board> list = bDAO.selelctAll();
		System.out.println("*********게시판**********");
		for (Board board : list) {
			System.out.println(board.getBoard_num() + ". " + board.getBoard_title());
		}
		System.out.println("************************");
	}

	protected void menuPrint(int role) {
		String str = "";
		if (role == 0) {
			str += "0.공지";
		}
		str += " 1.작성 2.선택 9.종료";

		System.out.println("========================");
		System.out.println(str);
		System.out.println("========================");
	}

	protected void boardWrtie(Member member) {
		Board board = BoardinputTitle(member);
		bDAO.insert(board);
		int boardNum = bDAO.seq();
		Board board2 = BoardinputContent(boardNum);
		bDAO.insertcontent(board2);
	}

	protected Board BoardinputContent(int boardNum) {
		Board board = new Board();
		System.out.println("내용");
		board.setBoard_content(sc.nextLine());
		board.setBoard_num(boardNum);
		return board;
	}

	protected Board BoardinputTitle(Member member) {
		Board board = new Board();
		System.out.println("제목>");
		board.setBoard_title(sc.nextLine());
		board.setMember_id(member.getMemberId());
		return board;
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
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}
}
