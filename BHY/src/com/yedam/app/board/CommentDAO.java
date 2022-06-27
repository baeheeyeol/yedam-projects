package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;
import com.yedam.app.member.Member;

public class CommentDAO extends DAO {

	// 싱글톤
	private CommentDAO() {
	};

	private static CommentDAO dao = null;

	public static CommentDAO getInstance() {
		if (dao == null) {
			dao = new CommentDAO();
		}
		return dao;
	}

	// 삭제
	public void delete(int commentNum) {
		try {
			connect();
			System.out.println(commentNum);
			String sql = "delete from board_comment where comment_num in (select comment_num from board_comment START WITH comment_num=  " + commentNum + " CONNECT BY prior COMMENT_NUM=comment_num_PARENT)";
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sql);
			if (result > 0) {
				System.out.println("삭제되었습니다.");
			} else {
				System.out.println("삭제되지 않았습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 입력
	public void insert(int boardNum, Member member, String content) {
		try {
			connect();
			String sql = "INSERT INTO board_comment (board_num,member_id,comment_num,comment_content) VALUES(?,?,COMMENT_SEQ.NEXTVAL,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, member.getMemberId());
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
	public void insertToInsert(int boardNum, Member member, String content, int commentNumParent) {
		try {
			connect();
			String sql = "INSERT INTO board_comment (board_num,member_id,comment_num,comment_content,comment_num_parent) VALUES(?,?,COMMENT_SEQ.NEXTVAL,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, member.getMemberId());
			pstmt.setString(3, content);
			pstmt.setInt(4, commentNumParent);
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
	public void update(int comment_num, String content) {
		try {
			connect();
			String sql = "UPDATE BOARD_COMMENT SET COMMENT_CONTENT = ? WHERE COMMENT_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, comment_num);

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

	// 수정 - 투명여부
	public void update(int comment_num) {
		try {
			connect();
			String sql = "UPDATE BOARD_COMMENT SET COMMENT_INVISIBLE = 1 WHERE COMMENT_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comment_num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 댓글 단건조회
	public Comment selectOne(int commentNum) {
		Comment comment = null;
		try {
			connect();
			String sql = "SELECT * FROM BOARD_COMMENT WHERE COMMENT_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, commentNum);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				comment = new Comment();
				comment.setCommentNumParent(rs.getInt("comment_num_parent"));
				comment.setCommentNum(rs.getInt("COMMENT_NUM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return comment;
	}

	// 댓글 전체조회
	public List<Comment> selectAll() {
		List<Comment> list = new ArrayList<Comment>();
		try {

			connect();
			String sql = "SELECT* FROM BOARD_COMMENT";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentNum(rs.getInt("comment_num"));
				comment.setMemberId(rs.getString("member_id"));
				comment.setCommentNumParent(rs.getInt("comment_num_parent"));
				list.add(comment);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 댓글 전체조회 - 게시판번호
	public List<Comment> selectAll(int boardNum) {
		List<Comment> list = new ArrayList<Comment>();
		try {

			connect();
			String sql = "SELECT comment_num, lpad(' ',(level),'=')||comment_content AS content,member_id,COMMENT_INVISIBLE FROM BOARD_COMMENT "
					+ "WHERE BOARD_NUM = " + boardNum + " START WITH comment_num_parent is null"
					+ " CONNECT BY prior COMMENT_NUM=comment_num_PARENT";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentNum(rs.getInt("comment_num"));
				comment.setCommentContent(rs.getString("content"));
				comment.setMemberId(rs.getString("member_id"));
				comment.setCommentInvisible(rs.getInt("comment_invisible"));
				list.add(comment);
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

}
