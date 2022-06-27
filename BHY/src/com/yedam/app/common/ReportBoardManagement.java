package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.ReportBoard;
import com.yedam.app.board.ReportBoardDAO;
import com.yedam.app.member.Member;

public class ReportBoardManagement extends Management {
	Scanner sc = new Scanner(System.in);
	ReportBoardDAO rDAO = ReportBoardDAO.getInstance();

	public ReportBoardManagement() {

	};

	public void ReportBoardManagementRun(Member member) {
		readReportBoard();
		while (true) {
			menuPrint();
			int num = menuSelect();
			if (num == 1) {
				readReportBoard();
			} else if (num == 2) {
				// 선택
				
			} else if (num == 9) {
				exit();
				return;
			} else {
				showInputError();
			}
		}
	}

	protected void readReportBoard() {
		List<ReportBoard> list = rDAO.selectAll();
		for (ReportBoard reportBoard : list) {
			System.out.println("===========신고 내역============");
			System.out.println(reportBoard.getReportBoardNum() + ". " + "게시판번호 : " + reportBoard.getBoardNum() + " "
					+ reportBoard.getBoardTitle());
			System.out.println("=============================");
		}
	}

	protected void insertReport(Member member, Board board) {
		rDAO.insert(member, board, inputContent(member));
		return;
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

	protected void menuPrint() {
		System.out.println("==============");
		System.out.println("1.선택 9.뒤로가기");
		System.out.println("==============");
	}

//	protected void menuPrint(Member member) {
//		String str = "";
//		if (member.getRole() == 1) {
//			str += "1.신고하기";
//		} else if (member.getRole() == 0) {
//			str += "2.내역보기";
//		}
//		str += "9.뒤로가기";
//		System.out.println(str);
//	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}
}
