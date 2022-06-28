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
	protected boolean nonMember;
	protected String nonMemberId;

	public LoginMenu() throws InterruptedException {
		this.nonMember = false;
		while (true) {
			clear();
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				signup();
			} else if (menuNo == 2) {
				login();
			} else if (menuNo == 3) {
				loginNonMember();
			} else if (menuNo == 9) {
				// 프로그램 종료
				if (nonMember) {
					deleteNonMember();
				}
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	protected void menuPrint() {
		System.out.println("=======================================================");
		System.out.println("1.회원가입 2.로그인 3.비회원 로그인 9.종료");
		System.out.println("=======================================================");
		System.out.print("번호>");
	}

	protected int menuSelect() throws InterruptedException {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
			Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() throws InterruptedException {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
		Thread.sleep(1000);
	}

	void loginNonMember() throws InterruptedException {
		
		
		Member mb = new Member();
		int num = (int) (Math.random() * 1001);
		String nonMemberId = "guest" + num;
		String nonMemberPwd = "0000";
		mb.setMemberId(nonMemberId);
		mb.setMemberPwd(nonMemberPwd);
		mb.setRole(3);
		mDAO.insert(mb);
		new Management().ManagementRun(mb);
		this.nonMember = true;
		this.nonMemberId = mb.getMemberId();
	}

	void deleteNonMember() {
		mDAO.delete(nonMemberId);
	}

	protected void login() throws InterruptedException {
		Member inputInfo = memberInputAll();
		if (MemberDAO.getInstance().selectOne(inputInfo) == null) {
			return;
		}
		inputInfo = mDAO.selectOne(inputInfo.getMemberId());
		new Management().ManagementRun(inputInfo);
	}

	// 회원가입
	protected void signup() throws InterruptedException {
		String id = inputId();
		List<String> list = mDAO.selectAllId();
		boolean idCheck = false;
		for (int i = 0; i < list.size(); i++) {
			if (id.equals(list.get(i))) {
				System.out.println("등록된 id입니다.");
				Thread.sleep(1000);
				idCheck = true;
				break;
			}
		}
		if (!idCheck) {
			Member mb = new Member();
			mb.setMemberId(id);
			mb.setMemberPwd(inputPassword());
			mb.setRole(1);
			mDAO.insert(mb);
		}
	}

	protected String inputId() throws InterruptedException {
		String Id;
		while (true) {
			System.out.printf("ID>");
			Id = sc.nextLine();
			if (Id.length() >= 8) {
				System.out.println("8글자 미만으로 설정해주세요");
				Thread.sleep(1000);
			} else {
				break;
			}

		}
		clear();
		return Id;
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
		clear();
		return temp;
	}

	public void clear() {
		for (int i = 0; i < 57; ++i)
			System.out.println();
	}
}
