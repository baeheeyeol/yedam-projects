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

	public int seq() {

		int boardNum = 0;
		try {
			connect();
			String sql = "SELECT BOARD_NUM FROM (SELECT *FROM NOTICE_BOARD ORDER BY BOARD_NUM desc) S WHERE ROWNUM<=1";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				boardNum = rs.getInt("BOARD_NUM");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return boardNum;
	}

	public void insertcontent(NoticeBoard noticeBoard) {

		try {
			connect();
			String sql = "INSERT INTO NOTICE_BOARD_CONTENT VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeBoard.getBoardNum());
			pstmt.setString(2, noticeBoard.getBoardContent());
			System.out.println(noticeBoard.getBoardNum());
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
	// 삭제

	public void delete(int board_num) {

		try {
			connect();
			String sql1 = "DELETE FROM NOTICE_BOARD_CONTENT WHERE BOARD_NUM =?";
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
			String sql = "DELETE FROM NOTICE_BOARD WHERE BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_num);
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

	// 등록
	public void insert(NoticeBoard noticeBoard) {
		try {
			connect();
			String sql = "INSERT INTO NOTICE_BOARD(board_num,board_title,member_id) VALUES(NT_board_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, noticeBoard.getBoardTitle());
			pstmt.setString(2, noticeBoard.getMemberId());
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
	public void updateContent(String content, NoticeBoard NoticeBoard) {
		try {
			connect();
			String sql = "UPDATE NOTICE_BOARD_CONTENT SET board_content = ? where BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			System.out.println(content);
			pstmt.setString(1, content);
			pstmt.setInt(2, NoticeBoard.getBoardNum());
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
	public void updateTitle(String title, NoticeBoard NoticeBoard) {
		try {
			connect();
			String sql = "UPDATE NOTICE_BOARD SET BOARD_TITLE = ? WHERE BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			System.out.println(title);
			pstmt.setString(1, title);
			pstmt.setInt(2, NoticeBoard.getBoardNum());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("수정되었습니다.");
			} else {
				System.out.println("수정되지 않았습니다.");
			}
		} catch (SQLException e) {

		} finally {
			disconnect();
		}
	}

	// 전체 게시판 번호 조회
	public List<Integer> selectAllBoardNum() {
		List<Integer> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT BOARD_NUM FROM NOTICE_BOARD";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getInt("BOARD_NUM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 젠체 게시판 제목 조회
	public List<String> selectAllBoardTitle() {
		List<String> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT BOARD_Title FROM NOTICE_BOARD";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("BOARD_Title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 단건조회 - 제목 - 번호로 조회
	public NoticeBoard selectOneTitle(int board_num) {
		NoticeBoard ntBoard = null;
		try {
			connect();
			String sql = "SELECT * FROM NOTICE_BOARD WHERE BOARD_NUM =" + board_num;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				ntBoard = new NoticeBoard();
				ntBoard.setMemberId(rs.getString("member_id"));
				ntBoard.setBoardNum(rs.getInt("board_num"));
				ntBoard.setBoardTitle(rs.getString("board_title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return ntBoard;
	}

	// 단건조회 - 제목+내용
	public NoticeBoard selectOneAll(int board_num) {
		NoticeBoard NoticeBoard = null;
		try {
			connect();
			String sql = "SELECT * FROM Notice_board join Notice_board_content using (board_num) WHERE BOARD_NUM = "
					+ board_num + " ORDER BY board_num";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NoticeBoard = new NoticeBoard();
				System.out.println(board_num);
				NoticeBoard.setBoardNum(rs.getInt("board_num"));
				NoticeBoard.setBoardTitle(rs.getString("board_title"));
				NoticeBoard.setBoardContent(rs.getString("board_content"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return NoticeBoard;
	}

	// 전체
	public List<NoticeBoard> selelctAll() {
		List<NoticeBoard> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM Notice_board join Notice_board_content using (board_num) ORDER BY board_num";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NoticeBoard NtBoard = new NoticeBoard();
				NtBoard.setBoardNum(rs.getInt("board_num"));
				NtBoard.setBoardTitle(rs.getString("board_title"));
				NtBoard.setBoardContent(rs.getString("board_content"));
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
