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
			String sql = "INSERT INTO BOARD (board_num,board_title,member_id) VALUES(board_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getMemberId());

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
			String sql = "INSERT INTO BOARD_CONTENT VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBoardNum());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardInvisible());

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
			String sql1 = "DELETE FROM BOARD_CONTENT WHERE BOARD_NUM =?";
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
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

	public void updateContent(String content, Board board) {
		try {
			connect();
			String sql = "UPDATE BOARD_content SET Board_content = ? where BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, board.getBoardNum());
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
	public void updateTitle(String title, Board board) {

		try {
			connect();
			String sql = "UPDATE BOARD SET BOARD_TITLE = ? WHERE BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setInt(2, board.getBoardNum());
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

	// 게시판 모든 번호 조회
	public List<Integer> selectAllBoardNum() {
		List<Integer> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT BOARD_NUM FROM BOARD";
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

	// 단건 조회 - 제목 + 내용
	public Board selectOneAll(int boardNum) {
		Board board = null;
		;
		try {
			connect();
			String sql = "SELECT * FROM BOARD JOIN BOARD_CONTENT USING(BOARD_NUM) WHERE BOARD_NUM = " + boardNum;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				board = new Board();
				board.setBoardContent(rs.getString("board_content"));
				board.setBoardNum(rs.getInt("board_num"));
				board.setBoardTitle(rs.getString("board_title"));
				board.setMemberId(rs.getString("member_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return board;
	}

	// 게시판 모든 제목 조회
	public List<String> selectAllBoardTitle() {
		List<String> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT BOARD_Title FROM BOARD";
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


	// 게시판 번호로 하나선택
	public Board selectOne(int boardNum) {
		Board board = new Board();
		try {
			connect();
			String sql = "SELECT * FROM BOARD WHERE BOARD_NUM = " + boardNum;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				board.setBoardNum(rs.getInt("board_num"));
				board.setBoardTitle(rs.getString("board_title"));
				board.setMemberId(rs.getString("member_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return board;
	}

	// 게시판 제목으로 하나선택
	public Board selectOne(String boardTitle) {
		Board board = new Board();
		try {
			connect();
			String sql = "SELECT * FROM BOARD WHERE BOARD_TITLE = '" + boardTitle + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				board.setBoardNum(rs.getInt("board_num"));
				board.setBoardTitle(rs.getString("board_title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return board;
	}

	// 모든 제목 조회
	public List<String> selectAllTitle() {
		List<String> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT BOARD_TITLE FROM BOARD";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("BOARD_TITLE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	public List<Board> selectAll(String memberId) {
		List<Board> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM BOARD WHERE MEMBER_ID = '" + memberId + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Board Board = new Board();
				Board.setBoardNum(rs.getInt("board_num"));
				Board.setBoardTitle(rs.getString("board_title"));
				list.add(Board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}
	public List<Board> selectAll1() {
		List<Board> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM BOARD";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Board Board = new Board();
				Board.setBoardNum(rs.getInt("board_num"));
				Board.setBoardTitle(rs.getString("board_title"));
				list.add(Board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 전체
	public List<Board> selelctAll() {

		List<Board> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM board join board_content using (board_num) ORDER BY board_num desc";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Board Board = new Board();
				Board.setBoardNum(rs.getInt("board_num"));
				Board.setBoardTitle(rs.getString("board_title"));
				Board.setBoardContent(rs.getString("board_content"));
				Board.setMemberId(rs.getString("member_id"));
				Board.setBoardInvisible(rs.getInt("board_invisible"));
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
