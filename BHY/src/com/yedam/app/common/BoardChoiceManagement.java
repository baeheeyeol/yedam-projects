package com.yedam.app.common;

import java.util.Scanner;

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
			if (menuNo ==1) {
				// 번호로 선택
				choiceBoardnum();
			} else if (menuNo == 2) {
				// 제목으로 선택
				choiceBoardtitle();
			} else if (menuNo == 3) {
				// 작성한글 선택
				choiceBoardmy();
			} else if (menuNo == 9) {
				// 프로그램 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void choiceBoardnum() 
	{
		System.out.println("번호>");
		
		
		
	}
	protected void menuPrint(String memberId) {
		System.out.println("선택방법>");
		System.out.println("1.번호 2.제목 3.작성한글");
	};

	protected String inputmenu() 
	{
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
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}
}
