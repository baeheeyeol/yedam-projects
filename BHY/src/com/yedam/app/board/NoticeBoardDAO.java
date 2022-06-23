package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class NoticeBoardDAO extends DAO {
	private NoticeBoardDAO() {
	};

	private static NoticeBoardDAO dao = null;

	public static NoticeBoardDAO getInstance() {
		if (dao == null) {
			dao = new NoticeBoardDAO();
		}
		return dao;
	}

	// 등록
	public void insert(NoticeBoard NoticeBoard) {
		try {
			connect();
			String sql = "INSERT INTO NOTICE_BOARD VALUES(NT_board_seq.nextval,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, NoticeBoard.getNtboard_title());
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

	
	// 수정 - 내용
	public void updateContent(NoticeBoard NoticeBoard) {
		try {
			connect();
			String sql = "UPDATE FROM BOARD SET Nt_board_content = ? where nt_BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, NoticeBoard.getNtboard_content());
			pstmt.setInt(2, NoticeBoard.getNtboard_num());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("삭제되었습니다.");
			} else {
				System.out.println("삭제되지 않았습니다.");
			}
		} catch (SQLException e) {

		} finally {
			disconnect();
		}
	}

	// 수정 - 제목
	public void updateTitle(Board board) {
		try {
			connect();
			String sql = "UPDATE FROM BOARD SET BOARD_TITLE = ? WHERE BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_title());
			pstmt.setInt(2, board.getBoard_num());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("삭제되었습니다.");
			} else {
				System.out.println("삭제되지 않았습니다.");
			}
		} catch (SQLException e) {

		} finally {
			disconnect();
		}
	}

	// 전체
	public List<NoticeBoard> selelctAll() {
		List<NoticeBoard> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM Notice_board join Ntboard_content using (ntboard_num) ORDER BY ntboard_num";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NoticeBoard NtBoard = new NoticeBoard();
				NtBoard.setNtboard_num(rs.getInt("board_num"));
				NtBoard.setNtboard_title(rs.getString("board_title"));
				NtBoard.setNtboard_content(rs.getString("board_content"));
				list.add(NtBoard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}
}
