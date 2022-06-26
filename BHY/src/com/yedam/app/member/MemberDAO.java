package com.yedam.app.member;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import com.yedam.app.common.DAO;

public class MemberDAO extends DAO {

	// 싱글톤
	private MemberDAO() {
	};

	private static MemberDAO dao = null;

	public static MemberDAO getInstance() {
		if (dao == null) {
			dao = new MemberDAO();
		}
		return dao;
	}

	// 전체조회
	public List<Member> selectAll() {
		List<Member> list = new ArrayList<Member>();
		try {
			connect();
			String sql = "SELECT * FROM MEMBERS ORDER BY MEMBER_ID";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Member mb = new Member();
				mb.setMemberId(rs.getString("member_id"));
				mb.setMemberPwd(rs.getString("member_pwd"));
				list.add(mb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 전체조회 -id
	public List<String> selectAllId() {
		List<String> list = new ArrayList<String>();
		try {
			connect();
			String sql = "SELECT MEMBER_ID FROM MEMBERS ORDER BY MEMBER_ID";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("member_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 등록
	public void insert(Member member) {
		try {
			connect();
			String sql = "INSERT INTO MEMBERS VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPwd());
			pstmt.setInt(3, member.getRole());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("회원가입성공");
			} else {
				System.out.println("회원가입실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// role 검색
	public Member selectOne(String id) {
		Member mb = null;
		try {
			connect();
			String sql = "SELECT * FROM MEMBERS WHERE MEMBER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				mb = new Member();
				mb.setMemberPwd(rs.getString("member_pwd"));
				mb.setMemberId(rs.getString("member_id"));
				mb.setRole(rs.getInt("member_role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return mb;
	}

	// 삭제
	public void delete(String memberId) {
		try {
			connect();
			String sql = "DELETE FROM MEMBERS WHERE MEMBER_ID =? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("정상적으로 삭제되었습니다.");
			} else {
				System.out.println("정상적으로 삭제되지 않았습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	public Member selectOne(Member member) {

		Member loginInfo = null;
		try {
			connect();
			String sql = "SELECT *FROM members WHERE member_id = '" + member.getMemberId() + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				// 아이디 존재
				if (rs.getString("member_pwd").equals(member.getMemberPwd())) {
					// 비밀번호 일치
					// -> 로그인 성공
					loginInfo = new Member();
					loginInfo.setMemberId(rs.getString("member_id"));
					loginInfo.setMemberPwd(rs.getString("member_pwd"));
					loginInfo.setRole(rs.getInt("member_role"));
				} else {
					System.out.println("비밀번호가 일치하지 않습니다.");
				}
			} else {
				System.out.println("아이디가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return loginInfo;
	}
}
