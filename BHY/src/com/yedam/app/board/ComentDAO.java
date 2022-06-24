package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class ComentDAO extends DAO {

	// 싱글톤
	private ComentDAO() {
	};

	private static ComentDAO dao = null;

	public static ComentDAO getInstance() {
		if (dao == null) {
			dao = new ComentDAO();
		}
		return dao;
	}

	// 입력
	public void insert(int boardNum, String memberId, String content) {
		try {
			connect();
			String sql = "INSERT INTO board_coment (board_num,member_id,coment_num,coment_content) VALUES(?,?,COMENT_SEQ.NEXTVAL,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, memberId);
			pstmt.setString(3, content);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("등록되었습니다.");
			} else {
				System.out.println("등록되지 않았습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 대댓글
	public void insertInsert(int boardNum, String memberId, int comentNUm, String content) {
		try {
			connect();
			String sql = "INSERT INTO board_coment VALUES(?,?,COMENT_SEQ.NEXTVAL,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, memberId);
			pstmt.setString(3, content);
			pstmt.setInt(4, comentNUm);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("등록되었습니다.");
			} else {
				System.out.println("등록되지 않았습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 수정
	public void update(int boardNum, String memberId, String content) {
		try {
			connect();
			String sql = "UPDATE BOARD_COMENT SET BOARD_COMENT = ? WHERE BOARD_NUM = ? AND MEMBER_ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, boardNum);
			pstmt.setString(3, memberId);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("등록되었습니다.");
			} else {
				System.out.println("등록되지 않았습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 게시판 전체조회
	public List<Coment> selectAll(int boardNum) {
		List<Coment> list = new ArrayList<Coment>();
		try {
			
			connect();
			String sql = "SELECT * FROM board_COMENT WHERE BOARD_NUM = "+ boardNum+"START WITH coment_num =0 "
					+ "CONNECT BY prior COMENT_NUM=coment_coment_num";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Coment coment = new Coment();
				coment.setBoardNum(rs.getInt("board_num"));
				coment.setComentContent(rs.getString("coment_content"));
				coment.setComentNum(rs.getInt("coment_num"));
				coment.setMemberId(rs.getString("member_id"));
				coment.setComentcomentNum(rs.getInt("coment_coment_num"));
				list.add(coment);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 댓글 번호 조회
	public List<Integer> selectAllNum() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			connect();
			String sql = "SELECT COMENT_NUM FROM BOARD_COMENT ORDER BY COMENT_NUM";
			stmt.getConnection();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getInt("COMENT_NUM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 대댓글 여부 조회
	public boolean selectComentComent(String memberId, int boardNum, int comentNum) {
		int num = 0;
		try {
			connect();
			String sql = "SELECT COMENT_COMENT_NUM FROM BOARD_COMENT WHERE BOARD_NUM = " + boardNum + "AND MEMBER_ID = "
					+ memberId + "AND COMENT_NUM = " + comentNum;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				num = rs.getInt("COMENT_COMENT_NUM");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		if (num == 0) {
			return true;
		} else {
			return false;
		}
	}

	
}
