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

	public void ReportBoardManagementRun(Member member) throws InterruptedException {
		while (true) {
			clear();
			readReportBoardList();
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 선택
				selectReportBoard();
			} else if (menuNo == 9) {
				// 종료
				exit();
				return;
			} else {
				showInputError();
			}
		}
	}

	protected void deleteBoard(int reportBoardNum) throws InterruptedException {
		while (true) {
			menuDeletePrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 선택
				ReportBoard reportBoard = rDAO.selectOne(reportBoardNum);
				rDAO.delete(reportBoard.getBoardNum(),reportBoardNum);
				break;
			} else if (menuNo == 9) {
				// 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void selectReportBoard() throws InterruptedException {
		clear();
		readReportBoardList();
		int reportBoardNum = menuSelect();
		if (existReportBoardNum(reportBoardNum)) {
			readReportBoard(reportBoardNum);
		}
		deleteBoard(reportBoardNum);
	}
	protected boolean existReportBoardNum(int reportBoardNum) throws InterruptedException {
		List<ReportBoard> list = rDAO.selectAll();
		for (ReportBoard reportBoard : list) {
			if (reportBoard.getReportBoardNum() == reportBoardNum) {
				return true;
			}
		}
		System.err.println("--------------------------------------------------");
		System.err.println("입력한 번호는 없는 번호입니다.");
		System.err.println("--------------------------------------------------");
		Thread.sleep(1000);
		return false;
	}

	protected void readReportBoard(int reportBoardNum) {
		clear();
		try {
		ReportBoard reportBoard = rDAO.selectOne(reportBoardNum);
		System.out.println("===================== 신고 내역 =====================");
		System.out.println("신고번호 : " + reportBoard.getReportBoardNum());
		System.out.println("신고내용 : " + reportBoard.getContent());
		System.out.println("=================== 신고 된 게시판 ===================");
		System.out.println("==================================================");
		System.out.println("No." + reportBoard.getBoardNum() + " " + reportBoard.getBoardTitle());
		System.out.println("==================================================");
		System.out.println(reportBoard.getBoardContent());
		System.out.println("==================================================");
		System.out.println("작성자 : " + reportBoard.getMemberId());
		System.out.println("==================================================");}
		catch(NullPointerException e) 
		{
			return;
		}
	}

	protected void readReportBoardList() {
		System.out.println("=================== 신고 내역 ======================");
		List<ReportBoard> list = rDAO.selectAll();
		for (ReportBoard reportBoard : list) {
			System.out.println("NO." + reportBoard.getReportBoardNum() + " \t " + "게시판번호 : " + reportBoard.getBoardNum()
					+ " \t " + reportBoard.getBoardTitle());
		}
		System.out.println("==================================================");
	}

	protected void insertReport(Member member, Board board) {
		clear();
		System.out.println("==================================================");
		System.out.println("신고내용");
		System.out.println("==================================================");
		rDAO.insert(member, board, inputContent(member));
		return;
	}

	protected int menuSelect() {
		int menu = 0;
		try {
			System.out.print("번호>");
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return menu;
	}

	protected void menuPrint() {
		System.out.println("--------------------------------------------------");
		System.out.println("1.선택 9.뒤로가기");
		System.out.println("--------------------------------------------------");
	}
	protected void menuDeletePrint() {
		System.out.println("--------------------------------------------------");
		System.out.println("1.삭제 9.뒤로가기");
		System.out.println("--------------------------------------------------");
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}

	public void clear() {
		for (int i = 0; i < 57; ++i)
			System.out.println();
	}
}
