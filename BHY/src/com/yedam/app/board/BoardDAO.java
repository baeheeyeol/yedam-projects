package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class BoardDAO extends DAO {
	// 싱글톤
	private BoardDAO() {
	};

	private static BoardDAO dao = null;

	public static BoardDAO getInstance() {
		if (dao == null) {
			dao = new BoardDAO();
		}
		return dao;
	}

	// 등록
	public void insert(Board board) {
		try {
			connect();
			String sql = "INSERT INTO BOARD VALUES(board_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_title());
			pstmt.setString(2, board.getMember_id());

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

	public int seq() {
		int boardNum = 0;
		try {
			connect();
			String sql = "SELECT BOARD_NUM FROM (SELECT *FROM BOARD ORDER BY BOARD_NUM desc) S WHERE ROWNUM<=1";
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

	public void insertcontent(Board board) {
		try {
			connect();
			String sql = "INSERT INTO BOARD_CONTENT VALUES(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBoard_num());
			pstmt.setString(2, board.getBoard_content());

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
			String sql = "DELETE FROM BOARD WHERE BOARD_NUM = ?";
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

	// 수정 - 내용
	public void updateContent(Board board) {
		try {
			connect();
			String sql = "UPDATE FROM BOARD SET Board_content = ? where BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_content());
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

	// 번호로 제목 선택
	public String selectTitle(int boardId) {
		String boardTitle = null;
		try {
			connect();
			String sql = "SELECT BOARD_TITLE FORM BOARD WHERE BOARD_NUM =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			if (rs.next()) {
				boardTitle = rs.getString("BOARD_TITLE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return boardTitle;
	}

	// 전체
	public List<Board> selelctAll() {
		List<Board> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM board join board_content using (board_num) ORDER BY board_num";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Board Board = new Board();
				Board.setBoard_num(rs.getInt("board_num"));
				Board.setBoard_title(rs.getString("board_title"));
				Board.setBoard_content(rs.getString("board_content"));
				list.add(Board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}
}
