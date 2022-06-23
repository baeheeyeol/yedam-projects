package com.yedam.app.member;

public class Member {

	private String memberId;
	private String memberPwd;
	private int role;

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPwd() {
		return memberPwd;
	}

	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}

	@Override
	public String toString() {
		String str = "";
		if (role == 0) {
			return "관리자";
		} else {
			return memberId;
		}

	}

}
