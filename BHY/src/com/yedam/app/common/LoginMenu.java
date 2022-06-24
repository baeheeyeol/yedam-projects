package com.yedam.app.common;

import java.util.List;

import java.util.Scanner;
import com.yedam.app.member.Member;
import com.yedam.app.member.MemberDAO;

public class LoginMenu {

	protected Scanner sc = new Scanner(System.in);
	protected MemberDAO mDAO = MemberDAO.getInstance();
	protected String memberId;
	protected int role;

	public LoginMenu() {
		while (true) {
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				signup();
			} else if (menuNo == 2) {
				login();
			} else if (menuNo == 9) {
				// 프로그램 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void menuPrint() {
		System.out.println("======================");
		System.out.println("1.회원가입 2.로그인 9.종료");
		System.out.println("======================");
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
//1.
	protected void login() {
		// 아이디와 비밀번호 입력
		Member inputInfo = memberInputAll();
		// 로그인 시도
		if (MemberDAO.getInstance().selectOne(inputInfo) == null) {
			// 실패할 경우 그대로 메소드 종료
			return;
		}
		// 성공할 경우 프로그램을 실행
		inputInfo = mDAO.selectOne(inputInfo.getMemberId());
		new Management(inputInfo);
	}

	// 회원가입
	protected void signup() {
		String id = inputId();
		List<String> list = mDAO.selectAllId();
		boolean idCheck = false;
		for (int i = 0; i < list.size(); i++) {
			if (id.equals(list.get(i))) {
				System.out.println("등록된 id입니다.");
				idCheck = true;
				break;
			}
		}
		if (!idCheck) {
			Member mb = new Member();
			mb.setMemberId(id);
			mb.setMemberPwd(inputPassword());
			mDAO.insert(mb);
		}
	}

	protected String inputId() {
		System.out.printf("ID>");
		return sc.nextLine();
	}

	protected String inputPassword() {
		System.out.printf("Password>");
		return sc.nextLine();
	}

	protected Member memberInputAll() {
		Member temp = new Member();
		System.out.printf("ID>");
		temp.setMemberId(sc.nextLine());
		System.out.printf("PASSWORD>");
		temp.setMemberPwd(sc.nextLine());
		return temp;
	}
}
