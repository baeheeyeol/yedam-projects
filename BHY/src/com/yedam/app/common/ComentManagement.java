package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Comment;
import com.yedam.app.board.CommentDAO;
import com.yedam.app.member.Member;

public class ComentManagement {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	CommentDAO cDAO = CommentDAO.getInstance();

	public ComentManagement(Member member, Board board) {

		while (true) {
			menuPrint();

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 입력
				insertComent(member.getMemberId(), board);
			} else if (menuNo == 2) {
				// 수정
				updateComent(member.getMemberId());
			} else if (menuNo == 3) {
				// 삭제
				deleteComent(member.getMemberId());
			} else if (menuNo == 9) {
				exit();
				break;
				// 뒤로가기
			} else {
				showInputError();
			}
		}
	}

	void deleteComent(String memberId) {
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(memberId)) {
				if (checkComentParent(commentNum)) {
					cDAO.update(commentNum);
				} else {
					cDAO.delete(commentNum);
				}
			}
		}
	}

	boolean checkComentParent(int commentNum) {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			if (commentNum == comment.getCommentNumParent()) {
				return true;
			}
		}
		return false;
	}

	protected void updateComent(String memberId) {
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(memberId)) {
				cDAO.update(commentNum, inputContent());
			}
		}
	}

	// 등록
	protected void insertComent(String memberId,Board board) {
		System.out.println("1.댓글 2.대댓글");
		System.out.print("번호>");
		int num = menuSelect();
		if (num == 1) {
			// 댓글 작성
			cDAO.insert(board.getBoardNum(), memberId, inputContent());
		} else if (num == 2) {
			// 대댓글 작성
			List<Comment> list = cDAO.selectAll(board.getBoardNum());
			System.out.println("댓글");
			System.out.println("====================");
			for (Comment comment : list) {
				if (comment.getCommentInvisible() == 1) {
					System.out.println(comment.getCommentNum()+". 삭제된 댓글입니다.");
				} else {
					System.out.println(comment.getCommentNum() + "." + comment.getCommentContent() + " /id : "
							+ comment.getMemberId());
				}
			}
			System.out.println("====================");
			int commentNum = selectComment();
			
			if (checkComentNum(commentNum)) {
				cDAO.insertToInsert(board.getBoardNum(), memberId, inputContent(), commentNum);
			}
		}
	}

	int selectComment() {
		int num = 0;
		try {
			System.out.print("댓글번호>");
			num = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return num;
	}

	String inputContent() {
		System.out.print("내용입력>");
		return sc.nextLine();
	}

	// 본인 댓글 여부 확인
	boolean checkMemberId(String memberId) {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			if (comment.getMemberId().equals(memberId)) {
				return true;
			}
		}
		System.out.println("본인의 댓글이 아닙니다.");
		return false;
	}

	// 댓글 존재 확인
	boolean checkComentNum(int commentNum) {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			if (comment.getCommentNum() == commentNum) {
				return true;
			}
		}
		System.out.println("선택한 번호의 댓글이 존재하지 않습니다.");
		return false;
	}

	void ComentUpdate(String memberId, int boardNum) {
		cDAO.insert(boardNum, memberId, insertContent());
	}

	protected String insertContent() {
		System.out.println("내용을 입력하세요");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.println("1.댓글작성 2.댓글수정 3.댓글삭제 9.뒤로가기");
	}

	protected int menuSelect() {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return menu;
	}

	protected void exit() {
		System.out.println("뒤로가기");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}
}
