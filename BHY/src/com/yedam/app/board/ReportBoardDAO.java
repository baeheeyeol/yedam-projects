package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;
import com.yedam.app.member.Member;

public class ReportBoardDAO extends DAO {
	private ReportBoardDAO() {
	};

	private static ReportBoardDAO dao = null;

	public static ReportBoardDAO getInstance() {
		if (dao == null) {
			dao = new ReportBoardDAO();
		}
		return dao;
	}

	public void delete(int boardNum,int reportBoardNum) {

		try {
			connect();
			String sql1 = "DELETE FROM BOARD_CONTENT WHERE BOARD_NUM =?";
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, boardNum);
			pstmt.executeUpdate();
			String sql = "DELETE FROM BOARD WHERE BOARD_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.executeUpdate();
			String sql2 ="DELETE FROM REPORT_BOARD WHERE REPORT_BOARD_NUM =?";
			pstmt=conn.prepareStatement(sql2);
			pstmt.setInt(1, reportBoardNum);
			int result =pstmt.executeUpdate();
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
	
	// 입력
	public void insert(Member member, Board board, String content) {
		try {
			connect();
			String sql = "INSERT INTO REPORT_BOARD VALUES(?,?,?,rt_board_seq.nextval,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setInt(2, board.getBoardNum());
			pstmt.setString(3, content);
			pstmt.setString(4,board.getBoardTitle());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("입력되었습니다.");
			} else {
				System.out.println("입력되지 않았습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	public ReportBoard selectOne(int reportBoardNum) {
		ReportBoard reportBoard = null;
		try {
			connect();
			String sql = "SELECT *FROM REPORT_BOARD JOIN BOARD_CONTENT USING(BOARD_NUM) WHERE REPORT_BOARD_NUM = " + reportBoardNum;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				reportBoard = new ReportBoard();
				reportBoard.setBoardNum(rs.getInt("board_num"));
				reportBoard.setContent(rs.getString("report_content"));
				reportBoard.setMemberId(rs.getString("member_id"));
				reportBoard.setReportBoardNum(rs.getInt("report_board_num"));
				reportBoard.setBoardTitle(rs.getString("board_title"));
				reportBoard.setBoardContent(rs.getString("board_content"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return reportBoard;
	}

	// 전체조회
	public List<ReportBoard> selectAll() {
		List<ReportBoard> list = new ArrayList<ReportBoard>();
		try {
			connect();
			String sql = "SELECT *FROM REPORT_BOARD";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ReportBoard reportBoard = new ReportBoard();
				reportBoard.setBoardNum(rs.getInt("board_num"));
				reportBoard.setContent(rs.getString("report_content"));
				reportBoard.setMemberId(rs.getString("member_id"));
				reportBoard.setReportBoardNum(rs.getInt("report_board_num"));
				reportBoard.setBoardTitle(rs.getString("board_title"));
				list.add(reportBoard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;

	}
}
