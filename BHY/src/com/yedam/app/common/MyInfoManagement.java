package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeBoardDAO;
import com.yedam.app.member.Member;

public class MyInfoManagement extends Management {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	NoticeBoardDAO ntDAO = NoticeBoardDAO.getInstance();

	public MyInfoManagement(Member member) {
		List<Board> list = bDAO.selectAll(member.getMemberId());
		while (true) {
			menuPrint(member);
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 정보
				info(member);
			} else if (menuNo == 2) {
				// 게시판
				myBoard(member);
			} else if (menuNo == 3 && member.getRole() == 0) {
				// 신고내역
				report(member);
			} else if (menuNo == 9) {
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void report(Member member) {
		new ReportBoardManagement().ReportBoardManagementRun(member);
		
	}

	protected void info(Member member) {
		System.out.println("==================");
		System.out.println("ID : " + member.getMemberId());
		System.out.println("PASSWORD : " + member.getMemberPwd());
		if (member.getRole() == 0) {
			List<NoticeBoard> ntlist = ntDAO.selelctAll();
			List<Board> list = bDAO.selectAll1();
			System.out.println("공지 게시글 수 : " + ntlist.size());
			System.out.println("게시글 수 : " + list.size());

		} else {
			List<Board> list = bDAO.selectAll(member.getMemberId());
			System.out.println("게시글 수 : " + list.size());
		}
		System.out.println("==================");

	}

	protected void myBoard(Member member) {
		while (true) {
			if (member.getRole() == 0) {
				readNotice();
				readBoard(member);
				boardChoiceType(member);
				return;
			} else {
				selectBoard(member);
				return;
			}
		}
	}

	protected void menuPrint(Member member) {
		String str = "1.정보 2.게시판";
		if (member.getRole() == 0) {
			str += " 3.신고내역";
		}
		str += " 9.뒤로가기";
		System.out.println(str);
		System.out.printf("번호>");
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